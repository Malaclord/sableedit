package hu.malaclord.sableedit.neoforge.mixin.client.region;

import dev.ryanhcode.sable.companion.SableCompanion;
import hu.malaclord.sableedit.neoforge.client.RegionMixedin;
import org.enginehub.worldeditcui.WorldEditCUI;
import org.enginehub.worldeditcui.render.RenderStyle;
import org.enginehub.worldeditcui.render.region.PolyhedronRegion;
import org.enginehub.worldeditcui.render.region.Region;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PolyhedronRegion.class)
public abstract class PolyhedronRegionMixin extends Region implements RegionMixedin {
    protected PolyhedronRegionMixin(WorldEditCUI controller, RenderStyle... styles) {
        super(controller, styles);
    }

    @Inject(method = "setCuboidPoint", at = @At("HEAD"))
    void setCuboidPointInjected(int id, double x, double y, double z, CallbackInfo ci) {
        sableEdit$setClientSubLevelAccess(SableCompanion.INSTANCE.getContainingClient(new Vector3d(x, y, z)));
    }
}
