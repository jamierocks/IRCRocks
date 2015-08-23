package uk.jamierocks.ircrocks.command;

import com.sk89q.intake.CommandException;
import com.sk89q.intake.context.CommandLocals;
import com.sk89q.intake.util.auth.AuthorizationException;
import org.spacehq.iirc.IrcChannel;
import org.spacehq.iirc.IrcProtocol;

public class MeCommand extends BaseCommand {

    @Override
    public boolean call(String arguments, CommandLocals locals, String[] parentCommands)
            throws CommandException, AuthorizationException {
        String[] args = arguments.split(" ");
        IrcChannel channel = locals.get(IrcChannel.class);
        IrcProtocol protocol = locals.get(IrcProtocol.class);

        if (args.length == 0) {
            CommandUtils.printUsage(locals.get(IrcChannel.class), "/me <action>");
            return false;
        }

        String action = args[0];
        protocol.sendAction(channel.getName(), action);
        protocol.onAction(protocol.getNick(), protocol.getLogin(), "", channel.getName(), action);
        return true;
    }
}
