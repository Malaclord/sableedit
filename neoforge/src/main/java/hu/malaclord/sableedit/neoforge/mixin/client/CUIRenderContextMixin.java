package hu.malaclord.sableedit.neoforge.mixin.client;

import dev.ryanhcode.sable.companion.ClientSubLevelAccess;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import hu.malaclord.sableedit.neoforge.client.CUIRenderContextMixedin;
import org.enginehub.worldeditcui.event.listeners.CUIRenderContext;
import org.enginehub.worldeditcui.render.RenderSink;
import org.enginehub.worldeditcui.util.Vector3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CUIRenderContext.class)
public abstract class CUIRenderContextMixin implements CUIRenderContextMixedin {
    @Shadow
    private float dt;
    @Shadow
    private Vector3 cameraPos;

    @Unique
    @Nullable
    ClientSubLevelAccess sableEdit$subLevelAccess;

    @Override
    public ClientSubLevelAccess sableEdit$getSubLevelAccess() {
        return sableEdit$subLevelAccess;
    }

    @Override
    public void sableEdit$setSubLevelAccess(ClientSubLevelAccess subLevelAccess) {
        this.sableEdit$subLevelAccess = subLevelAccess;
    }

    @Unique
    Vector3d sableEdit$vector3d = new Vector3d();

    @Unique
    @Nullable
    Pose3dc sableEdit$pose = null;

    @Inject(method = "init", at = @At("TAIL"))
    void initInjected(Vector3 cameraPos, float dt, RenderSink sink, CallbackInfo ci) {
        ClientSubLevelAccess access = sableEdit$getSubLevelAccess();
        if (access == null) {
            this.sableEdit$pose = null;
            return;
        }

        this.sableEdit$pose = access.renderPose(dt);

        sableEdit$vector3d.set(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
        var pose = access.renderPose(dt);
        pose.transformPositionInverse(sableEdit$vector3d);

        this.cameraPos = new Vector3(
                sableEdit$vector3d.x,
                sableEdit$vector3d.y,
                sableEdit$vector3d.z
        );
    }

    // TODO: Move this to the matrix stack, if possible
    @Redirect(method = "vertex(DDD)Lorg/enginehub/worldeditcui/event/listeners/CUIRenderContext;", at = @At(value = "INVOKE", target = "Lorg/enginehub/worldeditcui/render/RenderSink;vertex(DDD)Lorg/enginehub/worldeditcui/render/RenderSink;"))
    RenderSink redirected(RenderSink instance, double x, double y, double z) {
        if (sableEdit$pose != null) {
            sableEdit$vector3d.set(x, y, z);
            sableEdit$pose.transformNormal(sableEdit$vector3d);

            x = sableEdit$vector3d.x;
            y = sableEdit$vector3d.y;
            z = sableEdit$vector3d.z;
        }
        return instance.vertex(x, y, z);
    }
}
