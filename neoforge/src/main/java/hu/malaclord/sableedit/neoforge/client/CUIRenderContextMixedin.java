package hu.malaclord.sableedit.neoforge.client;

import dev.ryanhcode.sable.companion.ClientSubLevelAccess;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import org.jetbrains.annotations.Nullable;

public interface CUIRenderContextMixedin {
    @Nullable ClientSubLevelAccess sableEdit$getSubLevelAccess();
    void sableEdit$setSubLevelAccess(@Nullable ClientSubLevelAccess pose);
}
