package hu.malaclord.sableedit.neoforge;

import hu.malaclord.sableedit.SableEdit;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(SableEdit.MOD_ID)
@EventBusSubscriber
public final class SableEditNeoForge {
    public SableEditNeoForge() {
        // Run our common setup.
        SableEdit.init(new NeoforgeAdaptor());
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent commandsEvent) {
        SableEdit.registerCommands(commandsEvent.getDispatcher());
    }
}
