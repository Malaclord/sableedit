package hu.malaclord.sableedit.mixin;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.command.SelectionCommands;
import com.sk89q.worldedit.command.argument.SelectorChoiceOrList;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.world.World;
import hu.malaclord.sableedit.PlayerProxyExtended;
import hu.malaclord.sableedit.RegionSelectorExtended;
import hu.malaclord.sableedit.context.LevelContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SelectionCommands.class)
public class SelectionCommandsMixin {
    @Inject(method = "select", at = @At(value = "INVOKE", target = "Lcom/sk89q/worldedit/regions/RegionSelector;clear()V"))
    void selectInjected(Actor actor, World world, LocalSession session, SelectorChoiceOrList selectorChoiceOrList, boolean setDefaultSelector, CallbackInfo ci) {
        if (WorldEdit.getInstance().getPlatformManager().createProxyActor(actor) instanceof PlayerProxyExtended playerProxy) {
            playerProxy.sableEdit$setContext(new LevelContext());
        }
        ((RegionSelectorExtended)session.getRegionSelector(world)).sableEdit$setCurrentSubLevel(null);
    }
}
