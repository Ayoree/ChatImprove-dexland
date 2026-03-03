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

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import static org.ayoree.chatimprove.dexland.ChatImprove_dexland.MOD_ID;
import static org.ayoree.chatimprove.dexland.ChatImprove_dexland.LOGGER;

public class Config {
    @SerialEntry
    public String incorrectBanMsg = "/ban {NICKNAME} 8h 4.9 [Некорректный бан игрока `{RECEIVER}`]";
    @SerialEntry
    public String incorrectMuteMsg = "/ban {NICKNAME} 1h 4.21 [Некорректный мут игрока `{RECEIVER}`]";
    @SerialEntry
    public String incorrectWarnMsg = "/ban {NICKNAME} 1h 4.21 [Некорректный варн игрока `{RECEIVER}`]";
    @SerialEntry
    public String incorrectKickMsg = "/ban {NICKNAME} 1h 4.21 [Некорректный кик игрока `{RECEIVER}`]";
    @SerialEntry
    public String incorrectJailMsg = "/ban {NICKNAME} 1h 4.21 [Некорректный jail игрока `{RECEIVER}`]";
    @SerialEntry
    public String incorrectUnbanMsg = "/ban {NICKNAME} 8h 4.20 [Некорректный разбан игрока `{RECEIVER}`]";
    @SerialEntry
    public String incorrectUnmuteMsg = "/ban {NICKNAME} 8h 4.20 [Некорректный размут игрока `{RECEIVER}`]";
    @SerialEntry
    public String incorrectUnwarnMsg = "/ban {NICKNAME} 1h 4.20 [Некорректное снятие варна с игрока `{RECEIVER}`]";
    @SerialEntry
    public String incorrectUnjailMsg = "/ban {NICKNAME} 1h 4.20 [Некорректное освобождение игрока `{RECEIVER}`]";
    // Used when using `/co i`
    @SerialEntry
    public String coreProtectCommand = "/ban {NICKNAME} ";
    // Automatically add server:surv-x to the end of the command if possible to determine the server
    @SerialEntry
    public boolean isChangeGlobalLocal = true;
    @SerialEntry
    public boolean isAutoServerSuffix = false;
    @SerialEntry
    public boolean isBlockAdsMessages = true;
    @SerialEntry
    public boolean isBlockJoinMessages = true;
    @SerialEntry
    public boolean isBlockCaseMessages = true;
    @SerialEntry
    public boolean isBlockPaymentMessages = true;
    @SerialEntry
    public boolean isBlockPVPLeaveMessages = true;
    @SerialEntry
    public boolean isBlockSusPVPMessages = true;
    @SerialEntry
    public boolean isBlockWonDuelMessages = true;
    @SerialEntry
    public boolean isBlockAutoMineMessages = true;

    private static Config instance = null;
    private static ConfigClassHandler<Config> HANDLER = ConfigClassHandler.createBuilder(Config.class)
            .id(Identifier.of(MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("chatimprover/%s.json5".formatted(MOD_ID)))
                    .setJson5(true)
                    .build())
            .build();

    public static void save() {
        HANDLER.save();
    }

    public static Config getInst() {
        if (instance == null) {
            if (HANDLER.load())
                instance = HANDLER.instance();
            else
                LOGGER.error("Failed to load config");
        }
        return instance;
    }
}
