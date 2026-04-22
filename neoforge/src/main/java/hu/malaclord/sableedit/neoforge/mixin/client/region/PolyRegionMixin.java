package hu.malaclord.sableedit.neoforge.mixin.client.region;

import dev.ryanhcode.sable.companion.SableCompanion;
import hu.malaclord.sableedit.neoforge.client.RegionMixedin;
import org.enginehub.worldeditcui.WorldEditCUI;
import org.enginehub.worldeditcui.render.RenderStyle;
import org.enginehub.worldeditcui.render.region.PolygonRegion;
import org.enginehub.worldeditcui.render.region.Region;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PolygonRegion.class)
public abstract class PolyRegionMixin extends Region implements RegionMixedin {
    @Shadow
    private int max;

    @Shadow
    private int min;

    protected PolyRegionMixin(WorldEditCUI controller, RenderStyle... styles) {
        super(controller, styles);
    }

    @Inject(method = "setPolygonPoint", at = @At("HEAD"))
    void setPolygonPointInjected(int id, int x, int z, CallbackInfo ci) {
        sableEdit$setClientSubLevelAccess(SableCompanion.INSTANCE.getContainingClient(new Vector3d(x, this.min, z)));
    }
}
