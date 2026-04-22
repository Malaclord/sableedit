package hu.malaclord.sableedit.mixin;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.command.SelectionCommands;
import com.sk89q.worldedit.command.argument.SelectorChoice;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SelectionCommands.class)
public class SelectionCommandsMixin {
    @Inject(method = "pos", at = @At("HEAD"))
    void posInjected(Actor actor, World world, LocalSession session, BlockVector3 pos1, List<BlockVector3> pos2, SelectorChoice selectorChoice, CallbackInfo ci) {

    }
}
