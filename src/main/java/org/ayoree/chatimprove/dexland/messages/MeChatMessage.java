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

import org.ayoree.chatimprove.dexland.AddonInformerImpl;
import org.ayoree.chatimprover.api.ChatMessage;

import com.google.auto.service.AutoService;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class MeChatMessage extends ChatMessage {
    final String m_nick;
    static protected final int s_nickIndex = 1;

    public MeChatMessage(Text message) {
        super(message);
        final List<Text> siblings = m_message.getSiblings();
        final String tmpStr = siblings.get(1).getString().trim();
        m_nick = tmpStr.substring(0, tmpStr.indexOf(' '));
    }

    @AutoService(Provider.class)
    public static class ProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return message -> {
                final List<Text> siblings = message.getSiblings();
                return (
                    siblings.size() == 2 &&
                    siblings.get(0).getString().equals("*") &&
                    siblings.get(1).getString().startsWith(" ")
                );
            };
        }
        @Override
        public Function<Text, ChatMessage> creator() {
            return MeChatMessage::new;
        }
    }

    @Override
    public Text getChangedMessage() {
        MutableText newMsg = m_message.copy();
        final List<Text> siblings = newMsg.getSiblings();
        final Style senderStyle = siblings.get(s_nickIndex).getStyle()
            .withClickEvent(new ClickEvent.SuggestCommand("/m " + m_nick + " "))
            .withHoverEvent(new HoverEvent.ShowText(Text.of("§7Нажмите чтобы написать §f§n" + m_nick)));

        siblings.set(s_nickIndex, siblings.get(s_nickIndex).copy().setStyle(senderStyle));
        return newMsg;
    }
}
