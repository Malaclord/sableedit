package hu.malaclord.sableedit.neoforge.mixin.client.region;

import dev.ryanhcode.sable.companion.ClientSubLevelAccess;
import hu.malaclord.sableedit.neoforge.client.RegionMixedin;
import org.enginehub.worldeditcui.render.region.Region;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Region.class)
public class RegionMixin implements RegionMixedin {
    @Unique
    private ClientSubLevelAccess sableEdit$subLevelAccess;

    @Override
    public void sableEdit$setClientSubLevelAccess(ClientSubLevelAccess access) {
        this.sableEdit$subLevelAccess = access;
    }

    @Override
    public ClientSubLevelAccess sableEdit$getClientSubLevelAccess() {
        return sableEdit$subLevelAccess;
    }
}
