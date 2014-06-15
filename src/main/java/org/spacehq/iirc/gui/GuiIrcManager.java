package org.spacehq.iirc.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.spacehq.iirc.IrcChannel;
import org.spacehq.iirc.IrcManager;
import org.spacehq.iirc.IrcProtocol;
import org.spacehq.iirc.util.PomUtil;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiIrcManager extends JFrame implements IrcManager {
	public static void main(String[] args) {
		new GuiIrcManager().setVisible(true);
	}

	private JTabbedPane tabs;
	private Map<IrcProtocol, Map<String, GuiIrcChannel>> channels = new HashMap<IrcProtocol, Map<String, GuiIrcChannel>>();

	public GuiIrcManager() {
		super("IIRC " + PomUtil.getVersion());
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch(UnsupportedLookAndFeelException e) {
		}

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1, false, false));
		contentPane.setBackground(new Color(-10066330));
		contentPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null, 0, 0, null, new Color(-16777216)));
		this.tabs = new JTabbedPane();
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new GridLayoutManager(7, 4, new Insets(0, 0, 0, 0), -1, -1, false, false));
		loginPanel.setBackground(new Color(-3355444));
		loginPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, 0, 0, null, null));
		final JTextField hostname = new JTextField();
		final JPasswordField password = new JPasswordField();
		final JTextField nickname = new JTextField();
		final JTextField channels = new JTextField();
		JLabel hostnameLabel = new JLabel();
		hostnameLabel.setHorizontalAlignment(4);
		hostnameLabel.setText("Hostname");
		JLabel passwordLabel = new JLabel();
		passwordLabel.setHorizontalAlignment(4);
		passwordLabel.setText("Password");
		JLabel nicknameLabel = new JLabel();
		nicknameLabel.setHorizontalAlignment(4);
		nicknameLabel.setText("Nickname");
		JLabel channelsLabel = new JLabel();
		channelsLabel.setText("Channels");
		channelsLabel.setHorizontalTextPosition(4);
		JButton joinButton = new JButton();
		joinButton.setText("Join");
		joinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String hostData[] = hostname.getText().split(":");
				String host = hostData[0];
				int port = 6667;
				if(hostData.length > 1) {
					try {
						port = Integer.parseInt(hostData[1]);
					} catch(NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Invalid port specified.", "Could not connect to IRC server", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}

				String pass = String.valueOf(password.getPassword());
				String nick = nickname.getText();
				String chnls[] = channels.getText().split(",");
				if(host.length() == 0 || nick.length() == 0 || chnls.length == 0 || chnls[0].length() == 0) {
					JOptionPane.showMessageDialog(null, "Please fill out all of the required fields.", "Could not connect to IRC server", JOptionPane.ERROR_MESSAGE);
					return;
				}

				hostname.setText("");
				password.setText("");
				nickname.setText("");
				channels.setText("");
				IrcProtocol protocol = getProtocol(host, port);
				if(protocol != null) {
					for(String channel : chnls) {
						protocol.joinChannel(channel);
					}
				} else {
					connect(host, port, pass, nick, chnls);
				}
			}
		});

		loginPanel.add(channels, new GridConstraints(5, 2, 1, 1, 8, 1, 6, 0, null, new Dimension(150, -1), null));
		loginPanel.add(channelsLabel, new GridConstraints(5, 1, 1, 1, 8, 0, 0, 0, null, null, null, 1));
		loginPanel.add(nickname, new GridConstraints(4, 2, 1, 1, 8, 1, 6, 0, null, new Dimension(150, -1), null));
		loginPanel.add(nicknameLabel, new GridConstraints(4, 1, 1, 1, 8, 0, 0, 0, null, null, null, 1));
		loginPanel.add(password, new GridConstraints(3, 2, 1, 1, 8, 1, 6, 0, null, new Dimension(150, -1), null));
		loginPanel.add(passwordLabel, new GridConstraints(3, 1, 1, 1, 8, 0, 0, 0, null, null, null, 1));
		loginPanel.add(hostname, new GridConstraints(2, 2, 1, 1, 8, 1, 6, 0, null, new Dimension(150, -1), null));
		loginPanel.add(hostnameLabel, new GridConstraints(2, 1, 1, 1, 8, 0, 0, 0, null, null, null, 1));
		loginPanel.add(joinButton, new GridConstraints(6, 2, 1, 1, 0, 1, 3, 0, null, null, null));
		loginPanel.add(new Spacer(), new GridConstraints(3, 3, 1, 1, 0, 1, 6, 1, null, null, null));
		loginPanel.add(new Spacer(), new GridConstraints(4, 3, 1, 1, 0, 1, 6, 1, null, null, null));
		loginPanel.add(new Spacer(), new GridConstraints(0, 2, 2, 1, 0, 2, 1, 6, null, null, null));
		loginPanel.add(new Spacer(), new GridConstraints(6, 2, 1, 1, 0, 2, 1, 6, null, null, null));
		loginPanel.add(new Spacer(), new GridConstraints(5, 1, 2, 1, 0, 2, 1, 6, null, null, null));
		loginPanel.add(new Spacer(), new GridConstraints(2, 3, 1, 1, 0, 1, 6, 1, null, null, null));
		loginPanel.add(new Spacer(), new GridConstraints(2, 0, 1, 1, 0, 1, 6, 1, null, null, null));
		loginPanel.add(new Spacer(), new GridConstraints(3, 0, 1, 1, 0, 1, 6, 1, null, null, null));
		loginPanel.add(new Spacer(), new GridConstraints(4, 0, 1, 1, 0, 1, 6, 1, null, null, null));
		this.tabs.addTab("Login", null, loginPanel, null);
		contentPane.add(this.tabs, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, null, new Dimension(1200, 675), null));
		this.setContentPane(contentPane);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}

	public void connect(final String hostname, final int port, final String password, final String nickname, final String initialChannels[]) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				IrcProtocol protocol;
				try {
					protocol = new IrcProtocol(GuiIrcManager.this, hostname, port, password, nickname);
				} catch(Exception e) {
					StringWriter writer = new StringWriter();
					e.printStackTrace(new PrintWriter(writer));
					JOptionPane.showMessageDialog(null, writer.toString(), "Could not connect to IRC server", JOptionPane.ERROR_MESSAGE);
					return;
				}

				for(String channel : initialChannels) {
					if(channel.contains(":")) {
						channel = channel.substring(0, channel.indexOf(":"));
						String key = channel.substring(channel.indexOf(":") + 1, channel.length());
						protocol.joinChannel(channel, key);
					} else {
						protocol.joinChannel(channel);
					}
				}
			}
		}, "IrcConnectThread").start();
	}

	protected IrcProtocol getProtocol(String hostname, int port) {
		for(IrcProtocol protocol : this.channels.keySet()) {
			if(protocol.getHostName().equals(hostname) && protocol.getHostPort() == port) {
				return protocol;
			}
		}

		return null;
	}

	@Override
	public List<IrcChannel> getChannels(IrcProtocol protocol) {
		if(!this.channels.containsKey(protocol)) {
			return new ArrayList<IrcChannel>();
		}

		return new ArrayList<IrcChannel>(this.channels.get(protocol).values());
	}

	@Override
	public IrcChannel getChannel(IrcProtocol protocol, String name) {
		if(!this.channels.containsKey(protocol)) {
			this.channels.put(protocol, new HashMap<String, GuiIrcChannel>());
		}

		Map<String, GuiIrcChannel> channels = this.channels.get(protocol);
		if(!channels.containsKey(name)) {
			this.addChannel(protocol, name);
		}

		return channels.get(name);
	}

	@Override
	public void addChannel(IrcProtocol protocol, String name) {
		if(!this.channels.containsKey(protocol)) {
			this.channels.put(protocol, new HashMap<String, GuiIrcChannel>());
		}

		final Map<String, GuiIrcChannel> channels = this.channels.get(protocol);
		if(!channels.containsKey(name)) {
			final GuiIrcChannel channel = new GuiIrcChannel(name, this, protocol);
			channels.put(name, channel);
			this.tabs.addTab(name, null, channel, null);

			int index = this.tabs.indexOfTab(name);
			JPanel tab = new JPanel(new GridBagLayout());
			tab.setOpaque(false);
			JLabel title = new JLabel(name + " ");
			JLabel close = new JLabel("x");
			GridBagConstraints grid = new GridBagConstraints();
			grid.gridx = 0;
			grid.gridy = 0;
			grid.ipadx = 5;
			grid.weightx = 1;
			tab.add(title, grid);
			grid.gridx++;
			grid.weightx = 0;
			tab.add(close, grid);
			this.tabs.setTabComponentAt(index, tab);
			close.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton() == MouseEvent.BUTTON1) {
						removeChannel(channel.getProtocol(), channel.getName());
					}
				}
			});

			this.tabs.setSelectedIndex(index);
		}
	}

	@Override
	public void removeChannels(IrcProtocol protocol) {
		protocol.disconnect();
		if(this.channels.containsKey(protocol)) {
			Map<String, GuiIrcChannel> channels = this.channels.get(protocol);
			for(GuiIrcChannel channel : channels.values()) {
				this.tabs.remove(channel);
			}

			this.channels.remove(protocol);
		}
	}

	@Override
	public void removeChannel(IrcProtocol protocol, String name) {
		if(!this.channels.containsKey(protocol)) {
			return;
		}

		Map<String, GuiIrcChannel> channels = this.channels.get(protocol);
		if(channels.containsKey(name)) {
			GuiIrcChannel channel = channels.remove(name);
			this.tabs.remove(channel);
			if(channels.isEmpty() || channel.getName().equals(protocol.getHostName())) {
				this.removeChannels(protocol);
			}
		}
	}
}
