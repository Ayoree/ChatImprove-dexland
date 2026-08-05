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

package org.ayoree.chatimprove.dexland.messages.coreprotect;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.ayoree.chatimprove.dexland.Config;
import org.ayoree.chatimprove.dexland.AddonInformerImpl;
import org.ayoree.chatimprover.api.ChatMessage;
import org.ayoree.chatimprover.api.ChatMessageWithSender;

import com.google.auto.service.AutoService;

public class CoreProtectMessage extends ChatMessageWithSender {
    protected static final int s_nickIndex = 2;

    public CoreProtectMessage(Component message) {
        super(message);
        final List<Component> siblings = message.getSiblings();
        final String tempStr = siblings.get(s_nickIndex).getString();
        setSenderNick(tempStr.substring(0, tempStr.indexOf(" ") - 2));
    }

    @AutoService(Provider.class)
    public static class ProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Component> validator() {
            return message -> {
                final List<Component> siblings = message.getSiblings();
                return siblings.size() == 4 && siblings.get(0).getSiblings().size() == 1;
            };
        }
        @Override
        public Function<Component, ChatMessage> creator() {
            return CoreProtectMessage::new;
        }
    }

    @Override
    public ChatMessage generateChangedMsg() {
        String banStr = Config.getInst().coreProtectCommand;
        banStr = banStr.replace("{NICKNAME}", getSenderNick());

        MutableComponent newMsg = m_message.copy();
        final List<Component> siblings = newMsg.getSiblings();
        final Style nickStyle = siblings.get(s_nickIndex).getStyle()
            .withClickEvent(new ClickEvent.SuggestCommand(banStr))
            .withHoverEvent(new HoverEvent.ShowText(Component.nullToEmpty(banStr.replace('&', '§'))));
        siblings.set(s_nickIndex, siblings.get(s_nickIndex).copy().setStyle(nickStyle));

        m_changedMsg = newMsg;
        return this;
    }
}
