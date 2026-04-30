package hu.malaclord.sableedit.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.visitor.RegionVisitor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionOperationException;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.util.formatting.text.TranslatableComponent;
import com.sk89q.worldedit.world.World;
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import dev.ryanhcode.sable.companion.math.BoundingBox3i;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import hu.malaclord.sableedit.Adaptor;
import hu.malaclord.sableedit.PlayerProxyExtended;
import hu.malaclord.sableedit.RegionSelectorExtended;
import hu.malaclord.sableedit.context.LocationContext;
import hu.malaclord.sableedit.context.SubLevelContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.List;

public class AssembleCommand implements hu.malaclord.sableedit.command.Command {
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        Command<CommandSourceStack> command = (ctx) -> {
            final Actor actor = Adaptor.getInstance().adaptCommandSource(ctx.getSource());

            if (!actor.isPlayer()) return -1;

            if (WorldEdit.getInstance().getPlatformManager().createProxyActor(actor) instanceof PlayerProxyExtended proxy) {
                LocationContext context = proxy.sableEdit$getContext();
                LocalSession session = WorldEdit.getInstance().getSessionManager().getIfPresent(actor);
                if (session == null) return 0;

                try (EditSession editSession = session.createEditSession(actor)) {
                    Level level = Adaptor.getInstance().adapt(editSession.getWorld());
                    if (!(level instanceof ServerLevel serverLevel)) return 0;
                    final BlockAssemble assemble = new BlockAssemble(editSession);
                    final Region selection = session.getSelection();
                    final RegionVisitor visitor = new RegionVisitor(selection, assemble);

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


                    final ServerSubLevel subLevel = SubLevelAssemblyHelper.assembleBlocks(serverLevel, blocks.getFirst(), blocks, bounds);

                    if (subLevel.getMassTracker().isInvalid()) {
                        actor.printError(TranslatableComponent.of("commands.sable.sub_level.assemble.no_blocks"));
                        return -1;
                    }

                    actor.printInfo(TranslatableComponent.of("commands.sable.sub_level.assemble.region.success", TextComponent.of(String.valueOf(blocks.size()))));

                    final World world = editSession.getWorld();
                    final Region region = session.getSelection(world);
                    final BlockVector3 originalPoint = selection.getMinimumPoint();

                    final RegionSelector selector = session.getRegionSelector(world);

                    Location location = new Location(world, selection.getMinimumPoint().toVector3());

                    location = context.transformBack(location);

                    final SubLevelContext newContext = new SubLevelContext(subLevel);

                    location = newContext.transform(location);

                    proxy.sableEdit$setContext(newContext);

                    ((RegionSelectorExtended) selector).sableEdit$setCurrentSubLevel(subLevel);
                    ((RegionSelectorExtended) selector).sableEdit$markChangedSublevel(true);

                    var newPoint = new BlockVector3(
                            location.getBlockX(),
                            location.getBlockY(),
                            location.getBlockZ()
                    );

                    region.shift(newPoint.subtract(originalPoint));

                    selector.learnChanges();
                    // TODO: delay this to only happen after sub-level is received by client
                    //       im no networking professional
                    selector.explainRegionAdjust(actor, session);
                } catch (IncompleteRegionException | RegionOperationException e) {
                    throw new RuntimeException(e);
                }
            }

            return 1;
        };
        LiteralArgumentBuilder<CommandSourceStack> base = LiteralArgumentBuilder.literal("/assemble");
        dispatcher.register(base.executes(command));
    }
}
