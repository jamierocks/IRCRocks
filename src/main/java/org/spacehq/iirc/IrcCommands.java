package org.spacehq.iirc;

import org.spacehq.iirc.html.HtmlColor;
import org.spacehq.iirc.html.HtmlEscaping;

import java.util.Arrays;

public class IrcCommands {
	public static void handleCommand(IrcChannel channel, IrcProtocol protocol, String text) {
		int space = text.indexOf(" ");
		String cmd = text.toLowerCase().substring(1, space != -1 ? space : text.length());
		String args[] = space != -1 ? text.substring(space + 1).split(" ") : new String[0];
		if(cmd.equals("help")) {
			channel.outputMessage(HtmlColor.Blue + "Available commands:" + HtmlColor.End);
			channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/me <action> - ") + HtmlColor.End + HtmlColor.Red + "Sends an action message." + HtmlColor.End);
			channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/op <nick> - ") + HtmlColor.End + HtmlColor.Red + "Ops a user." + HtmlColor.End);
			channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/deop <nick> - ") + HtmlColor.End + HtmlColor.Red + "Deops a user." + HtmlColor.End);
			channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/voice <nick> - ") + HtmlColor.End + HtmlColor.Red + "Gives voice to a user." + HtmlColor.End);
			channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/devoice <nick> - ") + HtmlColor.End + HtmlColor.Red + "Takes voice from a user." + HtmlColor.End);
			channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/ban <nick> - ") + HtmlColor.End + HtmlColor.Red + "Bans a user." + HtmlColor.End);
			channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/unban <nick> - ") + HtmlColor.End + HtmlColor.Red + "Unbans a user." + HtmlColor.End);
			channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/kick <nick> - ") + HtmlColor.End + HtmlColor.Red + "Kicks a user." + HtmlColor.End);
			channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/nick <newnick> - ") + HtmlColor.End + HtmlColor.Red + "Changes your name." + HtmlColor.End);
			channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/identify <password> - ") + HtmlColor.End + HtmlColor.Red + "Identifies you to the IRC server." + HtmlColor.End);
			channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/topic <newtopic> - ") + HtmlColor.End + HtmlColor.Red + "Changes the channel topic." + HtmlColor.End);
			channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/mode <nick> - ") + HtmlColor.End + HtmlColor.Red + "Changes the mode of the channel." + HtmlColor.End);
			channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/invite <nick> - ") + HtmlColor.End + HtmlColor.Red + "Invites a user to the channel." + HtmlColor.End);
			channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/join <channel> [key] - ") + HtmlColor.End + HtmlColor.Red + "Joins a channel on the same network as the command was executed on." + HtmlColor.End);
			channel.outputMessage(HtmlColor.Green + HtmlEscaping.escape("/leave <channel> - ") + HtmlColor.End + HtmlColor.Red + "Leaves the channel." + HtmlColor.End);
		} else if(cmd.equals("me")) {
			if(args.length == 0) {
				printUsage(channel, "/me <action>");
				return;
			}

			String action = join(args, " ");
			protocol.sendAction(channel.getName(), action);
			protocol.onAction(protocol.getNick(), protocol.getLogin(), "", channel.getName(), action);
		} else if(cmd.equals("op")) {
			if(args.length == 0) {
				printUsage(channel, "/op <nick>");
				return;
			}

			protocol.op(channel.getName(), args[0]);
		} else if(cmd.equals("deop")) {
			if(args.length == 0) {
				printUsage(channel, "/deop <nick>");
				return;
			}

			protocol.deOp(channel.getName(), args[0]);
		} else if(cmd.equals("voice")) {
			if(args.length == 0) {
				printUsage(channel, "/voice <nick>");
				return;
			}

			protocol.voice(channel.getName(), args[0]);
		} else if(cmd.equals("devoice")) {
			if(args.length == 0) {
				printUsage(channel, "/devoice <nick>");
				return;
			}

			protocol.deVoice(channel.getName(), args[0]);
		} else if(cmd.equals("ban")) {
			if(args.length == 0) {
				printUsage(channel, "/ban <nick>");
				return;
			}

			protocol.ban(channel.getName(), args[0]);
		} else if(cmd.equals("unban")) {
			if(args.length == 0) {
				printUsage(channel, "/unban <nick>");
				return;
			}

			protocol.unBan(channel.getName(), args[0]);
		} else if(cmd.equals("kick")) {
			if(args.length == 0) {
				printUsage(channel, "/kick <nick>");
				return;
			}

			protocol.kick(channel.getName(), args[0]);
		} else if(cmd.equals("nick")) {
			if(args.length == 0) {
				printUsage(channel, "/nick <newnick>");
				return;
			}

			protocol.changeNick(args[0]);
		} else if(cmd.equals("identify")) {
			if(args.length == 0) {
				printUsage(channel, "/identify <password>");
				return;
			}

			protocol.identify(args[0]);
		} else if(cmd.equals("topic")) {
			if(args.length == 0) {
				printUsage(channel, "/topic <newtopic>");
				return;
			}

			protocol.setTopic(channel.getName(), args[0]);
		} else if(cmd.equals("mode")) {
			if(args.length == 0) {
				printUsage(channel, "/mode <mode>");
				return;
			}

			protocol.setMode(channel.getName(), args[0]);
		} else if(cmd.equals("invite")) {
			if(args.length == 0) {
				printUsage(channel, "/invite <nick>");
				return;
			}

			protocol.sendInvite(args[0], channel.getName());
		} else if(cmd.equals("msg")) {
			if(args.length < 2) {
				printUsage(channel, "/msg <nick> <message>");
				return;
			}

			protocol.sendMessage(args[0], join(Arrays.copyOfRange(args, 1, args.length), " "));
		} else if(cmd.equals("join")) {
			if(args.length == 0) {
				printUsage(channel, "/join <channel> [key]");
				return;
			}

			if(args.length > 1) {
				protocol.joinChannel(args[0], args[1]);
			} else {
				protocol.joinChannel(args[0]);
			}
		} else if(cmd.equals("leave")) {
			if(args.length == 0) {
				printUsage(channel, "/leave <channel>");
				return;
			}

			protocol.partChannel(args[0]);
		}
	}

	private static void printUsage(IrcChannel channel, String usage) {
		channel.outputMessage(HtmlColor.Red + HtmlEscaping.escape("Usage: " + usage) + HtmlColor.End);
	}

	private static String join(String strs[], String separator) {
		StringBuilder str = new StringBuilder();
		boolean first = true;
		for(String s : strs) {
			if(!first) {
				str.append(separator);
			}

			first = false;
			str.append(s);
		}

		return str.toString();
	}
}
