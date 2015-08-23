package uk.jamierocks.ircrocks;

import com.sk89q.intake.CommandException;
import com.sk89q.intake.context.CommandLocals;
import com.sk89q.intake.dispatcher.Dispatcher;
import com.sk89q.intake.dispatcher.SimpleDispatcher;
import com.sk89q.intake.util.auth.AuthorizationException;
import org.spacehq.iirc.IrcChannel;
import org.spacehq.iirc.IrcProtocol;
import uk.jamierocks.ircrocks.command.HelpCommand;
import uk.jamierocks.ircrocks.command.MeCommand;

public class IrcCommands {

    private static Dispatcher dispatcher = new SimpleDispatcher();

    public static void init() {
        dispatcher.registerCommand(new HelpCommand(), "help");
        dispatcher.registerCommand(new MeCommand(), "me");
    }

    public static void handleCommand(IrcChannel channel, IrcProtocol protocol, String command) {
        CommandLocals locals = new CommandLocals();
        locals.put(IrcChannel.class, channel);
        locals.put(IrcProtocol.class, protocol);
        try {
            IrcCommands.dispatcher.call(command.substring(1, command.length()), locals, new String[0]);
        } catch (CommandException e) {
            e.printStackTrace();
        } catch (AuthorizationException e) {
            e.printStackTrace();
        }
    }
}
