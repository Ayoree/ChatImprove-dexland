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

package org.ayoree.chatimprove.dexland;

import java.util.function.Predicate;

import org.ayoree.chatimprove.dexland.messages.RealnameChatMessage;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.network.chat.Component;
import org.ayoree.chatimprover.api.ChatImproverApi;

public class RealnameHandler {

    public static void init() {
        ClientReceiveMessageEvents.ALLOW_GAME.register(ChatImproverApi.ADDON_PHASE, RealnameHandler::onAllowMessage);
    }

     private static boolean onAllowMessage(final Component message, final boolean isOverlay) {
        if (isOverlay)
            return true;

        final Predicate<Component> realNameValidator = new RealnameChatMessage.ProviderImpl().validator();
        if (realNameValidator.test(message))
            RealnameReceiver.getInst().receivedRealnameMessage();
        else
            RealnameReceiver.getInst().receivedOtherMessage();
        final boolean isNeedBlock = RealnameReceiver.getInst().isNeedToBlock();
        if (isNeedBlock) {
            new RealnameChatMessage(message); // Need to create one instance to make RealnameReceiver work properly
        }
        return !isNeedBlock;
    }
}
