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

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ayoree.chatimprove.dexland.AddonInformerImpl;
import org.ayoree.chatimprover.api.ChatMessage;

import com.google.auto.service.AutoService;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class SayChatMessage extends ChatMessage {
    final String m_nick;
    static private Pattern m_pattern = Pattern.compile("\\[([^\\s]{3,})\\]\\s.*");

    public SayChatMessage(Text message) {
        super(message);
        final String msgStr = m_message.getString();
        m_nick = msgStr.substring(1, msgStr.indexOf("] "));
    }

    @AutoService(Provider.class)
    public static class ProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return message -> {
                Matcher matcher = m_pattern.matcher(message.getString());
                return matcher.matches();
            };
        }
        @Override
        public Function<Text, ChatMessage> creator() {
            return SayChatMessage::new;
        }
    }

    @Override
    public Text getChangedMessage() {
        final Style senderStyle = m_message.getStyle()
            .withClickEvent(new ClickEvent.SuggestCommand("/m " + m_nick + " "))
            .withHoverEvent(new HoverEvent.ShowText(Text.of("§7Нажмите чтобы написать §f§n" + m_nick)));
            
        MutableText newMsg = m_message.copy().setStyle(senderStyle);
        return newMsg;
    }
}
