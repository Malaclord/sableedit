package hu.malaclord.sableedit.mixin;

import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.permission.ActorSelectorLimits;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ActorSelectorLimits.class)
public interface ActorSelectorLimitsAccessor {
    @Accessor
    Actor getActor();
}
