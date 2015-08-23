package uk.jamierocks.ircrocks.command;

import org.spacehq.iirc.IrcChannel;
import org.spacehq.iirc.IrcProtocol;

public class VoiceCommand extends BaseCommand {

    @Override
    public void call(IrcChannel channel, IrcProtocol protocol, String[] args) {
        if(args.length == 0) {
            printUsage(channel, "/voice <nick>");
            return;
        }

        protocol.voice(channel.getName(), args[0]);
    }
}
