/*
 * Responsible for making IRCCloud API connection and new network connections
 * 
 */
package api;

import connection.Session;
import connection.Streamparser;
import irc.IrcBuffer;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import thread.RequestThread;
import thread.StreamThread;

public class Connectionmanager {

    private static DefaultHttpClient httpclient;
    private static APICallbacklistener callbacklistener;

    /**
     * Gets token from IRCCloud API, needed for login
     */
    public static void getToken() {
        System.out.println("REQUESTING TOKEN");
        RequestThread tokenthread = new RequestThread(callbacklistener, httpclient, "POST", APIConstants.RECEIVER_CLASS_TYPE_TOKEN_RESPONSE, APIConstants.TOKEN_URL);
        //tokenthread.addHeader("content-length", "0");
        tokenthread.start();
    }

    /**
     * Sends user login data to irccloud server.
     *
     * @param receiverClass
     * @param email
     * @param password
     */
    public static void sendLogin(String email, String password) {
        RequestThread loginthread = new RequestThread(callbacklistener, httpclient, "POST", APIConstants.RECEIVER_CLASS_TYPE_LOGIN_RESPONSE, APIConstants.LOGIN_URL);
        loginthread.addStringEntity("token=" + Session.getSessionToken() + "&email=" + Session.getUsername() + "&password=" + Session.getPassword());
        loginthread.addHeader("x-auth-formtoken", Session.getSessionToken());
        loginthread.start();
    }

    /**
     * Send an OOBresponse to IrcCloud API
     *
     * @param receiverClass Class that receives the callback
     * @param ooburl OOBURL as given from irccloud api
     */
    public static void sendOOBResponse(String ooburl) {
        RequestThread oobthread = new RequestThread(callbacklistener, httpclient, "GET", APIConstants.RECEIVER_CLASS_TYPE_OOB_RESPONSE, APIConstants.IRCCLOUD_URL + ooburl);
        //oobthread.addStringEntity("session="+Session.getSessionID());
        oobthread.addHeader("Accept-Encoding", "gzip");
        oobthread.start();
    }

    /**
     * Sends heartbeat for given buffer to IRCCloud API
     *
     * @param buffer Buffer for the heartbeat
     */
    public static void sendHeartBeat(IrcBuffer buffer) {
        //channel.setEidAsLatest();

        RequestThread beatthread = new RequestThread(callbacklistener, httpclient, "POST", APIConstants.RECEIVER_CLASS_TYPE_HEARTBEAT_RESPONSE, APIConstants.HEARTBEAT_URL);
        JSONObject seeneid = new JSONObject();
        JSONObject bid = new JSONObject();

        bid.put(buffer.bid, buffer.getLastSeenEid());
        seeneid.put(buffer.cid, bid);
        String seeneidstring = JSONValue.toJSONString(seeneid);
        beatthread.addStringEntity("session=" + Session.getSessionID() + "&selectedBuffer=" + buffer.bid + "&seenEids=" + seeneidstring);
        beatthread.start();
    }

    /**
     * Connects to IRC network
     *
     * @param hostname Hostname for the network
     * @param port Port for the network
     * @param ssl Does the network connection use SSL
     * @param realname Realname to be used in the network
     * @param nickname User nickname for the connection
     * @param serverpass Optional password for the network
     * @param channels Channels you want to join when after connecting.
     */
    public static void connectToNerwork(String hostname, String port, boolean ssl, String realname, String nickname, String serverpass, String channels) {
        RequestThread networkJoinThread = new RequestThread(callbacklistener, httpclient, "POST", APIConstants.RECEIVER_CLASS_TYPE_NETWORKCONNECT_RESPONSE, APIConstants.NETWORKCONNECT_URL);

        String host = "";
        String usesSSL = "";
        String networkname = "";
        String serverkey = "";
        String npass = "";
        String joincommands = "";
        String channelstring = "";
        if (ssl) {
            usesSSL = "1";
        }
        else {
            usesSSL = "0";
        }
        try {
            channelstring = URLEncoder.encode(channels, "UTF-8");
            host = URLEncoder.encode(hostname, "UTF-8");
            networkname = URLEncoder.encode(realname, "UTF-8");
            if (!serverpass.isEmpty()) {
                serverkey = URLEncoder.encode(serverpass, "UTF-8");
            }

        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        networkJoinThread.addStringEntity("session=" + Session.getSessionID() + "&hostname=" + host + "&port=" + port + "&ssl=" + usesSSL + "&netname=" + networkname + "&nickname=" + nickname + "&realname=" + realname + "&server_pass" + serverkey + "&npass=" + npass + "&joincommands=" + joincommands + "&channels=" + channelstring);
        networkJoinThread.start();
    }

    /**
     * Reconnects to the given network
     *
     * @param cid Connection ID for the network you want to reconnect
     */
    public static void reconnectToNetwork(String cid) {
        RequestThread networkreconnect = new RequestThread(callbacklistener, httpclient, "POST", APIConstants.RECEIVER_CLASS_TYPE_NETWORKRECONNECT_RESPONSE, APIConstants.NETWORKRECONNECT_URL);
        networkreconnect.addStringEntity("session=" + Session.getSessionID() + "&cid=" + cid);
        networkreconnect.start();
    }

    /**
     * Called when networks status changes
     *
     * @param cid Cid of the networks which status changed
     * @param newStatus The new status for the network
     */
    public static void networkStatusChanged(String cid, String newStatus) {
        if (newStatus.equals("connected_ready")) {
            for (IrcBuffer c : Core.getNetwork(cid).getAllBuffers()) {
                if (c.deferred) {
                    getBacklogForBuffer(cid, c.bid);
                }
            }
        }
        Core.getNetwork(cid).changeNetworkStatus(newStatus);

    }

    /**
     * Gets backlog for given buffer
     *
     * @param cid Cid for the network the buffer belongs to
     * @param bid Bid for the buffer
     */
    public static void getBacklogForBuffer(String cid, String bid) {
        RequestThread backlog = new RequestThread(callbacklistener, httpclient, "GET", APIConstants.RECEIVER_CLASS_TYPE_BACKLOG_RESPONSE, APIConstants.BACKLOG_URL + "?cid=" + cid + "&bid=" + bid);
        backlog.start();
    }

    /**
     * Opens the incoming stream from Irccloud
     */
    public static void openStream(Streamparser streamparser) {
        StreamThread streamthread = new StreamThread(httpclient, streamparser, Session.getSessionID());
        streamthread.start();
    }

    /**
     * Sets Httpclient
     *
     * @param client Httpclient to be used
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
