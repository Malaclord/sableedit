package hu.malaclord.sableedit.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public interface Command {
    public void register(CommandDispatcher<CommandSourceStack> commandDispatcher);
}
