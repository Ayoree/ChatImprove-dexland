/*
 * ChatImprove-dexland, a Minecraft mod-addon for <https://github.com/Ayoree/ChatImprover>
 * Copyright (C) Ayoree <https://github.com/Ayoree>
 * Copyright (C) ChatImprove-dexland contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.ayoree.chatimprove.dexland.messages;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.ayoree.chatimprove.dexland.AddonInformerImpl;
import org.ayoree.chatimprove.dexland.Config;
import org.ayoree.chatimprover.api.ChatMessage;
import org.ayoree.chatimprover.api.ChatMessageWithSender;

import com.google.auto.service.AutoService;

// Global chat / Local chat
public class CommonChatMessage extends ChatMessageWithSender implements IChatMessageWithoutStyle {
    protected final String m_textPart;
    private final boolean m_isGlobal;

    @SuppressWarnings("null")
    public CommonChatMessage(Component message) {
        super(message);
        
        final List<Component> siblings = message.getSiblings();
        final ClickEvent clickEvent = siblings.getFirst().getStyle().getClickEvent();
        assert clickEvent != null && clickEvent.action() == ClickEvent.Action.SUGGEST_COMMAND;
        final ClickEvent.SuggestCommand suggestCommand = (ClickEvent.SuggestCommand) clickEvent;
        final String command = suggestCommand.command();
        m_isGlobal = getMessageStr().startsWith("[ɢ]");
        setSenderNick(command.substring(5, command.length() - 1)); // /msg ник
        m_textPart = siblings.get(1).getString();
    }

    @AutoService(Provider.class)
    public static class ProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Component> validator() {
            return message -> {
                // Do not create CommonChatMessage instance if disabled this config option
                final List<Component> siblings = message.getSiblings();
                return siblings.size() == 2 && siblings.getFirst().getString().endsWith("→ ") && siblings.getFirst().getStyle().getHoverEvent() != null;
            };
        }
        @Override
        public Function<Component, ChatMessage> creator() {
            return CommonChatMessage::new;
        }
    }

    @SuppressWarnings("null")
    @Override
    public ChatMessage generateChangedMsg() {
        m_changedMsg = Config.getInst().isRemoveColors ? removeStyle(getOrigMessage()) : getOrigMessage();

        if (!Config.getInst().isChangeGlobalLocal)
            return this;

        MutableComponent newMsg = Component.empty();
        final Component donatePart = getRank();
        final Component nickPart = Component.nullToEmpty("§f" + getSenderNick() + " ");
        final List<Component> siblings = m_changedMsg.getSiblings();
        final Style msgStyle = Style.EMPTY
            .withHoverEvent(new HoverEvent.ShowText(buildHoverText()))
            .withClickEvent(getClickEvent());
        
        newMsg.append(m_isGlobal ? "§a[ɢ] " : "§e[ʟ] ");
        newMsg.append(donatePart).append(" ").append(nickPart);
        newMsg.setStyle(msgStyle);
        newMsg.append(Component.nullToEmpty("§8→ §r").copy().setStyle(Style.EMPTY));
        
        for (int i = 1; i < siblings.size(); ++i) {
            newMsg.append(siblings.get(i));
        }
        m_changedMsg = newMsg;
        return this;
    }

    private Component getRank() {
        final HoverEvent hEvent = m_message.getSiblings().getFirst().getStyle().getHoverEvent();
        assert hEvent != null && hEvent.action() == HoverEvent.Action.SHOW_TEXT;
        final HoverEvent.ShowText showText = (HoverEvent.ShowText) hEvent;
        @SuppressWarnings("null")
        final Component hText = showText.value();
        final String text = hText.getString();
        final String first = text.split("\\s+", 2)[0];
        return Component.nullToEmpty(first).copy().setStyle(hText.getStyle());
    }

    @SuppressWarnings("null")
    private Component buildHoverText() {
        final HoverEvent hEvent = m_message.getSiblings().getFirst().getStyle().getHoverEvent();
        assert hEvent.action() == HoverEvent.Action.SHOW_TEXT;
        final HoverEvent.ShowText showText = (HoverEvent.ShowText) hEvent;
        MutableComponent hoverText = showText.value().copy();
        hoverText.append("\n§8Оригинальный клан/префикс/ник/суффикс:\n");
        hoverText.append(m_message.getSiblings().getFirst());
        return hoverText;
    }

    private ClickEvent getClickEvent() {
        return m_message.getSiblings().getFirst().getStyle().getClickEvent();
    }

    @SuppressWarnings("null")
    @Override
    public Component removeStyle(Component message) {
        List<Component> siblings = message.getSiblings();
        if (siblings.isEmpty()) return message;

        MutableComponent result = Component.empty();
        result.append(siblings.get(0));
        result.append(siblings.get(1).getString());
        return result;
    }
}
