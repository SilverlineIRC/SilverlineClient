/*
 * Handles most of the outgoing messages to the IRCCloud API
 */
package api;

import connection.Session;
import irc.IrcBuffer;
import irc.IrcNetwork;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.http.impl.client.DefaultHttpClient;
import thread.RequestThread;

public class OutgoingMessages {

    private static DefaultHttpClient httpclient;
    private static APICallbacklistener callbacklistener;

    /**
     * Sends a message to IRC
     *
     * @param channel Target channel
     * @param message The message
     */
    public static void sendMessage(IrcBuffer channel, String message) {
        try {
            message = URLEncoder.encode(message, "UTF-8");
            RequestThread saythread = new RequestThread(callbacklistener, httpclient, "POST", APIConstants.RECEIVER_CLASS_TYPE_SAY_RESPONSE, APIConstants.SAY_URL);
            saythread.addStringEntity("session=" + Session.getSessionID() + "&cid=" + channel.cid + "&to=" + channel.getBufferName() + "&msg=" + message);
            saythread.start();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Changes nick on specified network
     *
     * @param Connection ID for the network you want to change nick in
     * @param nick The new nickname
     */
    public static void changeNick(String cid, String nick) {
        RequestThread nickthread = new RequestThread(callbacklistener, httpclient, "POST", APIConstants.RECEIVER_CLASS_TYPE_NICKCHANGE_RESPONSE, APIConstants.NICKCHANGE_URL);
        nickthread.addStringEntity("session=" + Session.getSessionID() + "&cid=" + cid + "&nick=" + nick);
        nickthread.start();
    }

    /**
     * Joins a IRC-channel
     *
     * @param network Specifies the network
     * @param channelname Name for the channel you want to join
     * @param key Optional channel key/password
     */
    public static void joinChannel(IrcNetwork network, String channelname, String key) {
        try {
            RequestThread jointhread = new RequestThread(callbacklistener, httpclient, "POST", APIConstants.RECEIVER_CLASS_TYPE_JOINCHANNEL_RESPONSE, APIConstants.JOINCHANNEL_URL);
            String channel = URLEncoder.encode(channelname, "UTF-8");
            if (!key.isEmpty()) {
                String password = URLEncoder.encode(key, "UTF-8");
                jointhread.addStringEntity("session=" + Session.getSessionID() + "&cid=" + network.cid + "&channel=" + channel + "&key=" + password);
            }
            else {
                jointhread.addStringEntity("session=" + Session.getSessionID() + "&cid=" + network.cid + "&channel=" + channel);
            }
            jointhread.start();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Parts a IRC buffer
     *
     * @param channel Buffer to be parted from
     */
    public static void partChannel(IrcBuffer channel) {
        RequestThread partthread = new RequestThread(callbacklistener, httpclient, "POST", APIConstants.RECEIVER_CLASS_TYPE_PARTCHANNEL_RESPONSE, APIConstants.PARTCHANNEL_URL);
        String cid = channel.cid;
        String name = channel.getBufferName();
        partthread.addStringEntity("session=" + Session.getSessionID() + "&cid=" + cid + "&channel=" + name + "&msg=Silverline!");
        partthread.start();
    }

    /**
     * Archives a IRC buffer
     *
     * @param channel Buffer to be archived
     */
    public static void archiveChannel(IrcBuffer channel) {
        RequestThread archivethread = new RequestThread(callbacklistener, httpclient, "POST", APIConstants.RECEIVER_CLASS_TYPE_ARCHIVE_RESPONSE, APIConstants.ARCHIVE_URL);
        archivethread.addStringEntity("session=" + Session.getSessionID() + "&cid=" + channel.cid + "&id=" + channel.bid);
        archivethread.start();
    }

    /**
     * Unarchives a IRC buffer
     *
     * @param channel Buffer to be Unarchived
     */
    public static void unarchiveChannel(IrcBuffer channel) {
        RequestThread archivethread = new RequestThread(callbacklistener, httpclient, "POST", APIConstants.RECEIVER_CLASS_TYPE_UNARCHIVE_RESPONSE, APIConstants.UNARCHIVE_URL);
        archivethread.addStringEntity("session=" + Session.getSessionID() + "&cid=" + channel.cid + "&id=" + channel.bid);
        archivethread.start();
    }

    /**
     * Delete a IRC buffer
     *
     * @param channel Buffer to be deleted
     */
    public static void deleteChannel(IrcBuffer channel) {
        RequestThread deletethread = new RequestThread(callbacklistener, httpclient, "POST", APIConstants.RECEIVER_CLASS_TYPE_BUFFERDELETE_RESPONSE, APIConstants.BUFFERDELETE_URL);
        deletethread.addStringEntity("session=" + Session.getSessionID() + "&cid=" + channel.cid + "&id=" + channel.bid);
        deletethread.start();
    }

    /**
     * Requests a whois for the given user
     *
     * @param cid Network connection ID for the user
     * @param nick User nickname for the whois
     */
    public static void requestWhois(String cid, String nick) {
        RequestThread networkreconnect = new RequestThread(callbacklistener, httpclient, "POST", APIConstants.RECEIVER_CLASS_TYPE_WHOIS_RESPONSE, APIConstants.WHOIS_URL);
        networkreconnect.addStringEntity("session=" + Session.getSessionID() + "&cid=" + cid + "&nick=" + nick);
        networkreconnect.start();
    }

    /**
     * Sets Httpclient
     *
     * @param client Htppclient to be used for the messages
     */
    public static void setHttpClient(DefaultHttpClient client) {
        httpclient = client;
    }

    /**
     * Sets callback listener
     *
     * @param listener Callback listener to be used
     */
    public static void setCallbacklistener(APICallbacklistener listener) {
        callbacklistener = listener;
    }
}
