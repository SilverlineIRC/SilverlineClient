/**
 * Core controls most of the innermost things for Silverline.
 */
package api;

import connection.Session;
import connection.Streamparser;
import gui.windows.login.GUILogin;
import irc.IrcBuffer;
import irc.IrcNetwork;
import irc.events.IrcEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.impl.client.DefaultHttpClient;
import util.GUIInterface;

/**
 *
 * @author Zuppi
 */
public class Core {

    public static List<IrcNetwork> networks;
    public static List<String> networkcids;
    public static GUIInterface guiinterface;
    public static GUILogin guilogin;
    public static Streamparser streamparser;

    private static boolean parsingBacklog = false;
    private static double componentsToLoad = 0;
    private static double loadedComponents = 0;

    //List holding channels that have been buffercreated but not initialized/added
    private static List<IrcBuffer> waitingChannels = new ArrayList<IrcBuffer>();

    /**
     * After login to irccloud server was succesfull.
     */
    public static void loginSuccesful() {
        Connectionmanager.openStream(streamparser);
        guilogin.setVisible(false);
        guiinterface.showIrcGui(true);
    }

    /**
     * Creates new IrcNetwork
     *
     * @param networkMap Map from streamparser to be used for network creation
     */
    public static void createNetwork(Map networkMap) {
        IrcNetwork network = new IrcNetwork(networkMap);
        networks.add(network);
        if (network.status.equals("disconnected")) {
            Connectionmanager.reconnectToNetwork(networkMap.get("cid").toString());
        }
    }

    /**
     * Creates new IrcBuffer
     *
     * @param channelmap Map from streamparser to be used for buffer creation
     */
    public static void createBuffer(Map channelmap) {
        IrcBuffer buffer;
        if (parsingBacklog) {
            buffer = new IrcBuffer(channelmap, IrcBuffer.BACKLOGCHANNEL);
            getNetwork(buffer.cid).addBuffer(buffer);
            if (buffer.deferred) {
                if (buffer.isArchived()) {
                    progressLoad();
                }
            }
            else {
                progressLoad();
            }
        }
        else {
            buffer = new IrcBuffer(channelmap, IrcBuffer.NEWCHANNEL);
            getNetwork(buffer.cid).addBuffer(buffer);
            guiinterface.addNetworkChannel(buffer);
        }
    }

    /**
     * Initializes a newly created IrcBuffer
     *
     * @param bid Bid of the buffer
     * @param timestamp Creation timestamp for the channel
     * @param initmap Map containing initialization data
     */
    public static void initBuffer(String bid, String timestamp, Map initmap) {
        IrcBuffer channel = getBuffer(bid);
        channel.initChannel(initmap, timestamp);
    }

    /**
     * Undefers a deferred buffer
     *
     * @param bid Bid of the buffer to be undeferred
     */
    public static void undeferBuffer(String bid) {
        getBuffer(bid).deferred = false;
        progressLoad();
    }

    /**
     * Updates each channels last seen Eid
     *
     * @param heartbeatmap Map containing heartbeat information from IRCCloud API
     */
    public static void updateLastSeenEids(Map heartbeatmap) {
        for (IrcNetwork n : networks) {
            if (heartbeatmap.containsKey(n.cid)) {
                Map buffermap = (Map) heartbeatmap.get(n.cid);
                for (IrcBuffer b : getNetwork(n.cid).networkBuffers) {
                    if (buffermap.containsKey(b.bid)) {
                        Long eid = (Long) buffermap.get(b.bid);
                        b.setLastSeenEid(eid);
                    }
                }
            }
        }

        //System.out.println(networkset);
        //List testlist = new ArrayList(heartbeatmap.entrySet());
        //System.out.println(testlist);
        //System.out.println(testlist.get(0));
        //Object[] testarray = mapkeys.toArray();
        //System.out.println(testarray.toString());
        /*for(IrcNetwork n : networks){
         if (heartbeatmap.containsKey(n.cid)){
         System.out.println("FOUND CID!");
         Map networkmap = (Map) heartbeatmap.get(n.cid);
         for (IrcBuffer b : n.networkBuffers){
         if (networkmap.containsKey(b.bid)){
         System.out.println("FOUND BID");
         b.setEidAsLatest();
         }
         }
         }
         }*/
    }

