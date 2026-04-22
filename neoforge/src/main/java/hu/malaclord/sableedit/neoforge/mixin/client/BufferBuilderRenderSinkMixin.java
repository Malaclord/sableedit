package hu.malaclord.sableedit.neoforge.mixin.client;

import org.enginehub.worldeditcui.render.BufferBuilderRenderSink;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BufferBuilderRenderSink.class)
public class BufferBuilderRenderSinkMixin {
    @Inject(method = "flush", at = @At("HEAD"))
    void injected(CallbackInfo ci) {
            
    }
}
