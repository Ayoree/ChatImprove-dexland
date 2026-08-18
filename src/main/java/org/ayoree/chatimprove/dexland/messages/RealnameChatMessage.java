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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import org.ayoree.chatimprove.dexland.AddonInformerImpl;
import org.ayoree.chatimprove.dexland.RealnameReceiver;
import org.ayoree.chatimprover.api.ChatMessage;
import org.ayoree.chatimprover.api.ChatMessageWithReceiverAndSender;

import com.google.auto.service.AutoService;

public class RealnameChatMessage extends ChatMessageWithReceiverAndSender {
    public RealnameChatMessage(Component message) {
        super(message);

        Component usefulPart = message.getSiblings().get(2);
        final String lastPart = usefulPart.getSiblings().getLast().getString();
        
        final String fullPart = usefulPart.getString();
        final String changedNick = fullPart.substring(0, fullPart.length() - lastPart.length());
        setChangedNick(changedNick);
        setRealNick(lastPart.substring(lastPart.lastIndexOf(" ") + 1));

        RealnameReceiver.getInst().addPair(getChangedNickname(), getRealNickname());
    }

    @AutoService(Provider.class)
    public static class ProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Component> validator() {
            return message -> {
                final List<Component> siblings = message.getSiblings();
                if (siblings.size() != 3)
                    return false;

                final Component mainPart = siblings.get(2);
                return mainPart.plainCopy().getString().equals("~~") &&
                       mainPart.getSiblings().getLast().getString().startsWith(" является ");
            };
        }
        @Override
        public Function<Component, ChatMessage> creator() {
            return RealnameChatMessage::new;
        }
    }

    @Override
    public ChatMessage generateChangedMsg() {
        m_changedMsg = getOrigMessage();
        return this;
    }

    public void setChangedNick(String nickname) {
        setSenderNick(nickname);
    }

    public void setRealNick(String nickname) {
        setReceiverNick(nickname);
    }

    public String getChangedNickname() {
        return getSenderNick();
    }

    public String getRealNickname() {
        return getReceiverNick();
    }
}
