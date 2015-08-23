package uk.jamierocks.ircrocks.command;

import org.spacehq.iirc.IrcChannel;
import org.spacehq.iirc.IrcProtocol;

public class KickCommand extends BaseCommand {

    @Override
    public void call(IrcChannel channel, IrcProtocol protocol, String[] args) {
        if(args.length == 0) {
            printUsage(channel, "/kick <nick>");
            return;
        }

        protocol.kick(channel.getName(), args[0]);
    }
}
