/*
 * This file is part of IRCRocks, licensed under the MIT License (MIT).
 *
 * Copyright (C) 2014-2015 Steveice10
 * Copyright (c) 2015, Jamie Mansfield <https://www.jamierocks.uk/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package uk.jamierocks.ircrocks.command;

import org.spacehq.iirc.IrcChannel;
import org.spacehq.iirc.IrcProtocol;
import org.spacehq.iirc.html.HtmlColor;
import org.spacehq.iirc.html.HtmlEscaping;

public class HelpCommand extends BaseCommand {

    @Override
    public void call(IrcChannel channel, IrcProtocol protocol, String[] args) {
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
    }
}
