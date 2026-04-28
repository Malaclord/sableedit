package hu.malaclord.sableedit;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.permission.ActorSelectorLimits;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.util.formatting.text.TranslatableComponent;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import hu.malaclord.sableedit.context.LevelContext;
import hu.malaclord.sableedit.context.SubLevelContext;
import hu.malaclord.sableedit.mixin.ActorSelectorLimitsAccessor;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

public class RegionSelectorCommon {
    public static void selectPrimary(RegionSelectorExtended selector, BlockVector3 position, SelectorLimits limits, CallbackInfoReturnable<Boolean> cir) {
        if (changedSublevel(selector, position, true)) {
            if (limits instanceof ActorSelectorLimits actorSelectorLimits) {
                SubLevelAccess access = Adaptor.getInstance().getContaining(selector.sableEdit$getWorld(), position);

                Actor actor =
                        ((ActorSelectorLimitsAccessor) (actorSelectorLimits))
                                .getActor();
                if (actor instanceof PlayerProxyExtended) {
                    ((PlayerProxyExtended)actor)
                            .sableEdit$setContext(
                                    access != null ? new SubLevelContext(access)
                                            : new LevelContext());
                }
            }
            selector.sableEdit$markChangedSublevel(true);
            selector.sableEdit$clear();
        }
    }

    public static void selectSecondary(RegionSelectorExtended selector, BlockVector3 position, SelectorLimits limits, CallbackInfoReturnable<Boolean> cir) {
        if (changedSublevel(selector, position, false)) {
            if (limits instanceof ActorSelectorLimits actorSelectorLimits) {
                Actor actor = ((ActorSelectorLimitsAccessor)(actorSelectorLimits)).getActor();
                actor.printInfo(TranslatableComponent.of("sableedit.selection.error.outside"));
            }
            selector.sableEdit$markOutsideSublevel(true);
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    private static boolean changedSublevel(RegionSelectorExtended selector, BlockVector3 position, boolean primary) {
        boolean inPlotGrid = Adaptor.getInstance().isInPlotGrid(selector.sableEdit$getWorld(), position);
        boolean changed = false;
        if (inPlotGrid) {
            SubLevelAccess access = Adaptor.getInstance().getContaining(selector.sableEdit$getWorld(), position);
            if (((selector.sableEdit$getCurrentSubLevel() != null) ? selector.sableEdit$getCurrentSubLevel().getUniqueId() : null) != access.getUniqueId()) {
                if (primary) selector.sableEdit$setCurrentSubLevel(access);

                changed = true;
            }
        } else {
            if (selector.sableEdit$getCurrentSubLevel() != null) {
                if (primary) selector.sableEdit$setCurrentSubLevel(null);

                changed = true;
            }
        }

        return changed;
    }

    public static void explainPrimarySelection(RegionSelectorExtended selector, Actor player, LocalSession session, BlockVector3 pos, CallbackInfo ci) {
        if (selector.sableEdit$changedSublevel()) {
            session.dispatchCUISelection(player);

            player.printInfo(
                    TranslatableComponent.of(
                            "sableedit.selection.explain.changed",
                            selector.sableEdit$getCurrentSubLevel() == null
                                    ? TranslatableComponent.of("sableedit.selection.explain.world")
                                    : TranslatableComponent.of("sableedit.selection.explain.sublevel", TextComponent.of(
                                            Objects.requireNonNullElse(selector.sableEdit$getCurrentSubLevel().getName(), selector.sableEdit$getCurrentSubLevel().getUniqueId().toString())
                                    )
                            )
                    )
            );
            selector.sableEdit$markChangedSublevel(false);
        }
    }

    public static void explainSecondarySelection() {

    }

    public static String blockVector3ToString(RegionSelectorExtended selector, BlockVector3 instance) {
        SubLevelAccess access = Adaptor.getInstance().getContaining(selector.sableEdit$getWorld(), instance);
        if (access != null) return "*§n(" + instance.x() + ", " + instance.y() + ", " + instance.z() + ")§r";
        else return instance.toString();
    }

    public static String posVector3ToString(RegionSelectorExtended selector, Vector3 instance) {
        SubLevelAccess access = Adaptor.getInstance().getContaining(selector.sableEdit$getWorld(), instance.toBlockPoint());
        if (access != null) return "*§n(" + instance.x() + ", " + instance.y() + ", " + instance.z() + ")§r";
        else return instance.toString();
    }
}
