<b><center><h1>IIRC</h></center></b>
==========

![Screenshot](http://i.imgur.com/BBOuwvI.png)


IIRC is a lightweight IRC client written in Java using Pircbot. Currently, most things are supported by this client, with the exception of anything DCC-related.


<b>Using the Client</b>
--------

The main screen of the client is the login screen. The login screen contains 4 text fields, one each for the hostname, password, nickname, and channels to connect using. The password box is optional. In the channel box, you may either put a single channel name to connect to or a comma-separated list of channels to connect to. Additionally, a channel's name can be suffixed with a colon and the channel's key if a key is necessary for that channel.

After logging into a server and channel(s), tabs will open up for the server's internal messages and each of the channels you've connected to. You can chat normally in these tabs and you can type /help to see a list of available client commands. If you want to connect to more channels or servers, you can go back to the login tab and connect like before. They will open in new tabs without closing your previous tabs.


<b>Building the Source</b>
--------

IIRC uses Maven to manage dependencies. Simply run 'mvn clean install' in the source's directory.
Snapshots (if any exist) can be downloaded <b>[here](http://repo.spacehq.org/content/repositories/snapshots/org/spacehq/iirc)</b>.
Releases (if any exist) can be downloaded <b>[here](http://repo.spacehq.org/content/repositories/release/org/spacehq/iirc)</b>.


<b>License</b>
---------

IIRC is licensed under the <b>[MIT license](http://www.opensource.org/licenses/mit-license.html)</b>.
