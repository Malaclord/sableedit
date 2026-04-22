package hu.malaclord.sableedit.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

import java.util.ArrayList;
import java.util.List;

public class Commands {
    private static final List<Command> commands = new ArrayList<>();

    private static final AssembleCommand ASSEMBLE_COMMAND = add(new AssembleCommand());

    private static <E extends Command> E add(E command) {
        commands.add(command);
        return command;
    }


    public static void registerAll(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commands.forEach(command -> command.register(commandDispatcher));
    }
}
