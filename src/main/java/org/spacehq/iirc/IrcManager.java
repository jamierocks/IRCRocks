package org.spacehq.iirc;

import java.util.List;

public interface IrcManager {
    public List<IrcChannel> getChannels(IrcProtocol protocol);

    public IrcChannel getChannel(IrcProtocol protocol, String name);

    public void addChannel(IrcProtocol protocol, String name);

    public void removeChannels(IrcProtocol protocol);

    public void removeChannel(IrcProtocol protocol, String name);
}
