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
import org.ayoree.chatimprove.dexland.Config;
import org.ayoree.chatimprover.api.ChatMessage;
import org.ayoree.chatimprover.api.ChatMessageWithSender;

import com.google.auto.service.AutoService;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

// /msg messages
public class PMToMeChatMessage extends ChatMessageWithSender implements IChatMessageWithoutStyle {
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
                final List<Text> siblings = message.getSiblings();
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
    public ChatMessage generateChangedMsg() {
        m_changedMsg = Config.getInst().isRemoveColors ? removeStyle(getOrigMessage()) : getOrigMessage();
        return this;
    }

    @Override
    public Text removeStyle(Text message) {
        if (message.getString().startsWith("[*] [я -> "))
            return message; // pohuy

        List<Text> siblings = message.getSiblings();
        final MutableText newText = Text.empty();

        newText.append(message.copyContentOnly());
        newText.append(siblings.get(0));
        newText.append(siblings.get(1));
        
        Text part = siblings.get(2);
        newText.append(part.copyContentOnly());
        part = part.getSiblings().get(0);
        newText.append(part.copyContentOnly());

        siblings = part.getSiblings();
        for (int i = 0; i < siblings.size(); ++i) {
            if (i != siblings.size() - 1)
                newText.append(siblings.get(i));
            else {
                part = part.getSiblings().get(i);
                newText.append(part.copyContentOnly());
                part = part.getSiblings().get(0);
                newText.append(part.copyContentOnly());
                part = part.getSiblings().get(0);
                newText.append(part.copyContentOnly());
                for (final Text text : part.getSiblings())
                    newText.append(text.getString());
            }
        }

        final Style senderStyle = Style.EMPTY
            .withClickEvent(new ClickEvent.SuggestCommand("/m " + getSenderNick() + " "))
            .withHoverEvent(new HoverEvent.ShowText(Text.of("§7Нажмите чтобы написать §f§n" + getSenderNick())));
            
        newText.setStyle(senderStyle);
        
        return newText;
    }
}
