package uk.jamierocks.ircrocks.command;

import com.sk89q.intake.CommandCallable;
import com.sk89q.intake.CommandException;
import com.sk89q.intake.Description;
import com.sk89q.intake.context.CommandLocals;
import com.sk89q.intake.util.auth.AuthorizationException;
import org.spacehq.iirc.IrcChannel;
import org.spacehq.iirc.IrcProtocol;
import org.spacehq.iirc.html.HtmlColor;
import org.spacehq.iirc.html.HtmlEscaping;

import java.util.List;

public abstract class BaseCommand implements CommandCallable {

    public abstract void call(IrcChannel channel, IrcProtocol protocol, String[] args);

    @Override
    public boolean call(String arguments, CommandLocals locals, String[] parentCommands)
            throws CommandException, AuthorizationException {
        this.call(locals.get(IrcChannel.class), locals.get(IrcProtocol.class), arguments.split(" "));
        return true;
    }

    @Override
    public List<String> getSuggestions(String arguments, CommandLocals locals) throws CommandException {
        return null;
    }

    @Override
    public Description getDescription() {
        return null;
    }

    @Override
    public boolean testPermission(CommandLocals locals) {
        return true;
    }

    public void printUsage(IrcChannel channel, String usage) {
        channel.outputMessage(HtmlColor.Red + HtmlEscaping.escape("Usage: " + usage) + HtmlColor.End);
    }

    public String join(String[] strs, String separator) {
        StringBuilder str = new StringBuilder();
        boolean first = true;
        for (String s : strs) {
            if (!first) {
                str.append(separator);
            }

            first = false;
            str.append(s);
        }

        return str.toString();
    }
}
