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
import org.ayoree.chatimprove.dexland.messages.punishments.PunishChatMessage;
import org.ayoree.chatimprover.api.ChatMessage;

import com.google.auto.service.AutoService;

import net.minecraft.text.Text;

public class ReasonChatMessage extends ChatMessage {
    protected final String m_reason;

    public ReasonChatMessage(Text message) {
        super(message);
        final List<Text> siblings = message.getSiblings();
        int startIndex = 1;
        for (int i = startIndex; i < siblings.size(); ++i) {
            if (siblings.get(i).getString().equals("Причина: ")) {
                startIndex = i + 1;
                break;
            }
        }
        String tmpReason = "";
        for (int i = startIndex; i < siblings.size(); ++i) {
            tmpReason = tmpReason.concat(siblings.get(i).getString());
        }
        m_reason = tmpReason.trim();

        // We receive `ReasonChatMessage` only after `PunishChatMessage`
        PunishChatMessage.getLastMessage().setReason(getReasonStr());
    }

    @AutoService(Provider.class)
    public static class ProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return message -> {
                final List<Text> siblings = message.getSiblings();
                return siblings.size() >= 3 && (
                    (
                        siblings.get(0).getString().equals(" » ") &&
                        siblings.get(1).getString().equals("Причина: ")
                    ) ||
                    (
                        siblings.get(0).getString().equals(" ") &&
                        siblings.get(1).getString().equals("» ") &&
                        siblings.get(2).getString().equals("Причина: ")
                    ));
            };
        }
        @Override
        public Function<Text, ChatMessage> creator() {
            return ReasonChatMessage::new;
        }
    }

    public String getReasonStr() {
        return m_reason;
    }
}
