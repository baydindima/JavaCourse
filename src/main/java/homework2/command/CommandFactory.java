package homework2.command;

import homework2.command.impl.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Dmitriy Baidin on 9/23/2016.
 */
public class CommandFactory {

    private static final Map<String, Supplier<Command>> commands;

    static {
        commands = new HashMap<>();
        commands.put("add", AddCommand::new);
        commands.put("checkout", CheckoutCommand::new);
        commands.put("close", CloseBranchCommand::new);
        commands.put("commit", CommitCommand::new);
        commands.put("branch", CreateBranchCommand::new);
        commands.put("init", InitCommand::new);
        commands.put("log", LogCommand::new);
        commands.put("merge", MergeCommand::new);
        commands.put("status", StatusCommand::new);
        commands.put("clean", CleanCommand::new);
        commands.put("reset", ResetCommand::new);
        commands.put("rm", RmCommand::new);
    }

    private CommandFactory() {
    }

    public static Command getCommandByName(String name) {
        return commands.getOrDefault(name, FailCommand::new).get();
    }

}
