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
package uk.jamierocks.ircrocks;

import com.sk89q.intake.CommandCallable;
import com.sk89q.intake.CommandException;
import com.sk89q.intake.context.CommandLocals;
import com.sk89q.intake.dispatcher.Dispatcher;
import com.sk89q.intake.dispatcher.SimpleDispatcher;
import com.sk89q.intake.util.auth.AuthorizationException;
import org.spacehq.iirc.IrcChannel;
import org.spacehq.iirc.IrcProtocol;
import uk.jamierocks.ircrocks.command.BanCommand;
import uk.jamierocks.ircrocks.command.DeopCommand;
import uk.jamierocks.ircrocks.command.DevoiceCommand;
import uk.jamierocks.ircrocks.command.HelpCommand;
import uk.jamierocks.ircrocks.command.IdentifyCommand;
import uk.jamierocks.ircrocks.command.InviteCommand;
import uk.jamierocks.ircrocks.command.JoinCommand;
import uk.jamierocks.ircrocks.command.KickCommand;
import uk.jamierocks.ircrocks.command.LeaveCommand;
import uk.jamierocks.ircrocks.command.MeCommand;
import uk.jamierocks.ircrocks.command.MessageCommand;
import uk.jamierocks.ircrocks.command.ModeCommand;
import uk.jamierocks.ircrocks.command.NickCommand;
import uk.jamierocks.ircrocks.command.OpCommand;
import uk.jamierocks.ircrocks.command.TopicCommand;
import uk.jamierocks.ircrocks.command.UnbanCommand;
import uk.jamierocks.ircrocks.command.VoiceCommand;

public class IrcCommands {

    private static Dispatcher dispatcher = new SimpleDispatcher();

    public static void init() {
        registerCommand(new HelpCommand(), "help");
        registerCommand(new MeCommand(), "me");
        registerCommand(new OpCommand(), "op");
        registerCommand(new DeopCommand(), "deop");
        registerCommand(new VoiceCommand(), "voice");
        registerCommand(new DevoiceCommand(), "devoice");
        registerCommand(new BanCommand(), "ban");
        registerCommand(new UnbanCommand(), "unban");
        registerCommand(new KickCommand(), "kick");
        registerCommand(new NickCommand(), "nick");
        registerCommand(new IdentifyCommand(), "identify");
        registerCommand(new TopicCommand(), "topic");
        registerCommand(new ModeCommand(), "mode");
        registerCommand(new InviteCommand(), "invite");
        registerCommand(new MessageCommand(), "msg");
        registerCommand(new JoinCommand(), "join");
        registerCommand(new LeaveCommand(), "leave");
    }

    public static void handleCommand(IrcChannel channel, IrcProtocol protocol, String command) {
        CommandLocals locals = new CommandLocals();
        locals.put(IrcChannel.class, channel);
        locals.put(IrcProtocol.class, protocol);
        try {
            dispatcher.call(command.substring(1, command.length()), locals, new String[0]);
        } catch (CommandException e) {
            e.printStackTrace();
        } catch (AuthorizationException e) {
            e.printStackTrace();
        }
    }

    private static void registerCommand(CommandCallable callable, String... alias) {
        dispatcher.registerCommand(callable, alias);
    }
}
