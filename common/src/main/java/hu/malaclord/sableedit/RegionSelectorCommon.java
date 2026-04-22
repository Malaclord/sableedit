package hu.malaclord.sableedit.mixin;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.util.formatting.text.TranslatableComponent;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import hu.malaclord.sableedit.Adaptor;
import hu.malaclord.sableedit.SableEdit;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class RegionSelectorCommon {
    static void selectPrimary(RegionSelectorExtended selector, BlockVector3 position, SelectorLimits limits, CallbackInfoReturnable<Boolean> cir) {
        boolean inPlotGrid = Adaptor.getInstance().isInPlotGrid(selector.sableEdit$getWorld(), position);

        if (inPlotGrid) {
            SubLevelAccess access = Adaptor.getInstance().getContaining(selector.sableEdit$getWorld(), position);
            if (selector.sableEdit$getCurrentSubLevel() != access.getUniqueId()) {
                selector.sableEdit$setCurrentSubLevel(access.getUniqueId());

                selector.sableEdit$markChangedSublevel(true);
            }
        } else {
            if (selector.sableEdit$getCurrentSubLevel() != null) {
                selector.sableEdit$setCurrentSubLevel(null);

                selector.sableEdit$markChangedSublevel(true);
            }
        }
    }

    static void explainPrimarySelection(RegionSelectorExtended selector, Actor player, LocalSession session, BlockVector3 pos, CallbackInfo ci) {
        if (selector.sableEdit$changedSublevel()) {
            player.printInfo(
                    TranslatableComponent.of(
                            "sableedit.selection.explain.changed",
                            TextComponent.of(
                                    selector.sableEdit$getCurrentSubLevel() == null
                                            ? "world"
                                            : selector.sableEdit$getCurrentSubLevel().toString()
                            )
                    )
            );
            selector.sableEdit$markChangedSublevel(false);
        }
    }
}
