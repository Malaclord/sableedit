package hu.malaclord.sableedit.neoforge.mixin.client.region;

import dev.ryanhcode.sable.companion.SableCompanion;
import hu.malaclord.sableedit.neoforge.client.RegionMixedin;
import net.minecraft.core.Vec3i;
import org.enginehub.worldeditcui.WorldEditCUI;
import org.enginehub.worldeditcui.render.RenderStyle;
import org.enginehub.worldeditcui.render.region.CylinderRegion;
import org.enginehub.worldeditcui.render.region.Region;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CylinderRegion.class)
public abstract class CylinderRegionMixin extends Region implements RegionMixedin {
    protected CylinderRegionMixin(WorldEditCUI controller, RenderStyle... styles) {
        super(controller, styles);
    }

    @Inject(method = "setCylinderCenter", at = @At("HEAD"))
    void setCylinderCenterInjected(int x, int y, int z, CallbackInfo ci) {
        sableEdit$setClientSubLevelAccess(SableCompanion.INSTANCE.getContainingClient(new Vec3i(x, y, z)));
    }
}
