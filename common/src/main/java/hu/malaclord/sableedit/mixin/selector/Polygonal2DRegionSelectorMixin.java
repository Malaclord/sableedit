package hu.malaclord.sableedit.mixin.selector;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.selector.Polygonal2DRegionSelector;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;
import com.sk89q.worldedit.world.World;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import hu.malaclord.sableedit.RegionSelectorCommon;
import hu.malaclord.sableedit.RegionSelectorExtended;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Polygonal2DRegionSelector.class)
public abstract class Polygonal2DRegionSelectorMixin implements RegionSelectorExtended {
    @Inject(method = "selectPrimary", at = @At("HEAD"))
    void selectPrimaryInjected(BlockVector3 position, SelectorLimits limits, CallbackInfoReturnable<Boolean> cir) {
        RegionSelectorCommon.selectPrimary(this, position, limits, cir);
    }

    @Inject(method = "selectSecondary", at = @At("HEAD"), cancellable = true)
    void selectSecondaryInjected(BlockVector3 position, SelectorLimits limits, CallbackInfoReturnable<Boolean> cir) {
        RegionSelectorCommon.selectSecondary(this, position, limits, cir);
    }

    @Inject(method = "explainPrimarySelection", at = @At("HEAD"))
    void explainPrimarySelectionInjected(Actor player, LocalSession session, BlockVector3 pos, CallbackInfo ci) {
        RegionSelectorCommon.explainPrimarySelection(this, player, session, pos, ci);
    }

    @Redirect(method = "explainPrimarySelection", at = @At(value = "INVOKE", target = "Lcom/sk89q/worldedit/math/BlockVector3;toString()Ljava/lang/String;"))
    String explainPrimarySelectionBlockVector3ToString(BlockVector3 instance) {
        return RegionSelectorCommon.blockVector3ToString(this, instance);
    }

    @Redirect(method = "explainSecondarySelection", at = @At(value = "INVOKE", target = "Lcom/sk89q/worldedit/math/BlockVector3;toString()Ljava/lang/String;"))
    String explainSecondarySelectionBlockVector3ToString(BlockVector3 instance) {
        return RegionSelectorCommon.blockVector3ToString(this, instance);
    }
}
