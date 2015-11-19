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
package org.spacehq.iirc.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.spacehq.iirc.IrcChannel;
import org.spacehq.iirc.IrcManager;
import org.spacehq.iirc.IrcProtocol;
import org.spacehq.iirc.IrcUser;
import org.spacehq.iirc.html.HtmlColor;
import uk.jamierocks.ircrocks.IrcCommands;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiIrcChannel extends JPanel implements IrcChannel {
    private JTextField chatField;
    private HTMLDocument chatDoc;
    private HTMLEditorKit chat;
    private DefaultListModel<String> usrs;

    private String channel;
    private IrcProtocol protocol;
    private Map<String, IrcUser> users = new HashMap<String, IrcUser>();

    public GuiIrcChannel(String name, final IrcManager manager, final IrcProtocol proto) {
        this.channel = name;
        this.protocol = proto;
        this.chatField = new JTextField();
        this.chat = new HTMLEditorKit();
        this.chatDoc = new HTMLDocument();
        this.usrs = new DefaultListModel<String>();
        JTextPane chatText = new JTextPane();
        JScrollPane userListPane = new JScrollPane();
        final JList<String> userList = new JList<String>();
        final JScrollPane chatPane = new JScrollPane();
        chatText.setContentType("text/html");
        chatText.setEditable(false);
        chatText.setEditorKit(this.chat);
        chatText.setDocument(this.chatDoc);
        ((DefaultCaret) chatText.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        if(name.equals(proto.getHostName())) {
            chatText.setFont(new Font("Monospaced", chatText.getFont().getStyle(), chatText.getFont().getSize()));
        }

        userList.setModel(this.usrs);
        userListPane.setViewportView(userList);
        chatPane.setViewportView(chatText);
        this.setLayout(new GridLayoutManager(2, 6, new Insets(0, 0, 0, 0), -1, -1, false, false));
        this.setBackground(new java.awt.Color(-3355444));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, 0, 0, null, null));
        this.add(this.chatField, new GridConstraints(1, 0, 1, 6, 10, 1, 6, 0, null, new Dimension(150, -1), null));
        this.add(chatPane, new GridConstraints(0, 0, 1, 5, 0, 3, 7, 7, null, null, null));
        this.add(userListPane, new GridConstraints(0, 5, 1, 1, 0, 3, 1, 7, new Dimension(150, -1), new Dimension(150, -1), new Dimension(150, -1)));
        this.chatField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text = chatField.getText().trim();
                if(text.length() > 0) {
                    chatField.setText("");
                    if(text.startsWith("/")) {
                        IrcCommands.handleCommand(GuiIrcChannel.this, protocol, text);
                        return;
                    }

                    protocol.sendMessage(channel, text);
                    protocol.onMessage(channel, protocol.getNick(), protocol.getLogin(), "", text);
                }
            }
        });

        userList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if(e.isPopupTrigger()) {
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem item = new JMenuItem("Open private chat");
                    item.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            manager.addChannel(protocol, userList.getSelectedValue());
                        }
                    });

                    menu.add(item);
                    userList.setSelectedIndex(userList.locationToIndex(e.getPoint()));
                    menu.show(GuiIrcChannel.this, e.getLocationOnScreen().x, e.getY());
                }
            }
        });
    }

    public IrcProtocol getProtocol() {
        return this.protocol;
    }

    @Override
    public String getName() {
        return this.channel;
    }

    @Override
    public List<IrcUser> getUsers() {
        return new ArrayList<IrcUser>(this.users.values());
    }

    @Override
    public IrcUser getUser(String name) {
        return this.users.get(name);
    }

    @Override
    public boolean isOnline(String name) {
        return this.users.containsKey(name);
    }

    @Override
    public void addUser(String name) {
        if(!this.isOnline(name)) {
            this.users.put(name, new IrcUser(name));
            this.usrs.addElement(name);
        }
    }

    @Override
    public void removeUser(String name) {
        if(this.isOnline(name)) {
            this.usrs.removeElement(name);
            this.users.remove(name);
        }
    }

    @Override
    public void outputMessage(String chat) {
        StringBuilder build = new StringBuilder();
        boolean first = true;
        for(String word : chat.split(" ")) {
            if(!first) {
                build.append("&nbsp;");
            }

            first = false;
            if(word.replaceAll("\\p{Punct}", "").equals(this.protocol.getNick())) {
                build.append(HtmlColor.Green).append("<b>").append(word).append("</b>").append(HtmlColor.End);
            } else if(word.startsWith("#")) {
                build.append(HtmlColor.Green).append("<u>").append(word).append("</u>").append(HtmlColor.End);
            } else {
                build.append(word);
            }
        }

        try {
            this.chat.insertHTML(this.chatDoc, this.chatDoc.getLength(), build.toString() + "\n", 0, 0, null);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