    /**
     * Determines which network and buffer the event belongs to
     *
     * @param event List containing the event data
     */
    public static void assignEventToBuffer(String cid, String bid, IrcEvent event) {

        for (IrcNetwork n : networks) {
            if (n.cid.equals(cid)) {
                for (IrcBuffer c : n.networkBuffers) {
                    if (c.bid.equals(bid)) {
                        c.addIrcEvent((IrcEvent) event);
                        guiinterface.ircChannelEventReceived(c, (IrcEvent) event);
                        break;
                    }
                }
                break;
            }
        }

    }

    /**
     * Checks if given buffer cid is the current buffer in gui
     *
     * @param cid Connection ID for the buffer
     * @return Returns true if cid belongs to the currently open buffer, false if not
     */
    public static Boolean isCurrentBuffer(String cid) {
        for (IrcNetwork n : networks) {
            if (guiinterface.getCurrentSelectedChannel().cid.equals(cid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the network that corresponds to the given Connection ID
     *
     * @param cid Connection ID for the network to fetch
     * @return Network that corresponds to the given Connection ID
     */
    public static IrcNetwork getNetwork(String cid) {
        for (IrcNetwork n : networks) {
            if (n.cid.equals(cid)) {
                return n;
            }
        }
        return null;
    }

    /**
     * Finds IrcBuffer
     *
     * @param bid Bid of the buffer to be searched
     * @return IrcBuffer corresponding to the given bid
     */
    public static IrcBuffer getBuffer(String bid) {
        for (IrcNetwork n : networks) {
            for (IrcBuffer c : n.networkBuffers) {
                if (c.bid.equals(bid)) {
                    return c;

                }
            }
        }
        return null;
    }

    /**
     * Initializes the core
     *
     * @param client Httpclient to be used in all API communication
     */
    public static void initCore(DefaultHttpClient client) {
        Connectionmanager.setHttpClient(client);
        OutgoingMessages.setHttpClient(client);
        APICallbacklistener listener = new APICallbacklistener();
        Connectionmanager.setCallbacklistener(listener);
        OutgoingMessages.setCallbacklistener(listener);
        guiinterface = new GUIInterface(false);
        networks = new ArrayList<IrcNetwork>();
        streamparser = new Streamparser();
    }

    /**
     * Starts the load progress
     *
     * @param bufferamount Amount of buffers to be loaded
     */
    public static void startLoadProgress(int bufferamount) {
        System.out.println("LOAD BUFFER AMOUNT: " + bufferamount);
        componentsToLoad = bufferamount;
        setParsingBacklog(true);
    }

    /**
     * Progresses loading
     */
    public static void progressLoad() {
        if (parsingBacklog) {
            loadedComponents++;
            if (loadedComponents == componentsToLoad) {
                guiinterface.loadingComplete();

            }
            else {
                int loadPercentage = (int) Math.ceil(loadedComponents / componentsToLoad * 100);
                guiinterface.setLoadProgress(loadPercentage);
            }
        }
    }

    /**
     * Called when backlog parsing is complete
     */
    public static void backlogComplete() {
        setParsingBacklog(false);
        if (Session.getLastOpenBid().equals("-1")) {
            guiinterface.connectionToIrccloudCompleted(networks, networks.get(0).networkBuffers.get(1));

        }
        else {
            guiinterface.connectionToIrccloudCompleted(networks, getBuffer(Session.getLastOpenBid()));
        }
    }

    /**
     * Updates the server latency
     *
     * @param cid Cid for the server
     * @param latency Amount of the latency
     */
    public static void updateServerLatency(String cid, String latency) {
        getNetwork(cid).latency = latency;
    }

    /**
     * Sets the logingui that will be used
     *
     * @param login GUILogin to be used
     */
    public static void setLoginGUI(GUILogin login) {
        guilogin = login;
    }

    /**
     * Updates all components backlog parsing status
     *
     * @param parsing Status of backlog parsing
     */
    public static void setParsingBacklog(boolean parsing) {
        parsingBacklog = parsing;
        streamparser.setParsingBacklog(parsing);
        IncomingMessages.setParsingBacklog(parsing);
    }
}
