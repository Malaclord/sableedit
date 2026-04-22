package hu.malaclord.sableedit;

import com.sk89q.worldedit.world.World;
import dev.ryanhcode.sable.companion.SubLevelAccess;

import javax.annotation.Nullable;
import java.util.UUID;

public interface RegionSelectorExtended {
    void    sableEdit$markChangedSublevel(boolean b);
    boolean sableEdit$changedSublevel();
    void    sableEdit$markOutsideSublevel(boolean b);
    boolean sableEdit$outsideSublevel();

    void    sableEdit$clear();


    void    sableEdit$setCurrentSubLevel(@Nullable SubLevelAccess access);
    SubLevelAccess    sableEdit$getCurrentSubLevel();

    World sableEdit$getWorld();
}
