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

package org.ayoree.chatimprove.dexland.messages.punishments;

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

public class UnwarnChatMessage extends UnPunishChatMessage {
    protected static final int s_senderIndex = 2;
    protected static final int s_receiverIndex = 4;
    protected final boolean m_isHidden;

    public UnwarnChatMessage(Text message) {
        super(message);
        final List<Text> siblings = message.getSiblings();
        m_isHidden = siblings.size() == 6;
        final int start = m_isHidden ? 1 : 0;
        setSenderNick(siblings.get(start + 2).getString().trim());
        setReceiverNick(siblings.get(start + 4).getString().trim());
    }

    @AutoService(Provider.class)
    public static class ProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return message -> {
                final List<Text> siblings = message.getSiblings();
                int start = 0;
                if (siblings.size() == 6)
                    start = 1;
                return (
                    (siblings.size() == 5 || siblings.size() == 6) &&
                    siblings.get(start).getString().equals("** ") &&
                    siblings.get(start + 3).getString().equals("размутил игрока ")
                );
            };
        }
        @Override
        public Function<Text, ChatMessage> creator() {
            return UnwarnChatMessage::new;
        }
    }

    @Override
    public ChatMessage generateChangedMsg() {
        String banStr = Config.getInst().incorrectUnwarnMsg;
        banStr = banStr.replace("{NICKNAME}", getSenderNick()).replace("{RECEIVER}", getReceiverNick());

        MutableText newMsg = m_message.copy();
        final List<Text> siblings = newMsg.getSiblings();
        final int start = m_isHidden ? 1 : 0;
        final Style senderStyle = siblings.get(start + s_senderIndex).getStyle()
            .withClickEvent(new ClickEvent.SuggestCommand(banStr))
            .withHoverEvent(new HoverEvent.ShowText(Text.of(banStr.replace('&', '§'))));
        final Style receiverStyle = siblings.get(start + s_receiverIndex).getStyle()
            .withClickEvent(new ClickEvent.SuggestCommand("/warn " + getReceiverNick()))
            .withHoverEvent(new HoverEvent.ShowText(Text.of("§7Нажмите чтобы переварнить §f§n" + getReceiverNick())));
        
        siblings.set(start + s_senderIndex, siblings.get(start + s_senderIndex).copy().setStyle(senderStyle));
        siblings.set(start + s_receiverIndex, siblings.get(start + s_receiverIndex).copy().setStyle(receiverStyle));

        m_changedMsg = newMsg;
        return this;
    }
}
