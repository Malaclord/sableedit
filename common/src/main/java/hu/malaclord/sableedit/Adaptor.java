package hu.malaclord.sableedit;

import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.world.World;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public abstract class Adaptor {
    private static Adaptor instance;

    public static Adaptor getInstance() {
        if (instance == null) {
            instance = SableEdit.getAdaptor();
        }

        return instance;
    }

    public abstract Level adapt(World world);
    public abstract Actor adaptCommandSource(CommandSourceStack source);
    public BlockPos adapt(BlockVector3 blockVector3) {
        return new BlockPos(blockVector3.x(), blockVector3.y(), blockVector3.z());
    }

    public boolean isInPlotGrid(World world, BlockVector3 blockVector3) {
        return SableCompanion.INSTANCE.isInPlotGrid(adapt(world), adapt(blockVector3));
    }

    public @Nullable SubLevelAccess getContaining(World world, BlockVector3 blockVector3) {
        return SableCompanion.INSTANCE.getContaining(adapt(world), adapt(blockVector3));
    }

    public Vector3d adapt(Vector3 vector3) {
        return new Vector3d(vector3.x(), vector3.y(), vector3.z());
    }

    public Vector3d adapt(Vector3 vector3, Vector3d dest) {
        dest.x = vector3.x();
        dest.y = vector3.y();
        dest.z = vector3.z();
        return dest;
    }

    public Vector3 adapt(Vector3dc vector3dc) {
        return new Vector3(vector3dc.x(), vector3dc.y(), vector3dc.z());
    }
}
