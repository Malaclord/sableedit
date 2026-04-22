package hu.malaclord.sableedit.neoforge.client;

import dev.ryanhcode.sable.companion.ClientSubLevelAccess;
import org.jetbrains.annotations.Nullable;

public interface RegionMixedin {
    void sableEdit$setClientSubLevelAccess(@Nullable ClientSubLevelAccess access);
    @Nullable ClientSubLevelAccess sableEdit$getClientSubLevelAccess();
}
