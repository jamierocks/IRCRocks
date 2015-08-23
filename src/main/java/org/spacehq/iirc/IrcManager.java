package org.spacehq.iirc;

import java.util.List;

public interface IrcManager {
    List<IrcChannel> getChannels(IrcProtocol protocol);

    IrcChannel getChannel(IrcProtocol protocol, String name);

    void addChannel(IrcProtocol protocol, String name);

    void removeChannels(IrcProtocol protocol);

    void removeChannel(IrcProtocol protocol, String name);
}
