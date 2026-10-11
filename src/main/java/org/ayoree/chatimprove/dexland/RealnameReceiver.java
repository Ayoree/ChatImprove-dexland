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

import static org.ayoree.chatimprove.dexland.ChatImprove_dexland.LOGGER;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

public class RealnameReceiver {
    private static RealnameReceiver instance = null;

    private boolean isCommandSend = false;
    private boolean isReadingNicknames = false;

    private Map<String, String> nicknames = new HashMap<>();

    public static RealnameReceiver getInst() {
        return instance;
    }

    public static void init() {
        instance = new RealnameReceiver();
    }

    public boolean isNeedToBlock() {
        return isReadingNicknames;
    }

    public void receivedRealnameMessage() {
        if (isCommandSend && !isReadingNicknames) {
            isReadingNicknames = true;
        }
    }

    public void receivedOtherMessage() {
        if (isReadingNicknames) {
            isReadingNicknames = false;
            isCommandSend = false;
        }
    }

    public String getRealName(String changedNickname) {
        LOGGER.info("getRealName: %s".formatted(changedNickname));
        if (nicknames.containsKey(changedNickname)) {
            return nicknames.get(changedNickname);
        }

        requestRealnames(Minecraft.getInstance().getConnection());
        return changedNickname;
    }

    public void addPair(String changedNickname, String realNickname) {
        nicknames.put(changedNickname, realNickname);
        LOGGER.info("AddPair: `%s`, `%s`".formatted(changedNickname, realNickname));
    }
    
    private void requestRealnames(ClientPacketListener clientPacketListener) {
        LOGGER.info("requestRealnames");
        if (!isCommandSend) {
            LOGGER.info("sendCommand(\"realname ~\");");
            clientPacketListener.sendCommand("realname ~");
            isCommandSend = true;
        }
    }
}
