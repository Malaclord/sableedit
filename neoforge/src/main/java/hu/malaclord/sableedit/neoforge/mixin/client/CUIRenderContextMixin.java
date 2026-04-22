package hu.malaclord.sableedit.neoforge.mixin.client;

import dev.ryanhcode.sable.companion.ClientSubLevelAccess;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import hu.malaclord.sableedit.neoforge.client.CUIRenderContextMixedin;
import net.minecraft.client.Minecraft;
import org.enginehub.worldeditcui.event.listeners.CUIRenderContext;
import org.enginehub.worldeditcui.render.RenderSink;
import org.enginehub.worldeditcui.util.Vector3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(CUIRenderContext.class)
public abstract class CUIRenderContextMixin implements CUIRenderContextMixedin {
    @Shadow
    private float dt;
    @Shadow
    private Vector3 cameraPos;

    @Shadow
    public abstract Vector3 cameraPos();

    @Unique
    @Nullable
    ClientSubLevelAccess sableEdit$pose;

    @Override
    public ClientSubLevelAccess sableEdit$getSubLevelAccess() {
        return sableEdit$pose;
    }

    @Override
    public void sableEdit$setSubLevelAccess(ClientSubLevelAccess pose) {
        this.sableEdit$pose = pose;
    }

    @Unique
    Vector3d sableEdit$vector3d = new Vector3d();

    @Unique
    Matrix4d sableEdit$matrix4d = new Matrix4d();

    @Inject(method = "cameraPos", at = @At("RETURN"), cancellable = true)
    void cameraPosInjected(CallbackInfoReturnable<Vector3> cir) {
        ClientSubLevelAccess access = sableEdit$getSubLevelAccess();
        if (access == null) return;

        sableEdit$vector3d.set(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
        var pose = access.renderPose(dt);
        pose.transformPositionInverse(sableEdit$vector3d);

        cir.setReturnValue(new Vector3(
                sableEdit$vector3d.x,
                sableEdit$vector3d.y,
                sableEdit$vector3d.z
        ));
    }

    @Redirect(method = "vertex(DDD)Lorg/enginehub/worldeditcui/event/listeners/CUIRenderContext;", at = @At(value = "INVOKE", target = "Lorg/enginehub/worldeditcui/render/RenderSink;vertex(DDD)Lorg/enginehub/worldeditcui/render/RenderSink;"))
    RenderSink redirected(RenderSink instance, double x, double y, double z) {
        ClientSubLevelAccess access = sableEdit$getSubLevelAccess();
        if (access != null) {
            sableEdit$vector3d.set(x, y, z);
            var pose = access.renderPose(dt);
            pose.transformNormal(sableEdit$vector3d);

            x = sableEdit$vector3d.x;
            y = sableEdit$vector3d.y;
            z = sableEdit$vector3d.z;
        }
        return instance.vertex(x, y, z);
    }
}
