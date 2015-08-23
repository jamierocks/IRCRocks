package uk.jamierocks.ircrocks.command;

import com.sk89q.intake.CommandException;
import com.sk89q.intake.context.CommandLocals;
import com.sk89q.intake.util.auth.AuthorizationException;
import org.spacehq.iirc.IrcChannel;
import org.spacehq.iirc.html.HtmlColor;
import org.spacehq.iirc.html.HtmlEscaping;

public class HelpCommand extends BaseCommand {

    @Override
    public boolean call(String arguments, CommandLocals locals, String[] parentCommands)
            throws CommandException, AuthorizationException {
        IrcChannel channel = locals.get(IrcChannel.class);

        channel.outputMessage(HtmlColor.Blue + "Available commands:" + HtmlColor.End);
        channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/me <action> - ") + HtmlColor.End + HtmlColor.Red
                + "Sends an action message." + HtmlColor.End);
        channel.outputMessage(
                HtmlColor.Green + HtmlEscaping.escape("/op <nick> - ") + HtmlColor.End + HtmlColor.Red + "Ops a user."
                        + HtmlColor.End);
        channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/deop <nick> - ") + HtmlColor.End + HtmlColor.Red
                + "Deops a user." + HtmlColor.End);
        channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/voice <nick> - ") + HtmlColor.End + HtmlColor.Red
                + "Gives voice to a user." + HtmlColor.End);
        channel.outputMessage(
                HtmlColor.Green + HtmlEscaping.escape("/devoice <nick> - ") + HtmlColor.End + HtmlColor.Red
                        + "Takes voice from a user." + HtmlColor.End);
        channel.outputMessage(
                HtmlColor.Green + HtmlEscaping.escape("/ban <nick> - ") + HtmlColor.End + HtmlColor.Red + "Bans a user."
                        + HtmlColor.End);
        channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/unban <nick> - ") + HtmlColor.End + HtmlColor.Red
                + "Unbans a user." + HtmlColor.End);
        channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/kick <nick> - ") + HtmlColor.End + HtmlColor.Red
                + "Kicks a user." + HtmlColor.End);
        channel.outputMessage(
                HtmlColor.Green + HtmlEscaping.escape("/nick <newnick> - ") + HtmlColor.End + HtmlColor.Red
                        + "Changes your name." + HtmlColor.End);
        channel.outputMessage(
                HtmlColor.Green + HtmlEscaping.escape("/identify <password> - ") + HtmlColor.End + HtmlColor.Red
                        + "Identifies you to the IRC server." + HtmlColor.End);
        channel.outputMessage(
                HtmlColor.Green + HtmlEscaping.escape("/topic <newtopic> - ") + HtmlColor.End + HtmlColor.Red
                        + "Changes the channel topic." + HtmlColor.End);
        channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/mode <nick> - ") + HtmlColor.End + HtmlColor.Red
                + "Changes the mode of the channel." + HtmlColor.End);
        channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/invite <nick> - ") + HtmlColor.End + HtmlColor.Red
                + "Invites a user to the channel." + HtmlColor.End);
        channel.outputMessage(
                HtmlColor.Green + HtmlEscaping.escape("/join <channel> [key] - ") + HtmlColor.End + HtmlColor.Red
                        + "Joins a channel on the same network as the command was executed on." + HtmlColor.End);
        channel.outputMessage(
                HtmlColor.Green + HtmlEscaping.escape("/leave <channel> - ") + HtmlColor.End + HtmlColor.Red
                        + "Leaves the channel." + HtmlColor.End);
        return true;
    }
}
