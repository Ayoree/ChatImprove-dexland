package org.ayoree.chatimprove.dexland;

import java.util.function.Predicate;

import org.ayoree.chatimprove.dexland.messages.RealnameChatMessage;
import org.ayoree.chatimprover.api.Filter;

import com.google.auto.service.AutoService;

import net.minecraft.network.chat.Component;

public class RealnameFilterImpl extends Filter {
    @AutoService(Provider.class)
    public static class RealnameFilterProviderImpl extends AddonInformerImpl implements Provider {
        @Override
        public Predicate<Component> validator() {
            return text -> {
                final Predicate<Component> realNameValidator = new RealnameChatMessage.ProviderImpl().validator();
                if (realNameValidator.test(text))
                    RealnameReceiver.getInst().receivedRealnameMessage(new RealnameChatMessage(text));
                else
                    RealnameReceiver.getInst().receivedOtherMessage();
                return RealnameReceiver.getInst().isNeedToBlock();
            };
        }
    }
}
