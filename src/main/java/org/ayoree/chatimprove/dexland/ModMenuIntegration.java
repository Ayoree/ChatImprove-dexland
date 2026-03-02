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

package org.ayoree.chatimprove.dexland;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.createBuilder()
            .title(Text.empty())
            .category(ConfigCategory.createBuilder()
                .name(Text.literal("Кастомизация"))
                .tooltip(Text.literal("Ты нашёл печеньку! 🍪"))
                .group(OptionGroup.createBuilder()
                    .name(Text.literal("Команды наказаний"))
                    .description(OptionDescription.of(Text.literal("Эти команды будут предлагаться при клике на ники в сообщениях о наказаниях!\n\nДоступные плейсхолдеры:\n\t1) {NICKNAME} - ник игрока, который выдал наказание;\n\t2) {RECEIVER} - ник игрока, который получил наказание;")))
                    .option(Option.<String>createBuilder()
                        .name(Text.literal("Некорректный бан"))
                        .description(OptionDescription.of(Text.literal("Команда, которая предлагается, если нажать на ник того, кто выдал бан")))
                        .binding("/ban {NICKNAME} 8h 4.9 [Некорректный бан игрока `{RECEIVER}`]", () -> Config.getInst().incorrectBanMsg, newVal -> Config.getInst().incorrectBanMsg = newVal)
                        .controller(StringControllerBuilder::create)
                        .build())
                    .option(Option.<String>createBuilder()
                        .name(Text.literal("Некорректный мут"))
                        .description(OptionDescription.of(Text.literal("Команда, которая предлагается, если нажать на ник того, кто выдал мут")))
                        .binding("/ban {NICKNAME} 1h 4.21 [Некорректный мут игрока `{RECEIVER}`]", () -> Config.getInst().incorrectMuteMsg, newVal -> Config.getInst().incorrectMuteMsg = newVal)
                        .controller(StringControllerBuilder::create)
                        .build())
                    .option(Option.<String>createBuilder()
                        .name(Text.literal("Некорректный варн"))
                        .description(OptionDescription.of(Text.literal("Команда, которая предлагается, если нажать на ник того, кто выдал варн")))
                        .binding("/ban {NICKNAME} 1h 4.21 [Некорректный варн игрока `{RECEIVER}`]", () -> Config.getInst().incorrectWarnMsg, newVal -> Config.getInst().incorrectWarnMsg = newVal)
                        .controller(StringControllerBuilder::create)
                        .build())
                    .option(Option.<String>createBuilder()
                        .name(Text.literal("Некорректный кик"))
                        .description(OptionDescription.of(Text.literal("Команда, которая предлагается, если нажать на ник того, кто выдал кик")))
                        .binding("/ban {NICKNAME} 1h 4.21 [Некорректный кик игрока `{RECEIVER}`]", () -> Config.getInst().incorrectKickMsg, newVal -> Config.getInst().incorrectKickMsg = newVal)
                        .controller(StringControllerBuilder::create)
                        .build())
                    .option(Option.<String>createBuilder()
                        .name(Text.literal("Некорректный разбан"))
                        .description(OptionDescription.of(Text.literal("Команда, которая предлагается, если нажать на ник того, кто снял бан")))
                        .binding("/ban {NICKNAME} 8h 4.20 [Некорректный разбан игрока `{RECEIVER}`]", () -> Config.getInst().incorrectUnbanMsg, newVal -> Config.getInst().incorrectUnbanMsg = newVal)
                        .controller(StringControllerBuilder::create)
                        .build())
                    .option(Option.<String>createBuilder()
                        .name(Text.literal("Некорректный размут"))
                        .description(OptionDescription.of(Text.literal("Команда, которая предлагается, если нажать на ник того, кто снял мут")))
                        .binding("/ban {NICKNAME} 8h 4.20 [Некорректный размут игрока `{RECEIVER}`]", () -> Config.getInst().incorrectUnmuteMsg, newVal -> Config.getInst().incorrectUnmuteMsg = newVal)
                        .controller(StringControllerBuilder::create)
                        .build())
                    .option(Option.<String>createBuilder()
                        .name(Text.literal("Некорректный разварн"))
                        .description(OptionDescription.of(Text.literal("Команда, которая предлагается, если нажать на ник того, кто снял варн")))
                        .binding("/ban {NICKNAME} 1h 4.20 [Некорректное снятие варна с игрока `{RECEIVER}`]", () -> Config.getInst().incorrectUnwarnMsg, newVal -> Config.getInst().incorrectUnwarnMsg = newVal)
                        .controller(StringControllerBuilder::create)
                        .build())
                    .build())
                .group(OptionGroup.createBuilder()
                    .name(Text.literal("Проверка блоков через /co i"))
                    .description(OptionDescription.of(Text.literal("Эти команды будут предлагаться при клике на ники в сообщениях, показанных при использовании `/co i`!\n\nДоступные плейсхолдеры:\n\t1) {NICKNAME} - ник игрока, который совершил изменение;")))
                    .option(Option.<String>createBuilder()
                        .name(Text.literal("/co i"))
                        .description(OptionDescription.of(Text.literal("Команда, которая предлагается, если нажать на ник того, кто совершил изменение")))
                        .binding("/ban {NICKNAME} ", () -> Config.getInst().coreProtectCommand, newVal -> Config.getInst().coreProtectCommand = newVal)
                        .controller(StringControllerBuilder::create)
                        .build())
                    .build())
                .group(OptionGroup.createBuilder()
                    .name(Text.literal("Спам-Фильтр"))
                    .description(OptionDescription.of(Text.literal("Фильтровать бесполезные сообщения в чате")))
                    .option(Option.<Boolean>createBuilder()
                        .name(Text.literal("Реклама"))
                        .description(OptionDescription.of(Text.literal("Фильтровать рекламные сообщения.\n\nНапример:\n§a§l[*]§f Донат§c§l [Владелец]§f получает по§a§l 10.000§e⛁")))
                        .binding(true, () -> Config.getInst().isBlockAdsMessages, newVal -> Config.getInst().isBlockAdsMessages = newVal)
                        .controller(BooleanControllerBuilder::create)
                        .build())
                    .option(Option.<Boolean>createBuilder()
                        .name(Text.literal("Ежедневные кейсы"))
                        .description(OptionDescription.of(Text.literal("Фильтровать сообщения об открытии ежедневных кейсов.\n\nНапример:\n§6§l|§7 Игрок§6 Pupa§f выбил§e Незеритовый меч§7 из ежедневного кейса на /warp case")))
                        .binding(true, () -> Config.getInst().isBlockCaseMessages, newVal -> Config.getInst().isBlockCaseMessages = newVal)
                        .controller(BooleanControllerBuilder::create)
                        .build())
                    .option(Option.<Boolean>createBuilder()
                        .name(Text.literal("Приветствия"))
                        .description(OptionDescription.of(Text.literal("Фильтровать приветственные сообщения.\n\nНапример:\n§c⇨ Добро пожаловать на сервер§l БлаБлаБла")))
                        .binding(true, () -> Config.getInst().isBlockJoinMessages, newVal -> Config.getInst().isBlockJoinMessages = newVal)
                        .controller(BooleanControllerBuilder::create)
                        .build())
                    .option(Option.<Boolean>createBuilder()
                        .name(Text.literal("Выплаты по времени"))
                        .description(OptionDescription.of(Text.literal("Фильтровать сообщения об получении монет за проведенное время в игре.\n\nНапример:\n§a§l[*]§f Ты получил§a§l +10000§e⛁§f за§a§l 5§a минут§f игры.")))
                        .binding(true, () -> Config.getInst().isBlockPaymentMessages, newVal -> Config.getInst().isBlockPaymentMessages = newVal)
                        .controller(BooleanControllerBuilder::create)
                        .build())
                    .option(Option.<Boolean>createBuilder()
                        .name(Text.literal("Автошахта"))
                        .description(OptionDescription.of(Text.literal("Фильтровать сообщения об обновлении автошахты.\n\nНапример:\n§a§lАвтоШахта§8 »§f Шахта обновлена, телепортироваться -§e /warp mine")))
                        .binding(true, () -> Config.getInst().isBlockAutoMineMessages, newVal -> Config.getInst().isBlockAutoMineMessages = newVal)
                        .controller(BooleanControllerBuilder::create)
                        .build())
                    .option(Option.<Boolean>createBuilder()
                        .name(Text.literal("Победы в дуэлях"))
                        .description(OptionDescription.of(Text.literal("Фильтровать сообщения об победах в дуэлях.\n\nНапример:\n§6§lДуэли§8 ▸§f Игрок§a Pupa§f победил§a Lupa§f за§a 00:07§f минут с набором§d Кожанка PvP§7 (Скрыть: /duel msg)")))
                        .binding(true, () -> Config.getInst().isBlockWonDuelMessages, newVal -> Config.getInst().isBlockWonDuelMessages = newVal)
                        .controller(BooleanControllerBuilder::create)
                        .build())
                    .option(Option.<Boolean>createBuilder()
                        .name(Text.literal("Выход во время PVP"))
                        .description(OptionDescription.of(Text.literal("Фильтровать сообщения об выходе из игры во время PVP.\n\nНапример:\n§6§l|§f Игрок§6 Pupa§f покинул игру во время§6 PvP§f и§c был убит")))
                        .binding(true, () -> Config.getInst().isBlockPVPLeaveMessages, newVal -> Config.getInst().isBlockPVPLeaveMessages = newVal)
                        .controller(BooleanControllerBuilder::create)
                        .build())
                    .option(Option.<Boolean>createBuilder()
                        .name(Text.literal("Подозрения на читы"))
                        .description(OptionDescription.of(Text.literal("Фильтровать сообщения об киках античита.\n\nНапример:\n§6АнтиЧит§8 ▸§f Игрок§c Pupa§f кикнут по подозрению в использовании читов.")))
                        .binding(true, () -> Config.getInst().isBlockSusPVPMessages, newVal -> Config.getInst().isBlockSusPVPMessages = newVal)
                        .controller(BooleanControllerBuilder::create)
                        .build())
                    .build())
                .group(OptionGroup.createBuilder()
                    .name(Text.literal("Прочее"))
                    .description(OptionDescription.of(Text.literal("Другое")))
                    .option(Option.<Boolean>createBuilder()
                        .name(Text.literal("Показывать только донат и ник в Глобальном и Локальном чатах"))
                        .description(OptionDescription.of(Text.literal("Убирает клан, префикс, суффикс, и меняет измененный ник на настоящий ник игрока в локальном и глобальном чатах")))
                        .binding(true, () -> Config.getInst().isChangeGlobalLocal, newVal -> Config.getInst().isChangeGlobalLocal = newVal)
                        .controller(BooleanControllerBuilder::create)
                        .build())
                    .option(Option.<Boolean>createBuilder()
                        .name(Text.literal("Авто определение сервера"))
                        .description(OptionDescription.of(Text.literal("Автоматически добавлять `server:surv-x` в конец команды, если есть возможность определить сервер")))
                        .binding(false, () -> Config.getInst().isAutoServerSuffix, newVal -> Config.getInst().isAutoServerSuffix = newVal)
                        .controller(BooleanControllerBuilder::create)
                        .build())
                    .build())
                .build())
            .save(Config::save)
            .build()
            .generateScreen(parentScreen);
    }
}