package hu.malaclord.sableedit.mixin.selector;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.selector.SphereRegionSelector;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;
import hu.malaclord.sableedit.RegionSelectorCommon;
import hu.malaclord.sableedit.RegionSelectorExtended;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SphereRegionSelector.class)
public abstract class SphereRegionSelectorMixin implements RegionSelectorExtended {
    @Inject(method = "selectSecondary", at = @At(value = "HEAD"), cancellable = true)
    void selectSecondaryInjected(BlockVector3 position, SelectorLimits limits, CallbackInfoReturnable<Boolean> cir) {
        RegionSelectorCommon.selectSecondary(this, position, limits, cir);
    }
}
