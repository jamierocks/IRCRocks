package org.spacehq.iirc;

import java.util.List;

public interface IrcChannel {
    String getName();

    List<IrcUser> getUsers();

    IrcUser getUser(String name);

    boolean isOnline(String name);

    void addUser(String name);

    void removeUser(String name);

    void outputMessage(String chat);
}
