package hu.malaclord.sableedit.mixin;

import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.util.Location;
import hu.malaclord.sableedit.PlayerProxyExtended;
import hu.malaclord.sableedit.context.LevelContext;
import hu.malaclord.sableedit.context.LocationContext;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(targets = "com.sk89q.worldedit.extension.platform.PlayerProxy")
public class PlayerProxyMixin implements PlayerProxyExtended {

    @Shadow
    @Final
    private Player basePlayer;

    @Unique
    @NotNull
    private final static Map<UUID, LocationContext> sableEdit$contexts = new HashMap<>();

    @Override
    public void sableEdit$setContext(@NotNull LocationContext context) {
        sableEdit$contexts.put(basePlayer.getUniqueId(), context);
    }

    @Override
    public @NotNull LocationContext sableEdit$getContext() {
        if (!sableEdit$contexts.containsKey(basePlayer.getUniqueId())) {
            sableEdit$setContext(new LevelContext());
        }
        return sableEdit$contexts.get(basePlayer.getUniqueId());
    }

    @Inject(method = "getLocation", at = @At("RETURN"), cancellable = true)
    void getLocationReturnInjected(CallbackInfoReturnable<Location> cir) {
        cir.setReturnValue(sableEdit$getContext().transform(cir.getReturnValue()));
    }

    @Redirect(method = "setLocation", at = @At(value = "INVOKE", target = "Lcom/sk89q/worldedit/entity/Player;setLocation(Lcom/sk89q/worldedit/util/Location;)Z"))
    boolean setLocationInjected(Player instance, Location location) {
        return instance.setLocation(sableEdit$getContext().transformBack(location));
    }

    @Redirect(method = "trySetPosition", at = @At(value = "INVOKE", target = "Lcom/sk89q/worldedit/entity/Player;trySetPosition(Lcom/sk89q/worldedit/math/Vector3;FF)Z"))
    boolean trySetPositionInjected(Player instance, Vector3 pos, float pitch, float yaw) {
        Location loc = sableEdit$getContext().transformBack(new Location(instance.getExtent(), pos, pitch, yaw));
        return instance.trySetPosition(loc.toVector(), loc.getPitch(), loc.getYaw());
    }

    @Redirect(method = "floatAt", at = @At(value = "INVOKE", target = "Lcom/sk89q/worldedit/entity/Player;floatAt(IIIZ)V"))
    void floatAtInjected(Player instance, int x, int y, int z, boolean b) {
        Location loc = sableEdit$getContext().transformBack(new Location(instance.getExtent(), new Vector3(x, y, z), instance.getLocation().getPitch(), instance.getLocation().getYaw()));
        instance.floatAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), b);
    }
}
