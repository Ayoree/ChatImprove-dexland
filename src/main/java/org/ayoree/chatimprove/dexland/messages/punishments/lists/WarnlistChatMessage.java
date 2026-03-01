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

 package org.ayoree.chatimprove.dexland.messages.punishments.lists;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.ayoree.chatimprove.dexland.Config;
import org.ayoree.chatimprove.dexland.AddonInformerImpl;
import org.ayoree.chatimprover.api.ChatMessage;

import com.google.auto.service.AutoService;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class WarnlistChatMessage extends PunishlistChatMessage {
    public WarnlistChatMessage(Text message) {
        super(message);
    }

    @AutoService(Provider.class)
    public static class ProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return message -> {
                final List<Text> siblings = message.getSiblings();
                return (
                    siblings.size() >= 15 &&
                    siblings.get(0).getString().equals("- ") &&
                    siblings.get(2).getString().startsWith("предупрежден ") &&
                    siblings.get(4).getString().equals("в ") &&
                    siblings.get(6).getString().equals("на ") &&
                    siblings.get(8).getString().equals("по причине: \'")
                );
            };
        }
        @Override
        public Function<Text, ChatMessage> creator() {
            return WarnlistChatMessage::new;
        }
    }

    @Override
    public Text getChangedMessage() {
        String banStr = Config.getInst().incorrectWarnMsg;
        String unwarnStr = "/unwarn " + getReceiverNick();
        banStr = banStr.replace("{NICKNAME}", getSenderNick()).replace("{RECEIVER}", getReceiverNick());
        if (Config.getInst().isAutoServerSuffix) {
            banStr = banStr.concat(" server:" + m_server);
            unwarnStr = unwarnStr.concat(" server:" + m_server);
        }

        MutableText newMsg = m_message.copy();
        final List<Text> siblings = newMsg.getSiblings();
        final Style senderStyle = siblings.get(s_senderIndex).getStyle()
            .withClickEvent(new ClickEvent.SuggestCommand(banStr))
            .withHoverEvent(new HoverEvent.ShowText(Text.of(banStr.replace('&', '§'))));
        final Style receiverStyle = siblings.get(s_receiverIndex).getStyle()
            .withClickEvent(new ClickEvent.SuggestCommand(unwarnStr))
            .withHoverEvent(new HoverEvent.ShowText(Text.of("§7Нажмите чтобы разварнить §f§n" + getReceiverNick())));
        
        siblings.set(s_receiverIndex, siblings.get(s_receiverIndex).copy().setStyle(receiverStyle));
        siblings.set(s_senderIndex, siblings.get(s_senderIndex).copy().setStyle(senderStyle));

        return addExtraStuff(newMsg);
    }
}
