package uk.jamierocks.ircrocks.command;

import org.spacehq.iirc.IrcChannel;
import org.spacehq.iirc.IrcProtocol;

public class MeCommand extends BaseCommand {

    @Override
    public void call(IrcChannel channel, IrcProtocol protocol, String[] args) {
        if(args.length == 0) {
            printUsage(channel, "/me <action>");
            return;
        }

        String action = join(args, " ");
        protocol.sendAction(channel.getName(), action);
        protocol.onAction(protocol.getNick(), protocol.getLogin(), "", channel.getName(), action);
    }
}
