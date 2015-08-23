package uk.jamierocks.ircrocks.command;

import org.spacehq.iirc.IrcChannel;
import org.spacehq.iirc.IrcProtocol;

public class JoinCommand extends BaseCommand {

    @Override
    public void call(IrcChannel channel, IrcProtocol protocol, String[] args) {
        if(args.length == 0) {
            printUsage(channel, "/join <channel> [key]");
            return;
        }

        if(args.length > 1) {
            protocol.joinChannel(args[0], args[1]);
        } else {
            protocol.joinChannel(args[0]);
        }
    }
}
