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

// Global chat / Local chat
public class CommonChatMessage extends ChatMessageWithSender {
    protected final String m_textPart;
    private final boolean m_isGlobal;

    public CommonChatMessage(Text message) {
        super(message);
        
        final List<Text> siblings = message.getSiblings();
        final ClickEvent clickEvent = siblings.getFirst().getStyle().getClickEvent();
        assert clickEvent != null && clickEvent.getAction() == ClickEvent.Action.SUGGEST_COMMAND;
        final ClickEvent.SuggestCommand suggestCommand = (ClickEvent.SuggestCommand) clickEvent;
        final String command = suggestCommand.command();
        m_isGlobal = getMessageStr().startsWith("[ɢ]");
        setSenderNick(command.substring(5, command.length() - 1)); // /msg ник
        m_textPart = siblings.get(1).getString();
    }

    @AutoService(Provider.class)
    public static class ProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Text> validator() {
            return message -> {
                // Do not create CommonChatMessage instance if disabled this config option
                if (!Config.getInst().isChangeGlobalLocal)
                    return false;
                final List<Text> siblings = message.getSiblings();
                return siblings.size() == 2 && siblings.getFirst().getString().endsWith("→ ");
            };
        }
        @Override
        public Function<Text, ChatMessage> creator() {
            return CommonChatMessage::new;
        }
    }

    @Override
    public ChatMessage generateChangedMsg() {
        MutableText newMsg = Text.empty();
        final Text donatePart = getRank();
        final Text nickPart = Text.of("§f" + getSenderNick() + " ");
        final List<Text> siblings = m_message.getSiblings();
        final Style msgStyle = Style.EMPTY
            .withHoverEvent(new HoverEvent.ShowText(buildHoverText()))
            .withClickEvent(getClickEvent());
        
        newMsg.append(m_isGlobal ? "§a[ɢ] " : "§e[ʟ] ");
        newMsg.append(donatePart).append(" ").append(nickPart);
        newMsg.setStyle(msgStyle);
        newMsg.append(Text.of("§8→ §r").copy().setStyle(Style.EMPTY));
        for (int i = 1; i < siblings.size(); ++i) {
            newMsg.append(siblings.get(i));
        }
        m_changedMsg = newMsg;
        return this;
    }

    private Text getRank() {
        final HoverEvent hEvent = m_message.getSiblings().getFirst().getStyle().getHoverEvent();
        assert hEvent != null && hEvent.getAction() == HoverEvent.Action.SHOW_TEXT;
        final HoverEvent.ShowText showText = (HoverEvent.ShowText) hEvent;
        final Text hText = showText.value();
        final String text = hText.getString();
        final String first = text.split("\\s+", 2)[0];
        return Text.of(first).copy().setStyle(hText.getStyle());
    }

    private Text buildHoverText() {
        final HoverEvent hEvent = m_message.getSiblings().getFirst().getStyle().getHoverEvent();
        assert hEvent.getAction() == HoverEvent.Action.SHOW_TEXT;
        final HoverEvent.ShowText showText = (HoverEvent.ShowText) hEvent;
        MutableText hoverText = showText.value().copy();
        hoverText.append("\n§8Оригинальный клан/префикс/ник/суффикс:\n");
        hoverText.append(m_message.getSiblings().getFirst());
        return hoverText;
    }

    private ClickEvent getClickEvent() {
        return m_message.getSiblings().getFirst().getStyle().getClickEvent();
    }
}
