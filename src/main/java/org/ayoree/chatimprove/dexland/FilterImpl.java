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

import java.util.List;
import java.util.function.Predicate;

import org.ayoree.chatimprover.api.Filter;

import com.google.auto.service.AutoService;

import net.minecraft.text.Text;

public class FilterImpl extends Filter {
    @AutoService(Provider.class)
    public static class EmptyFilterProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return text -> text.getString().isBlank();
        }
    }

    @AutoService(Provider.class)
    public static class AdsFilterProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return text -> {
                if (!Config.getInst().isBlockAdsMessages)
                    return false;
                List<Text> siblings = text.getSiblings();
                return (siblings.size() == 14 &&
                    siblings.get(0).getString().equals(" ✸ ") &&
                    siblings.get(1).getString().equals("Хотите быть первым вкурсе ") &&
                    siblings.get(2).getString().equals("всех") &&
                    siblings.get(3).getString().equals(" новостей сервера?")
                ) || (
                    siblings.size() == 4 &&
                    siblings.get(0).getString().equals("| ") &&
                    siblings.get(1).getString().equals("Покупка донат-кейсов производится на сайте › ")
                ) || (
                    siblings.size() == 10 &&
                    siblings.get(0).getString().equals("[") &&
                    siblings.get(1).getString().equals("*") &&
                    siblings.get(2).getString().equals("] ") &&
                    siblings.get(3).getString().equals("Донат ") &&
                    siblings.get(4).getString().equals("[") &&
                    siblings.get(6).getString().equals("] ") &&
                    siblings.get(7).getString().equals("получает по ") &&
                    siblings.get(9).getString().equals("⛁")
                );
            };
        }
    }

    @AutoService(Provider.class)
    public static class JoinFilterProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return text -> {
                if (!Config.getInst().isBlockJoinMessages)
                    return false;
                List<Text> siblings = text.getSiblings();
                return (
                    siblings.size() >= 3 &&
                    siblings.get(0).getString().isBlank() &&
                    siblings.get(1).getString().equals("⇨ ")
                );
            };
        }
    }

    @AutoService(Provider.class)
    public static class CaseFilterProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return text -> {
                if (!Config.getInst().isBlockCaseMessages)
                    return false;
                List<Text> siblings = text.getSiblings();
                return (siblings.size() == 6 &&
                    siblings.get(0).getString().equals("| ") &&
                    siblings.get(1).getString().equals("Игрок ") &&
                    siblings.get(3).getString().equals(" выбил ") &&
                    siblings.get(5).getString().equals("из ежедневного кейса на /warp case")
                );
            };
        }
    }

    @AutoService(Provider.class)
    public static class PaymentFilterProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return text -> {
                if (!Config.getInst().isBlockPaymentMessages)
                    return false;
                List<Text> siblings = text.getSiblings();
                return (siblings.size() == 11 &&
                    siblings.get(0).getString().equals("[") &&
                    siblings.get(1).getString().equals("*") &&
                    siblings.get(2).getString().equals("] ") &&
                    siblings.get(3).getString().equals("Ты получил ") &&
                    siblings.get(4).getString().equals("+") &&
                    siblings.get(6).getString().equals("⛁ ") &&
                    siblings.get(7).getString().equals("за ") &&
                    siblings.get(9).getString().equals(" минут") &&
                    siblings.get(10).getString().equals(" игры.")
                );
            };
        }
    }

    @AutoService(Provider.class)
    public static class PVPLeaveFilterProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return text -> {
                if (!Config.getInst().isBlockPVPLeaveMessages)
                    return false;
                List<Text> siblings = text.getSiblings();
                return (siblings.size() == 7 &&
                    siblings.get(0).getString().equals("|") &&
                    siblings.get(1).getString().equals(" Игрок ") &&
                    siblings.get(3).getString().equals(" покинул игру во время ") &&
                    siblings.get(4).getString().equals("PvP") &&
                    siblings.get(5).getString().equals(" и ") &&
                    siblings.get(6).getString().equals("был убит")
                );
            };
        }
    }

    @AutoService(Provider.class)
    public static class SusPVPFilterProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return text -> {
                if (!Config.getInst().isBlockSusPVPMessages)
                    return false;
                List<Text> siblings = text.getSiblings();
                return (siblings.size() == 5 &&
                    siblings.get(0).getString().equals("АнтиЧит") &&
                    siblings.get(1).getString().equals(" ▸ ") &&
                    siblings.get(2).getString().equals("Игрок ") &&
                    siblings.get(4).getString().equals(" кикнут по подозрению в использовании читов.")
                );
            };
        }
    }

    @AutoService(Provider.class)
    public static class WonDuelFilterProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return text -> {
                if (!Config.getInst().isBlockWonDuelMessages)
                    return false;
                List<Text> siblings = text.getSiblings();
                return (siblings.size() == 11 &&
                    siblings.get(0).getString().equals("Дуэли ") &&
                    siblings.get(1).getString().equals("▸ ") &&
                    siblings.get(2).getString().equals("Игрок ") &&
                    siblings.get(4).getString().equals("победил ") &&
                    siblings.get(6).getString().equals("за ") &&
                    siblings.get(8).getString().equals("минут с набором ")
                );
            };
        }
    }

    @AutoService(Provider.class)
    public static class AutoMineFilterProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return text -> {
                if (!Config.getInst().isBlockAutoMineMessages)
                    return false;
                List<Text> siblings = text.getSiblings();
                return (siblings.size() == 6 &&
                    siblings.get(0).getString().equals("АвтоШахта") &&
                    siblings.get(1).getString().equals(" ") &&
                    siblings.get(2).getString().equals("» ") &&
                    siblings.get(3).getString().equals("Шахта обновлена") &&
                    siblings.get(4).getString().equals(", телепортироваться - ") &&
                    siblings.get(5).getString().equals("/warp mine")
                );
            };
        }
    }
}
