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

package org.ayoree.chatimprove.dexland.messages.punishments.lists;

import java.util.List;

import org.ayoree.chatimprove.dexland.messages.punishments.AnyPunishChatMessage;

import net.minecraft.text.Text;

public abstract class AnyPunishlistChatMessage extends AnyPunishChatMessage {
    protected static final int s_senderIndex = 3;
    protected static final int s_receiverIndex = 1;
    
    protected String m_server;

    protected AnyPunishlistChatMessage(Text message) {
        super(message);
        final List<Text> siblings = m_message.getSiblings();
        for (int i = siblings.size() - 1; i >= 0; --i) {
            final Text sibling = siblings.get(i);
            final String str = sibling.getString();
            if (str.startsWith("(#surv-")) {
                m_server = str.substring(2, str.lastIndexOf(')'));
                break;
            }
        }
        m_receiverNick = siblings.get(s_receiverIndex).getString().trim();
        m_senderNick = siblings.get(s_senderIndex).getString().trim();
    }
}
