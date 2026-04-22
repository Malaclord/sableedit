package hu.malaclord.sableedit.command;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.math.BlockVector3;

import java.util.ArrayList;
import java.util.List;

public class BlockAssemble implements RegionFunction {
    private final Extent extent;

    private final List<BlockVector3> blocks = new ArrayList<>();

    public BlockAssemble(Extent extent) {
        this.extent = extent;
    }

    public List<BlockVector3> getBlocks() {
        return blocks;
    }

    @Override
    public boolean apply(BlockVector3 position) throws WorldEditException {
        return blocks.add(position);
    }
}
