package hu.malaclord.sableedit.neoforge.mixin.client;

import dev.ryanhcode.sable.companion.ClientSubLevelAccess;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import hu.malaclord.sableedit.neoforge.client.CUIRenderContextMixedin;
import org.enginehub.worldeditcui.event.listeners.CUIRenderContext;
import org.enginehub.worldeditcui.render.shapes.RenderEllipsoid;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderEllipsoid.class)
public class RenderEllipsoidMixin {
    @Unique
    private final Quaterniond sableEdit$quatD = new Quaterniond();
    @Unique
    private final Quaternionf sableEdit$quatF = new Quaternionf();

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4fStack;translate(FFF)Lorg/joml/Matrix4f;"))
    void injectedBeforeTranslate(CUIRenderContext ctx, CallbackInfo ci) {
        ClientSubLevelAccess access = ((CUIRenderContextMixedin) (Object) ctx).sableEdit$getSubLevelAccess();
        if (access == null) {
            return;
        }
        Pose3dc pose = access.renderPose(ctx.dt());
        ctx.poseStack().rotate(sableEdit$quatF.set(pose.orientation()));
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4fStack;translate(FFF)Lorg/joml/Matrix4f;", shift = At.Shift.AFTER))
    void injectedAfterTranslate(CUIRenderContext ctx, CallbackInfo ci) {
        ClientSubLevelAccess access = ((CUIRenderContextMixedin) (Object) ctx).sableEdit$getSubLevelAccess();
        if (access == null) {
            return;
        }
        Pose3dc pose = access.renderPose(ctx.dt());

        sableEdit$quatF.set(pose.orientation().invert(sableEdit$quatD));

        ctx.poseStack().rotate(sableEdit$quatF);
    }
}
