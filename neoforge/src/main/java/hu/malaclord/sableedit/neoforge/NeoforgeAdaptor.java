package hu.malaclord.sableedit.neoforge;

import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.neoforge.NeoForgeAdapter;
import com.sk89q.worldedit.world.World;
import hu.malaclord.sableedit.Adaptor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.Level;

public class NeoforgeAdaptor extends Adaptor {
    @Override
    public Level adapt(World world) {
        return NeoForgeAdapter.adapt(world);
    }

    @Override
    public Actor adaptCommandSource(CommandSourceStack source) {
        return NeoForgeAdapter.adaptCommandSource(source);
    }
}
