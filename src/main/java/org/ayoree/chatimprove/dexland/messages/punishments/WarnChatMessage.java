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

public class WarnChatMessage extends PunishChatMessage {
    protected static final int s_senderIndex = 3;
    protected static final int s_receiverIndex = 1;

    public WarnChatMessage(Text message) {
        super(message);
        final List<Text> siblings = m_message.getSiblings();
        m_receiverNick = siblings.get(s_receiverIndex).getString().trim();
        m_senderNick = siblings.get(s_senderIndex).getString().trim();
    }

    @AutoService(Provider.class)
    public static class ProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return message -> {
                final List<Text> siblings = message.getSiblings();
                return (
                    siblings.size() == 4 &&
                    siblings.get(0).getString().equals(" » ") &&
                    siblings.get(2).getString().startsWith("получил предупреждение от ")
                );
            };
        }
        @Override
        public Function<Text, ChatMessage> creator() {
            return WarnChatMessage::new;
        }
    }

    @Override
    public Text getChangedMessage() {
        String banStr = Config.getInst().incorrectWarnMsg;
        banStr = banStr.replace("{NICKNAME}", m_senderNick).replace("{RECEIVER}", m_receiverNick);

        MutableText newMsg = m_message.copy();
        final List<Text> siblings = newMsg.getSiblings();
        final Style senderStyle = siblings.get(s_senderIndex).getStyle()
            .withClickEvent(new ClickEvent.SuggestCommand(banStr))
            .withHoverEvent(new HoverEvent.ShowText(Text.of(banStr.replace('&', '§'))));
        final Style receiverStyle = siblings.get(s_receiverIndex).getStyle()
            .withClickEvent(new ClickEvent.SuggestCommand("/unwarn " + m_receiverNick))
            .withHoverEvent(new HoverEvent.ShowText(Text.of("§7Нажмите чтобы снять предупреждение с §f§n" + m_receiverNick)));
        
        siblings.set(s_receiverIndex, siblings.get(s_receiverIndex).copy().setStyle(receiverStyle));
        siblings.set(s_senderIndex, siblings.get(s_senderIndex).copy().setStyle(senderStyle));

        return newMsg;
    }
}
