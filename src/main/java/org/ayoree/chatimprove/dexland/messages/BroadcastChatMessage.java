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
import org.ayoree.chatimprover.api.ChatMessageWithSender;

import com.google.auto.service.AutoService;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class BroadcastChatMessage extends ChatMessageWithSender {
    public BroadcastChatMessage(Text message) {
        super(message);
        final List<Text> siblings = m_message.getSiblings();
        setSenderNick(siblings.get(siblings.size() - 2).getString());
    }

    @AutoService(Provider.class)
    public static class ProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return message -> {
                final List<Text> siblings = message.getSiblings();
                return (
                    siblings.size() >= 6 &&
                    siblings.get(0).getString().equals("[") &&
                    siblings.get(1).getString().equals("Объявление") &&
                    siblings.get(2).getString().startsWith("] ") &&
                    siblings.get(siblings.size() - 3).getString().equals("(Пишет: ") &&
                    siblings.get(siblings.size() - 1).getString().equals(")")
                );
            };
        }
        @Override
        public Function<Text, ChatMessage> creator() {
            return BroadcastChatMessage::new;
        }
    }

    @Override
    public Text getChangedMessage() {
        MutableText newMsg = m_message.copy();
        final Style senderStyle = Style.EMPTY
            .withClickEvent(new ClickEvent.SuggestCommand("/m " + getSenderNick() + " "))
            .withHoverEvent(new HoverEvent.ShowText(Text.of("§7Нажмите чтобы написать §f§n" + getSenderNick())));

        final List<Text> siblings = newMsg.getSiblings();
        final int index = siblings.size() - 2;
        siblings.set(index, siblings.get(index).copy().setStyle(senderStyle));

        return addExtraStuff(newMsg);
    }
}
