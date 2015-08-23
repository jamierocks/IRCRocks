package uk.jamierocks.ircrocks.command;

import org.spacehq.iirc.IrcChannel;
import org.spacehq.iirc.IrcProtocol;

public class LeaveCommand extends BaseCommand {

    @Override
    public void call(IrcChannel channel, IrcProtocol protocol, String[] args) {
        if(args.length == 0) {
            printUsage(channel, "/leave <channel>");
            return;
        }

        protocol.partChannel(args[0]);
    }
}
