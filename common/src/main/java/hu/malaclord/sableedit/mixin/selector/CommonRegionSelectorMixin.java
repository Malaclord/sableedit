package hu.malaclord.sableedit.mixin.selector;

import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.*;
import com.sk89q.worldedit.world.World;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import hu.malaclord.sableedit.RegionSelectorExtended;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(value = {
        CuboidRegionSelector.class,
        Polygonal2DRegionSelector.class,
        CylinderRegionSelector.class,
        EllipsoidRegionSelector.class,
        ConvexPolyhedralRegionSelector.class,
        SphereRegionSelector.class,
        ExtendingCuboidRegionSelector.class
})
public abstract class CommonRegionSelectorMixin implements RegionSelectorExtended, RegionSelector {
    @Unique
    boolean sableEdit$changedSublevel;
    @Unique boolean sableEdit$outsideSublevel;

    @javax.annotation.Nullable
    @Unique SubLevelAccess sableEdit$subLevelAccess = null;

    @Override
    public void sableEdit$markChangedSublevel(boolean b) {
        sableEdit$changedSublevel = b;
    }

    @Override
    public boolean sableEdit$changedSublevel() {
        return sableEdit$changedSublevel;
    }

    @Override
    public void sableEdit$markOutsideSublevel(boolean b) {
        sableEdit$outsideSublevel = b;
    }

    @Override
    public boolean sableEdit$outsideSublevel() {
        return sableEdit$outsideSublevel;
    }

    @Override
    public void sableEdit$setCurrentSubLevel(@Nullable SubLevelAccess access) {
        sableEdit$subLevelAccess = access;
    }

    @Override
    public SubLevelAccess sableEdit$getCurrentSubLevel() {
        return sableEdit$subLevelAccess;
    }

    @Override
    public World sableEdit$getWorld() {
        return getWorld();
    }

    @Override
    public void sableEdit$clear() {
        clear();
    }
}
