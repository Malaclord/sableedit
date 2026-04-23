package hu.malaclord.sableedit.mixin.selector;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.selector.EllipsoidRegionSelector;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;
import hu.malaclord.sableedit.RegionSelectorCommon;
import hu.malaclord.sableedit.RegionSelectorExtended;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EllipsoidRegionSelector.class)
public abstract class EllipsoidRegionSelectorMixin implements RegionSelectorExtended {
    @Inject(method = "selectPrimary", at = @At(value = "HEAD"))
    void selectPrimaryInjected(BlockVector3 position, SelectorLimits limits, CallbackInfoReturnable<Boolean> cir) {
        RegionSelectorCommon.selectPrimary(this, position, limits, cir);
    }

    @Inject(method = "selectSecondary", at = @At(value = "HEAD"), cancellable = true)
    void selectSecondaryInjected(BlockVector3 position, SelectorLimits limits, CallbackInfoReturnable<Boolean> cir) {
        RegionSelectorCommon.selectSecondary(this, position, limits, cir);
    }

    @Inject(method = "explainPrimarySelection", at = @At(value = "HEAD"))
    void explainPrimarySelectionInjected(Actor player, LocalSession session, BlockVector3 pos, CallbackInfo ci) {
        RegionSelectorCommon.explainPrimarySelection(this, player, session, pos, ci);
    }

    @Redirect(method = "explainPrimarySelection", at = @At(value = "INVOKE", target = "Lcom/sk89q/worldedit/math/Vector3;toString()Ljava/lang/String;"))
    String explainPrimarySelectionBlockVector3ToString(Vector3 instance) {
        return RegionSelectorCommon.posVector3ToString(this, instance);
    }

    @Redirect(method = "explainSecondarySelection", at = @At(value = "INVOKE", target = "Lcom/sk89q/worldedit/math/Vector3;toString()Ljava/lang/String;"))
    String explainSecondarySelectionBlockVector3ToString(Vector3 instance) {
        return RegionSelectorCommon.posVector3ToString(this, instance);
    }
}
