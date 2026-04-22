package hu.malaclord.sableedit.neoforge.mixin.client.region;


import hu.malaclord.sableedit.neoforge.client.CUIRenderContextMixedin;
import hu.malaclord.sableedit.neoforge.client.RegionMixedin;
import org.enginehub.worldeditcui.event.listeners.CUIRenderContext;
import org.enginehub.worldeditcui.render.region.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {
        CuboidRegion.class,
        CylinderRegion.class,
        PolygonRegion.class,
        EllipsoidRegion.class,
        PolyhedronRegion.class
})
public abstract class CommonRegionMixin implements RegionMixedin {
    @Inject(method = "render", at = @At("HEAD"))
    void renderMixedIn(CUIRenderContext ctx, CallbackInfo ci) {
        ((CUIRenderContextMixedin) (Object) ctx).sableEdit$setSubLevelAccess(sableEdit$getClientSubLevelAccess());
    }
}
