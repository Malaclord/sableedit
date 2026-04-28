package hu.malaclord.sableedit.neoforge.mixin.client;

import org.enginehub.worldeditcui.event.CUIEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CUIEvent.class)
public abstract class CUIEventMixin {
    @Shadow
    @Final
    protected String[] params;

    @Inject(method = "getInt", at = @At("RETURN"), cancellable = true)
    void injected(int index, CallbackInfoReturnable<Integer> cir) {
        try {
            int v = Integer.parseInt(this.params[index]);
            cir.setReturnValue(v);
            return;
        } catch (NumberFormatException ignored) { }

        try {
            double d = Double.parseDouble(this.params[index]);
            cir.setReturnValue((int)Math.round(d));
        } catch (NumberFormatException ignored) { }
    }
}
