# AGENTS.md

Guidance for AI coding agents (Codex, Cursor, Copilot, Claude Code, …) working in this repository. `CLAUDE.md` imports this file, so keep all shared guidance here.

## First step: ask for a message dump

**Before starting any work, ask the user for a chat recording made on the Dexland server with the ChatImprover message dump enabled**, unless they already gave you one. Almost everything in this addon depends on the exact structure of Dexland's messages. A real recording saves a lot of tokens and time otherwise spent guessing formats from code.

Tell the user how to record it:
1. Start the game with the JVM argument `-Dchatimprover.debug=true` (the launcher's Java arguments field), or in this repo with `./gradlew runClient --args="-debug-chatimprover"`.
2. Join Dexland.
3. Trigger the messages in question.
4. Send the `logs/chatimprover/messages-*.jsonl` file.

If the flag has no effect, the installed ChatImprover is too old to have the dump. If the user has no recording or says it isn't needed, continue without it.

The first line of the dump documents its format. In short:
- `plain` is `getString()`.
- `tree` holds each node's Java access `path`, `content`, own `style`, `string` and `siblings`; that is what validators here match on.
- `json` is the whole message, decodable with `ComponentSerialization.CODEC`.

## What this is

ChatImprove-dexland is a client-side Fabric addon (mod id `chatimprove_dexland`, Minecraft 26.2, Java 25) for the Dexland server. It runs on top of the [ChatImprover](https://github.com/Ayoree/ChatImprover) library mod. It does three things:
- Hides server spam.
- Makes nicknames in chat, private messages and punishment messages clickable: suggested `/m <nick>` or moderation commands.
- Resolves disguised nicknames.

Player-facing strings are Russian; code and comments are English. Server addresses the addon is active on live in `ChatImprove_dexland.IPs`.

**Library reference (read it before changing anything that touches the library API):** ChatImprover's [AGENTS.md](https://github.com/Ayoree/ChatImprover/blob/main/AGENTS.md) (raw: `https://raw.githubusercontent.com/Ayoree/ChatImprover/main/AGENTS.md`). It describes:
- the message pipeline;
- the public API contract;
- Minecraft 26.2 naming specifics;
- the message dump format.

API sources: [`org.ayoree.chatimprover.api`](https://github.com/Ayoree/ChatImprover/tree/main/src/main/java/org/ayoree/chatimprover/api). Use only classes from that package; everything else in ChatImprover is internal.

## Build & run

Requires a local JDK 25. Versions live in `gradle.properties`; `chat_improver_version` must be a published ChatImprover `mod_version`.

| Command | Purpose |
|---|---|
| `./gradlew build` | Compile + license header check → `build/libs/`. CI runs this on every push/PR. |
| `./gradlew runClient` | Dev client with ChatImprover, owo-lib, YACL and ModMenu on the classpath. |
| `./gradlew runClient --args="-debug-chatimprover"` | Same, with the ChatImprover message dump enabled. |
| `./gradlew applyLicenses` | Add the header from `HEADER.txt` to Java files; `build` fails without it. |

- **Where ChatImprover comes from:** GitHub Packages `https://maven.pkg.github.com/Ayoree/ChatImprover`. It needs a token in the env var `AYOREE_GET_PACKAGES_KEY`; CI takes it from the `github-packages` environment secret. `mavenLocal()` is checked first, so `./gradlew publishToMavenLocal` in a checkout of the ChatImprover repo lets this addon build against unreleased library changes.
- **Tests:** none. Verify with `build`, then in `runClient` on Dexland itself; providers only activate on the addresses in `IPs`.
- **Branches:** one per Minecraft version (`1.21.11`, `26.2`); `master` tracks the current one.

## Architecture

The entry point `ChatImprove_dexland.onInitialize()` runs `Config.init()`, `RealnameHandler.init()` and `RealnameReceiver.init()`. Everything else is discovered by ChatImprover through `ServiceLoader`.

### Providers

- Every provider is a nested `ProviderImpl` class.
  - It extends `AddonInformerImpl`, which supplies `addonID()` = `MOD_ID` and `IPs()`.
  - It implements `Filter.Provider` or `ChatMessage.Provider`.
  - It is annotated `@AutoService(Provider.class)`. Without the annotation the provider is silently never loaded.
- **Filters** (`FilterImpl`): one provider per spam type, each gated by a `Config.isBlock*` flag. `validator()` returning true **blocks** the message.
- **Messages** (`messages/`): one class per message type.
  - It extends a library base (`ChatMessage`, `ChatMessageWithSender`, `ChatMessageWithReceiverAndSender`) and has its own `ProviderImpl` (`validator()` + `creator()`).
  - The constructor parses nicks out of the message.
  - `generateChangedMsg()` builds `m_changedMsg`, usually by copying `m_message` and replacing the style of the nick sibling with click/hover events.
- **Matching style:** mostly an exact sibling count plus `getSiblings().get(i).getString()` comparisons; sometimes a `getString()` prefix or a regex. These encode Dexland's current message formats and break when the server changes them. That is what the dump is for.
- **Validators** run for every system chat message on Dexland.
  - Keep them cheap, and check sizes before `get(i)`: ChatImprover does not catch exceptions from validators or constructors.
  - A constructor may only rely on what its validator guaranteed.
  - The first matching provider wins, and the order between providers is undefined, so validators must be mutually exclusive.
- **Colors:** `IChatMessageWithoutStyle.removeStyle()` strips player-chosen colors when `Config.isRemoveColors` is on.

### Message groups

- **Punishments** (`messages/punishments/`):
  - Hierarchy: `AnyPunishChatMessage` → `PunishChatMessage` (ban/mute/warn/kick/jail) and `UnPunishChatMessage`.
  - Clicking a nick suggests a command built from the `Config.incorrect*Msg` templates (`{NICKNAME}` = moderator, `{RECEIVER}` = punished player).
  - The punishment reason arrives as a separate, following message. `ReasonChatMessage` attaches it to `PunishChatMessage.getLastMessage()`, so this relies on message order.
- **Punishment lists** (`messages/punishments/lists/`):
  - These are lines of punishment list output.
  - The server name is parsed from `(#surv-…)` and appended to suggested commands when `Config.isAutoServerSuffix` is on.
- **Disguised nicknames** start with `~~`.
  1. `RealnameReceiver.getRealName()` returns the cached real nick, or sends `/realname ~` once and returns the disguised nick for now.
  2. While the reply arrives, `RealnameHandler` hides its lines. It is an `ALLOW_GAME` listener registered in `ChatImproverApi.ADDON_PHASE`, so it runs before ChatImprover's own listener, which may cancel the event; it skips action-bar (`overlay`) messages.
  3. `RealnameChatMessage`'s constructor stores each disguised → real pair.

  Any new `ALLOW_GAME` listener must follow the same two rules.
- **Config:** YACL v3 (`Config`).
  - The GUI is generated from the `@AutoGen` annotations, in field order, and saved to `config/chatimprover/chatimprove_dexland.json5`.
  - `ModMenuIntegration` exposes it; the addon appears under ChatImprover in ModMenu via `custom.modmenu.parent`.
  - A new option needs the annotated field plus `yacl3.config.chatimprove_dexland:config.<field>` (and `.desc`) keys in **both** `assets/chatimprove_dexland/lang/en_us.json` and `ru_ru.json`.

## Conventions

- Every `.java` file starts with the AGPL-3.0 header from this repo's `HEADER.txt`. The licenser enforces it; run `applyLicenses`.
- Code style:
  - `m_` prefix on instance fields; `s_` on static constants such as sibling indexes.
  - `final` locals.
  - Legacy `§` color codes in generated text.
- Minecraft code uses Mojang official names (no Yarn), e.g. `Component`, `ClickEvent.SuggestCommand`, `HoverEvent.ShowText`. See the library's AGENTS.md for 26.2 API shapes.
