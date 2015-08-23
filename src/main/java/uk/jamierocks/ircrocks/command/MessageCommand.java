package uk.jamierocks.ircrocks.command;

import org.spacehq.iirc.IrcChannel;
import org.spacehq.iirc.IrcProtocol;

import java.util.Arrays;

public class MessageCommand extends BaseCommand {

    @Override
    public void call(IrcChannel channel, IrcProtocol protocol, String[] args) {
        if(args.length < 2) {
            printUsage(channel, "/msg <nick> <message>");
            return;
        }

        protocol.sendMessage(args[0], join(Arrays.copyOfRange(args, 1, args.length), " "));
    }
}
