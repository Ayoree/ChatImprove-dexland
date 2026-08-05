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
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.ayoree.chatimprove.dexland.AddonInformerImpl;
import org.ayoree.chatimprove.dexland.Config;
import org.ayoree.chatimprover.api.ChatMessage;
import org.ayoree.chatimprover.api.ChatMessageWithSender;

import com.google.auto.service.AutoService;

public class MeChatMessage extends ChatMessageWithSender implements IChatMessageWithoutStyle {
    static protected final int s_nickIndex = 1;

    public MeChatMessage(Component message) {
        super(message);
        final List<Component> siblings = m_message.getSiblings();
        final String tmpStr = siblings.get(1).getString().trim();
        setSenderNick(tmpStr.substring(0, tmpStr.indexOf(' ')));
    }

    @AutoService(Provider.class)
    public static class ProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Component> validator() {
            return message -> {
                final List<Component> siblings = message.getSiblings();
                return (
                    siblings.size() == 2 &&
                    siblings.get(0).getString().equals("*") &&
                    siblings.get(1).getString().startsWith(" ")
                );
            };
        }
        @Override
        public Function<Component, ChatMessage> creator() {
            return MeChatMessage::new;
        }
    }

    @Override
    public ChatMessage generateChangedMsg() {
        m_changedMsg = Config.getInst().isRemoveColors ? removeStyle(getOrigMessage()) : getOrigMessage();

        MutableComponent newMsg = m_changedMsg.copy();
        final List<Component> siblings = newMsg.getSiblings();
        final Style senderStyle = siblings.get(s_nickIndex).getStyle()
            .withClickEvent(new ClickEvent.SuggestCommand("/m " + getSenderNick() + " "))
            .withHoverEvent(new HoverEvent.ShowText(Component.nullToEmpty("§7Нажмите чтобы написать §f§n" + getSenderNick())));

        siblings.set(s_nickIndex, siblings.get(s_nickIndex).copy().setStyle(senderStyle));
        m_changedMsg = newMsg;
        return this;
    }

    @Override
    public Component removeStyle(Component message) {
        List<Component> siblings = message.getSiblings();
        final MutableComponent newText = Component.empty();

        Component part = siblings.get(0);
        newText.append(part);
        newText.append(siblings.get(1).plainCopy());
        siblings = siblings.get(1).getSiblings();
        
        for (int i = 0; i < siblings.size(); ++i) {
            if (i != siblings.size() - 1)
                newText.append(siblings.get(i));
            else {
                newText.append(siblings.get(i).getString());
            }
        }

        return newText;
    }
}
