package org.spacehq.iirc;

import java.util.List;

public interface IrcChannel {
    public String getName();

    public List<IrcUser> getUsers();

    public IrcUser getUser(String name);

    public boolean isOnline(String name);

    public void addUser(String name);

    public void removeUser(String name);

    public void outputMessage(String chat);
}
