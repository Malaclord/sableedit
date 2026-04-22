package hu.malaclord.sableedit.fabric;

import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import hu.malaclord.sableedit.Adaptor;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class FabricAdaptor extends Adaptor {
    @Override
    public Level adapt(World world) {
        return FabricAdapter.adapt(world);
    }

    @Override
    public Actor adaptCommandSource(CommandSourceStack source) {
        return FabricAdapter.adaptCommandSource(source);
    }


}
