package org.ayoree.chatimprove.dexland;

import java.util.HashMap;
import java.util.Map;

import org.ayoree.chatimprove.dexland.messages.RealnameChatMessage;

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

    public void receivedRealnameMessage(RealnameChatMessage message) {
        if (isCommandSend && !isReadingNicknames) {
            isReadingNicknames = true;

            // TODO: Complete logic
        }
    }

    public void receivedOtherMessage() {
        if (isReadingNicknames) {
            isReadingNicknames = false;
            isCommandSend = false;
        }
    }

    public String getRealName(String changedNickname) {
        if (nicknames.containsKey(changedNickname)) {
            return nicknames.get(changedNickname);
        }

        sendCommand(Minecraft.getInstance().getConnection());
        return changedNickname;
    }

    public void addPair(String changedNickname, String realNickname) {
        nicknames.put(changedNickname, realNickname);
    }
    
    private void sendCommand(ClientPacketListener clientPacketListener) {
        if (!isCommandSend) {
            clientPacketListener.sendCommand("realname ~");
            isCommandSend = true;
        }
    }
}
