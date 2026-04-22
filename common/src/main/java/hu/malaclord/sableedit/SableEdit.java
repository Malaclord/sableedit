package hu.malaclord.sableedit;

import com.mojang.brigadier.CommandDispatcher;
import dev.ryanhcode.sable.sublevel.SubLevel;
import hu.malaclord.sableedit.command.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;

public final class SableEdit {
    public static final String MOD_ID = "sableedit";
    private static Adaptor adaptor;

    public static void init(Adaptor ad) {
        adaptor = ad;
    }

    public static @NotNull Adaptor getAdaptor() {
        if (adaptor == null) throw new IllegalStateException("called getAdaptor before mod init");
        return adaptor;
    };

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        Commands.registerAll(dispatcher);
    }

    public static void requestChunk(SubLevel subLevel, ChunkPos pos) {
        if (!subLevel.getPlot().contains(pos)) subLevel.getPlot().newEmptyChunk(pos);
    }
}
