package hu.malaclord.sableedit.mixin;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import dev.ryanhcode.sable.sublevel.SubLevel;
import hu.malaclord.sableedit.Adaptor;
import hu.malaclord.sableedit.PlayerProxyExtended;
import hu.malaclord.sableedit.context.LocationContext;
import hu.malaclord.sableedit.context.SubLevelContext;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(EditSession.class)
public class EditSessionMixin {
    @Shadow
    @Final
    @Nullable
    private Actor actor;

    @Shadow
    @Final
    protected World world;

    @Inject(method = "setBlock(Lcom/sk89q/worldedit/math/BlockVector3;Lcom/sk89q/worldedit/world/block/BlockStateHolder;Lcom/sk89q/worldedit/EditSession$Stage;)Z", at = @At("HEAD"))
    <B extends BlockStateHolder<B>> void setBlockInjectedDumbStupid(BlockVector3 position, B block, EditSession.Stage stage, CallbackInfoReturnable<Boolean> cir) {
        if (actor != null && actor.isPlayer() && actor instanceof PlayerProxyExtended playerProxy) {
            LocationContext context = playerProxy.sableEdit$getContext();
            if (context instanceof SubLevelContext subLevelContext) {
                SubLevelAccess access = subLevelContext.getAccess();
                if (access instanceof SubLevel subLevel) {
                    var inPlotGrid = Sable.HELPER.isInPlotGrid(Adaptor.getInstance().adapt(world), new ChunkPos(Adaptor.getInstance().adapt(position)));
                    if (!inPlotGrid) return;

                    subLevel.getPlot().expandIfNecessary(Adaptor.getInstance().adapt(position));

                    //SableEdit.requestChunk(subLevel, new ChunkPos(Adaptor.getInstance().adapt(position)));
                }
            }
        }
    }
}
