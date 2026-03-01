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

import org.ayoree.chatimprove.dexland.AddonInformerImpl;
import org.ayoree.chatimprover.api.ChatMessage;
import org.ayoree.chatimprover.api.ChatMessageWithReceiverAndSender;

import com.google.auto.service.AutoService;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

// /msg messages
public class PMChatMessage extends ChatMessageWithReceiverAndSender {
    protected final Text m_textPart;
    static final int startNickIndex = 14;

     public PMChatMessage(Text message) {
        super(message);
        
        setSenderNick(getMessageStr().substring(startNickIndex, getMessageStr().indexOf(" -> ", 10)));
        int receiverStartIndex = startNickIndex + (getSenderNick().length()) + 4;
        setReceiverNick(getMessageStr().substring(receiverStartIndex, getMessageStr().indexOf("] ", receiverStartIndex)));
        m_textPart = Text.of(m_message.getString().substring(receiverStartIndex + getReceiverNick().length() + 2));
    }

    @AutoService(Provider.class)
    public static class ProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return message -> {
                return message.getString().startsWith("[*] [SS] [*] [");
            };
        }
        @Override
        public Function<Text, ChatMessage> creator() {
            return PMChatMessage::new;
        }
    }

    @Override
    public Text getChangedMessage() {
        MutableText newMsg = Text.empty();
        final Style senderStyle = Style.EMPTY
            .withClickEvent(new ClickEvent.SuggestCommand("/m " + getSenderNick()))
            .withHoverEvent(new HoverEvent.ShowText(Text.of("§7Нажмите чтобы написать §f§n" + getSenderNick())));
        final Style receiverStyle = Style.EMPTY
            .withClickEvent(new ClickEvent.SuggestCommand("/m " + getReceiverNick()))
            .withHoverEvent(new HoverEvent.ShowText(Text.of("§7Нажмите чтобы написать §f§n" + getReceiverNick())));
        newMsg.append(Text.of("§7[SS] §f[").copy().setStyle(Style.EMPTY));
        newMsg.append(Text.of("§a" + getSenderNick()).copy().setStyle(senderStyle));
        newMsg.append(Text.of("§7 -> ").copy().setStyle(Style.EMPTY));
        newMsg.append(Text.of("§a" + getReceiverNick()).copy().setStyle(receiverStyle));
        newMsg.append(Text.of("§f]§r ").copy().setStyle(Style.EMPTY));
        newMsg.append(m_textPart);
        return addExtraStuff(newMsg);
    }
}
