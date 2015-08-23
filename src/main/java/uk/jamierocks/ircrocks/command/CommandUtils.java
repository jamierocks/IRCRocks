package uk.jamierocks.ircrocks.command;

import org.spacehq.iirc.IrcChannel;
import org.spacehq.iirc.html.HtmlColor;
import org.spacehq.iirc.html.HtmlEscaping;

public class CommandUtils {

    public static void printUsage(IrcChannel channel, String usage) {
        channel.outputMessage(HtmlColor.Red + HtmlEscaping.escape("Usage: " + usage) + HtmlColor.End);
    }

    public static String join(String strs[], String separator) {
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
