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
import org.ayoree.chatimprover.api.ChatMessageWithReceiverAndSender;
import org.ayoree.chatimprover.api.ChatMessageWithSender;

import com.google.auto.service.AutoService;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;

// /msg messages
public class PMToMeChatMessage extends ChatMessageWithSender {
     public PMToMeChatMessage(Text message) {
        super(message);
        
        ClickEvent abstractClickEvent = m_message.getStyle().getClickEvent();
        if (abstractClickEvent instanceof ClickEvent.SuggestCommand clickEvent) {
            setSenderNick(clickEvent.command().substring(3, clickEvent.command().length() - 1));
        }
    }

    @AutoService(Provider.class)
    public static class ProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return message -> {
                List<Text> siblings = message.getSiblings();
                return siblings.size() == 3 &&
                    siblings.get(0).getString().equals("*") &&
                    siblings.get(1).getString().equals("] ") &&
                    siblings.get(2).getString().startsWith("[");
            };
        }
        @Override
        public Function<Text, ChatMessage> creator() {
            return PMToMeChatMessage::new;
        }
    }

    @Override
    public Text getChangedMessage() {
        return addExtraStuff(getMessage());
    }
}
