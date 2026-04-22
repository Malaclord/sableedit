package hu.malaclord.sableedit.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.platform.AbstractPlayerActor;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.block.Counter;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.visitor.RegionVisitor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.util.formatting.text.TranslatableComponent;
import com.sk89q.worldedit.world.World;
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import dev.ryanhcode.sable.companion.math.BoundingBox3i;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import hu.malaclord.sableedit.Adaptor;
import hu.malaclord.sableedit.PlayerProxyExtended;
import hu.malaclord.sableedit.context.LocationContext;
import hu.malaclord.sableedit.context.SubLevelContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.List;
import java.util.stream.Collectors;

public class AssembleCommand implements hu.malaclord.sableedit.command.Command {
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        Command<CommandSourceStack> command = (ctx) -> {
            Actor actor = Adaptor.getInstance().adaptCommandSource(ctx.getSource());

            ctx.getSource().sendSystemMessage(Component.literal("yup"));

            if (!actor.isPlayer()) return -1;

            if (WorldEdit.getInstance().getPlatformManager().createProxyActor(actor) instanceof PlayerProxyExtended proxy) {
                LocationContext context = proxy.sableEdit$getContext();

                //if (!context.isLevel()) return 0;

                LocalSession session = WorldEdit.getInstance().getSessionManager().getIfPresent(actor);
                if (session == null) return 0;

                try (EditSession editSession = session.createEditSession(actor)) {
                    Level level = Adaptor.getInstance().adapt(editSession.getWorld());
                    if (!(level instanceof ServerLevel serverLevel)) return 0;
                    BlockAssemble assemble = new BlockAssemble(editSession);
                    Region selection = session.getSelection();
                    RegionVisitor visitor = new RegionVisitor(selection, assemble);

                    Operations.completeBlindly(visitor);

                    List<BlockPos> blocks = assemble.getBlocks().stream().map(
                            blockVector3 -> Adaptor.getInstance().adapt(blockVector3)
                    ).toList();

                    BoundingBox3i bounds = new BoundingBox3i(
                            Adaptor.getInstance().adapt(selection.getMinimumPoint()),
                            Adaptor.getInstance().adapt(selection.getMaximumPoint())
                    );

                    bounds.set(
                            bounds.minX - 1,
                            bounds.minY - 1,
                            bounds.minZ - 1,
                            bounds.maxX + 1,
                            bounds.maxY + 1,
                            bounds.maxZ + 1
                    );


                    ServerSubLevel subLevel = SubLevelAssemblyHelper.assembleBlocks(serverLevel, blocks.getFirst(), blocks, bounds);

                    if (subLevel.getMassTracker().isInvalid()) {
                        actor.printError(TranslatableComponent.of("commands.sable.sub_level.assemble.no_blocks"));
                    }

                    actor.printInfo(TranslatableComponent.of("commands.sable.sub_level.assemble.region.success", TextComponent.of(String.valueOf(blocks.size()))));

                } catch (IncompleteRegionException e) {
                    throw new RuntimeException(e);
                }
            }

            return 1;
        };
        LiteralArgumentBuilder<CommandSourceStack> base = LiteralArgumentBuilder.literal("/assemble");
        dispatcher.register(base.executes(command));
    }
}
