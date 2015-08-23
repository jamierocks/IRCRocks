package org.spacehq.iirc;

import org.jibble.pircbot.DccChat;
import org.jibble.pircbot.DccFileTransfer;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.spacehq.iirc.html.HtmlColor;
import org.spacehq.iirc.html.HtmlEscaping;
import org.spacehq.iirc.util.Constants;

import javax.swing.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IrcProtocol extends PircBot {
    private static final Pattern MODE_PATTERN = Pattern.compile(":([A-Za-z0-9.]+) MODE ([A-Za-z0-9#]+) ([A-Za-z0-9+-:]+)");

    private IrcManager manager;
    private String host;
    private int port;

    public IrcProtocol(IrcManager manager, String host, int port, String password, String name) throws Exception {
        this.manager = manager;
        this.host = host;
        this.port = port;
        this.setName(name);
        this.setLogin(name);
        this.setAutoNickChange(true);
        this.setVersion(Constants.getVersion());
        if(password != null && !password.isEmpty()) {
            this.connect(this.host, this.port, password);
        } else {
            this.connect(this.host, this.port);
        }
    }

    public String getHostName() {
        return this.host;
    }

    public int getHostPort() {
        return this.port;
    }

    @Override
    protected void handleLine(String line) {
        Matcher matcher = MODE_PATTERN.matcher(line);
        if(matcher.find()) {
            String source = matcher.group(1);
            String target = matcher.group(2);
            String mode = matcher.group(3);
            if(mode.startsWith(":")) {
                mode = mode.substring(1);
            }

            if(target.startsWith("#")) {
                this.onMode(target, source, mode);
            } else {
                this.onUserMode(target, source, mode);
            }
        } else {
            super.handleLine(line);
        }
    }

    @Override
    protected void onConnect() {
        this.manager.getChannel(this, this.host).outputMessage(HtmlColor.Blue + "Connected to " + HtmlColor.End + HtmlColor.Green + this.host + ":" + this.port + HtmlColor.End);
    }

    @Override
    protected void onDisconnect() {
        this.manager.removeChannels(this);
    }

    @Override
    protected void onServerResponse(int code, String response) {
        if(code == 372) {
            this.manager.getChannel(this, this.host).outputMessage(HtmlColor.Blue + HtmlEscaping.escape(response.substring(response.indexOf(":") + 1)) + HtmlColor.End);
        }
    }

    @Override
    protected void onUserList(String channel, User[] users) {
        IrcChannel c = this.manager.getChannel(this, channel);
        for(User user : users) {
            c.addUser(user.getNick());
        }
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        this.receivedMessage(channel, sender, message);
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        IrcChannel c = this.manager.getChannel(this, sender);
        c.addUser(this.getNick());
        c.addUser(sender);
        this.receivedMessage(sender, sender, message);
    }

    private void receivedMessage(String channel, String sender, String message) {
        boolean mentioned = false;
        for(String word : message.split(" ")) {
            if(word.replaceAll("\\p{Punct}", "").equalsIgnoreCase(this.getNick())) {
                mentioned = true;
            }
        }

        this.manager.getChannel(this, channel).outputMessage((mentioned ? HtmlColor.Blue.toString() : "") + HtmlColor.Green + sender + HtmlColor.End + ": " + HtmlEscaping.escape(message) + (mentioned ? HtmlColor.End.toString() : ""));
    }

    @Override
    public void onAction(String sender, String login, String hostname, String target, String action) {
        this.manager.getChannel(this, target).outputMessage(HtmlColor.Green + " * " + sender + HtmlColor.End + " " + HtmlColor.Red + HtmlEscaping.escape(action) + HtmlColor.End);
    }

    @Override
    protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
        this.manager.getChannel(this, this.host).outputMessage(HtmlColor.Red + HtmlEscaping.escape(notice) + HtmlColor.End);
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        IrcChannel c = this.manager.getChannel(this, channel);
        c.addUser(sender);
        c.outputMessage(HtmlColor.Green + sender + " (" + login + "@" + hostname + ") joined " + channel + "." + HtmlColor.End);
    }

    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
        IrcChannel c = this.manager.getChannel(this, channel);
        c.addUser(sender);
        c.outputMessage(HtmlColor.Red + sender + " (" + login + "@" + hostname + ") left " + channel + "." + HtmlColor.End);
        if(sender.equals(this.getNick())) {
            this.manager.removeChannel(this, channel);
        }
    }

    @Override
    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        for(IrcChannel c : this.manager.getChannels(this)) {
            if(c.isOnline(oldNick)) {
                c.removeUser(oldNick);
                c.addUser(newNick);
                c.outputMessage(HtmlColor.Green + oldNick + " (" + login + "@" + hostname + ")" + HtmlColor.End + HtmlColor.Orange + " has changed their nick to " + HtmlColor.End + HtmlColor.Green + newNick + HtmlColor.End + HtmlColor.Orange + "." + HtmlColor.End);
            }
        }
    }

    @Override
    protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
        IrcChannel c = this.manager.getChannel(this, channel);
        c.removeUser(recipientNick);
        c.outputMessage(HtmlColor.Green + kickerNick + " (" + kickerLogin + "@" + kickerHostname + ")" + HtmlColor.End + HtmlColor.Red + " has kicked " + HtmlColor.End + HtmlColor.Green + recipientNick + HtmlColor.End + HtmlColor.Red + ". (" + HtmlEscaping.escape(reason) + ")" + HtmlColor.End);
    }

    @Override
    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        for(IrcChannel c : this.manager.getChannels(this)) {
            if(c.isOnline(sourceNick)) {
                c.removeUser(sourceNick);
                c.outputMessage(HtmlColor.Red + sourceNick + " (" + sourceNick + "@" + sourceNick + ") quit. (" + HtmlEscaping.escape(reason) + ")" + HtmlColor.End);
            }
        }
    }

    @Override
    protected void onTopic(String channel, String topic, String setBy, long date, boolean changed) {
        if(changed) {
            this.manager.getChannel(this, channel).outputMessage(HtmlColor.Green + setBy + HtmlColor.End + HtmlColor.Orange + " set topic of " + HtmlColor.End + HtmlColor.Green + channel + HtmlColor.End + HtmlColor.Orange + " to \"" + HtmlColor.End + HtmlColor.Green + topic + HtmlColor.End + HtmlColor.Orange + "\"." + HtmlColor.End);
        } else {
            this.manager.getChannel(this, channel).outputMessage(HtmlColor.Green + "Topic set by " + setBy + " (" + new Date(date).toString() + "): " + topic + "." + HtmlColor.End);
        }
    }

    @Override
    protected void onChannelInfo(String channel, int userCount, String topic) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Red + String.valueOf(userCount) + HtmlColor.End + HtmlColor.Green + " user" + (userCount > 1 ? "s" : "") + " are currently in " + channel + ". Topic: " + topic + HtmlColor.End);
    }

    private void onMode(String channel, String sourceNick, String mode) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Green + "Mode of " + channel + " set to " + mode + " by " + sourceNick + "." + HtmlColor.End);
    }

    private void onUserMode(String targetNick, String sourceNick, String mode) {
        this.manager.getChannel(this, this.host).outputMessage(HtmlColor.Green + "Mode of " + targetNick + " set to " + mode + " by " + sourceNick + "." + HtmlColor.End);
    }

    @Override
    protected void onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Green + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") opped " + recipient + "." + HtmlColor.End);
    }

    @Override
    protected void onDeop(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Red + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") deopped " + recipient + "." + HtmlColor.End);
    }

    @Override
    protected void onVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Green + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") gave voice to " + recipient + "." + HtmlColor.End);
    }

    @Override
    protected void onDeVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Red + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") took voice from " + recipient + "." + HtmlColor.End);
    }

    @Override
    protected void onSetChannelKey(String channel, String sourceNick, String sourceLogin, String sourceHostname, String key) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Green + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") set channel key to \"" + key + "\"." + HtmlColor.End);
    }

    @Override
    protected void onRemoveChannelKey(String channel, String sourceNick, String sourceLogin, String sourceHostname, String key) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Red + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") removed channel key." + HtmlColor.End);
    }

    @Override
    protected void onSetChannelLimit(String channel, String sourceNick, String sourceLogin, String sourceHostname, int limit) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Green + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") set channel limit to " + limit + "." + HtmlColor.End);
    }

    @Override
    protected void onRemoveChannelLimit(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Red + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") removed channel limit." + HtmlColor.End);
    }

    @Override
    protected void onSetChannelBan(String channel, String sourceNick, String sourceLogin, String sourceHostname, String hostmask) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Green + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") channel-banned " + hostmask + "." + HtmlColor.End);
    }

    @Override
    protected void onRemoveChannelBan(String channel, String sourceNick, String sourceLogin, String sourceHostname, String hostmask) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Red + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") un-channel-banned " + hostmask + "." + HtmlColor.End);
    }

    @Override
    protected void onSetTopicProtection(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Green + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") set channel as having topic protection." + HtmlColor.End);
    }

    @Override
    protected void onRemoveTopicProtection(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Red + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") set channel as having no topic protection." + HtmlColor.End);
    }

    @Override
    protected void onSetNoExternalMessages(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Green + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") set channel as having no external messages." + HtmlColor.End);
    }

    @Override
    protected void onRemoveNoExternalMessages(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Red + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") set channel as having external messages." + HtmlColor.End);
    }

    @Override
    protected void onSetInviteOnly(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Green + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") set channel as invite-only." + HtmlColor.End);
    }

    @Override
    protected void onRemoveInviteOnly(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Red + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") set channel as non-invite-only." + HtmlColor.End);
    }

    @Override
    protected void onSetModerated(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Green + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") set channel as moderated." + HtmlColor.End);
    }

    @Override
    protected void onRemoveModerated(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Red + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") set channel as non-moderated." + HtmlColor.End);
    }

    @Override
    protected void onSetPrivate(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Green + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") set channel as private." + HtmlColor.End);
    }

    @Override
    protected void onRemovePrivate(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Red + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") set channel as non-private." + HtmlColor.End);
    }

    @Override
    protected void onSetSecret(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Green + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") set channel as secret." + HtmlColor.End);
    }

    @Override
    protected void onRemoveSecret(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        this.manager.getChannel(this, channel).outputMessage(HtmlColor.Red + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") set channel as non-secret." + HtmlColor.End);
    }

    @Override
    protected void onInvite(String targetNick, final String sourceNick, String sourceLogin, String sourceHostname, final String channel) {
        if(targetNick.equals(this.getNick())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int ret = JOptionPane.showConfirmDialog(null, sourceNick + " would like you to join " + channel + ". Accept invitation?", "Channel Invitation", JOptionPane.YES_NO_OPTION);
                    if(ret == JOptionPane.YES_OPTION) {
                        joinChannel(channel);
                    }
                }
            }, "InvitationThread").start();
        }
    }

    @Override
    protected void onIncomingFileTransfer(DccFileTransfer transfer) {
        // TODO
    }

    @Override
    protected void onFileTransferFinished(DccFileTransfer transfer, Exception e) {
        // TODO
    }

    @Override
    protected void onIncomingChatRequest(DccChat chat) {
        // TODO
    }
}
