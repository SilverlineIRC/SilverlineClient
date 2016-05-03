/*
 * 
 */
package connection;

import api.Connectionmanager;
import api.Core;
import api.IncomingMessages;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import thread.AbstractThreadCallbackListener;

/**
 * Directs each incoming message from IRCCLoud API to a correct function 
 */
public class Streamparser implements AbstractThreadCallbackListener {

    private boolean parsingBacklog = false;
    private Map currentMessagemap;

    public Streamparser() {

    }

    //Parses a incoming message from IRCCloud API
    public void parseStreamMessage() {

        String type = getMessageStringAttribute("type");

        if (type.equals("oob_include")) {
            IncomingMessages.OobReceived(currentMessagemap);
        }
        else {

            if (type.equals("header")) {
                parserPrint("RECIEVED: HEADER. HANDLING NOT IMPLEMENTED");
            }
            else if (type.equals("idle")) {
                //Nothing here on purpose, idle messages are used to prevent stream from closing
            }
            else if (type.equals("stat_user")) {
                //messagelist.add(currentMessagemap);
                IncomingMessages.userStatsRecieved(currentMessagemap);
                //TODO Lots of incoming variables to parse
            }
            else if (type.equals("num_invites")) {
                parserPrint("RECIEVED: NUM_INVITES, NO HANDLING IMPLEMENTED");
            }
            else if (type.equals("oob_timeout")) {
                parserPrint("OOB_INCLUDE TIMEOUT " + getMessageStringAttribute("timeout") + "ms");
            }
            else if (type.equals("oob_skipped")) {
                parserPrint("RECIEVED: OOB_SKIPPED, NO HANDLING IMPLEMENTED");
            }
            else if (type.equals("backlog_starts")) {
                parserPrint("BACKLOG STARTING");
                Core.startLoadProgress(Integer.parseInt(getMessageStringAttribute("numbuffers")));
                setParsingBacklog(true);
            }
            else if (type.equals("backlog_complete")) {
                parserPrint("BACKLOG COMPLETE");
                setParsingBacklog(false);
                Core.backlogComplete();

            }
            else if (type.equals("makeserver")) {
                Core.createNetwork(currentMessagemap);
            }
            else if (type.equals("end_of_backlog")) {
                parserPrint("SERVER BACKLOG END");
            }
            else if (type.equals("makebuffer")) {
                Core.createBuffer(currentMessagemap);
            }
            else if (type.equals("channel_init")) {
                IncomingMessages.bufferInit(currentMessagemap);

            }
            //Called when a status of a connection changes
            else if (type.equals("status_changed")) {
                String newStatus = getMessageStringAttribute("new_status");
                Connectionmanager.networkStatusChanged(getMessageStringAttribute("cid"), newStatus);
                //TODO: Add fail_info handling
            }
            else if (type.equals("connection:_lag")) {
                Core.updateServerLatency(getMessageStringAttribute("cid"), type);
            }
            else if (type.equals("heartbeat_echo")) {
                Map heartbeatmap = (Map) getMessageObjectAttribute("seenEids");
                Core.updateLastSeenEids(heartbeatmap);

            }
            else if (type.equals("buffer_msg")) {
                IncomingMessages.ircMessageEventReceived(currentMessagemap);

            }
            else if (type.equals("buffer_me_msg")) {
                IncomingMessages.ircMessageEventReceived(currentMessagemap);

            }
            else if (type.equals("channel_timestamp")) {
                //String creationTimestamp = convertUnixTimestamp(("timestamp"));
                IncomingMessages.ircChannelCreationTimestampEventReceived(currentMessagemap);
            }
            else if (type.equals("channel_url")) {
                parserPrint("RECIEVED: CHANNEL_URL, NO HANDLING IMPLEMENTED");
            }
            else if (type.equals("channel_topic")) {
                IncomingMessages.ircTopicChangeEventReceived(currentMessagemap);

            }
            else if (type.equals("channel_topic_is")) {
                IncomingMessages.ircTopicEventReceived(currentMessagemap);

            }
            else if (type.equals("channel_mode")) {
                IncomingMessages.ircChannelModeChangeEventRecieved(currentMessagemap);

            }
            else if (type.equals("channel_mode_is")) {
                IncomingMessages.ircModeEventReceived(currentMessagemap);

            }
            else if (type.equals("user_channel_mode")) {
                IncomingMessages.ircUserModeChangeEventRecieved(currentMessagemap);

            }
            else if (type.equals("member_updates")) {
                //Map membermap = (Map) getMessageObjectAttribute("updates");
                //IncomingMessages.memberUpdatesRecieved(membermap);
                IncomingMessages.memberUpdatesRecieved(currentMessagemap);
                //parserPrint("RECIEVED: MEMBER_UPDATES, NO HANDLING IMPLEMENTED");
            }
            else if (type.equals("self_details")) {
                parserPrint("RECIEVED: SELF_DETAILS, NO HANDLING IMPLEMENTED");
            }
            else if (type.equals("user_away")) {
                parserPrint("RECIEVED: USER_AWAY, NO HANDLING IMPLEMENTED");
            }
            else if (type.equals("away")) {
                parserPrint("RECIEVED: AWAY, NO HANDLING IMPLEMENTED");
            }
            else if (type.equals("self_away")) {
                parserPrint("RECIEVED: SELF_AWAY, NO HANDLING IMPLEMENTED");
            }
            else if (type.equals("joined_channel")) {
                IncomingMessages.ircJoinEventReceived(currentMessagemap);

            }
            else if (type.equals("you_joined_channel")) {
                //Nothing here on purpose, refer to: https://github.com/irccloud/irccloud-tools/wiki/API-Stream-Message-Reference#-you_joined_channel
            }
            else if (type.equals("parted_channel")) {
                IncomingMessages.ircPartEvent(currentMessagemap);

            }
            else if (type.equals("you_parted_channel")) {
                IncomingMessages.userPartedChannel(currentMessagemap);
            }
            else if (type.equals("kicked_channel")) {
                IncomingMessages.ircKickEvent(currentMessagemap);

            }
            else if (type.equals("you_kicked_channel")) {
                parserPrint("RECIEVED: YOU_KICKED_CHANNEL, NO HANDLING IMPLEMENTED");
            }
            else if (type.equals("quit")) {
                IncomingMessages.ircQuitEvent(currentMessagemap);

            }
            else if (type.equals("quit_server")) {
                IncomingMessages.userQuitServer(currentMessagemap);

            }
            else if (type.equals("nickchange") || type.equals("you_nickchange")) {

                IncomingMessages.ircNickChangeEventReceived(currentMessagemap);

            }
            else if (type.equals("rename_conversation")) {
                parserPrint("RECIEVED: RENAME_CONVERSATION, NO HANDLING IMPLEMENTED");
            }
            else if (type.equals("delete_buffer")) {
                IncomingMessages.bufferDeleteMessageRecieved(currentMessagemap);
            }
            else if (type.equals("buffer_archived")) {
                IncomingMessages.archiveChannelMessageRecieved(currentMessagemap);
            }
            else if (type.equals("buffer_unarchived")) {
                IncomingMessages.unarchiveChannelMessageRecieved(currentMessagemap);
            }
            else if (type.equals("server_details_changed")) {
                parserPrint("RECIEVED: SERVER_DETAILS_CHANGED, NO HANDLING IMPLEMENTED");
            }
            else if (type.equals("whois_response")) {
                IncomingMessages.whoisResponse(currentMessagemap);

            }
            else if (type.equals("set_ignores")) {
                parserPrint("RECIEVED: SET_IGNORES, NO HANDLING IMPLEMENTED");
            }
            else if (type.equals("link_channel")) {
                parserPrint("RECIEVED: LINK_CHANNEL, NO HANDLING IMPLEMENTED");
            }
            else if (type.equals("isupport_params")) {
                parserPrint("RECIEVED: ISUPPORT_PARAMS, NO HANDLING IMPLEMENTED");
            }
            else if (type.equals("myinfo")) {
                parserPrint("RECIEVED: MYINFO, NO HANDLING IMPLEMENTED");
            }
            //Messages below are not in API-documentation, but they are still being sent by IRCCloud servers.
            else if (type.equals("notice") || type.equals("server_welcome") || type.equals("server_yourhost") || type.equals("server_created")) {

                //parserPrint("APICALL NOT IN DOCUMENTATION!");
                String msg = getMessageStringAttribute("msg");
                IncomingMessages.ircNotice(currentMessagemap);

            }
            else if (type.equals("motd_response")) {
                IncomingMessages.motdResponse(currentMessagemap);

            }
            else if (type.equals("socket_closed")) {
                //parserPrint("APICALL NOT IN DOCUMENTATION!");
                /*String port = getMessageStringAttribute("port");
                 String hostname = getMessageStringAttribute("hostname");
                 String socketmessage = "Socket closed: " + hostname + " " + port;
                 IrcEventNotice socketclose = new IrcEventNotice(eid, timestamp, socketmessage);
                 messagelist.add(socketclose);
                 IncomingMessages.ircMessageEventReceived(messagelist);*/
            }
            else {
                parserPrint("UNCAUGHT EVENT: " + type);
            }
        }
    }

    public void parseOOBResponse(List list) {
        Iterator<JSONObject> iterator = list.iterator();

        while (iterator.hasNext()) {
            currentMessagemap = (Map) iterator.next();
            parseStreamMessage();
        }
    }

    public void parseBacklogResponse(List list) {
        Core.setParsingBacklog(true);
        Iterator<JSONObject> iterator = list.iterator();
        while (iterator.hasNext()) {
            currentMessagemap = (Map) iterator.next();
            parseStreamMessage();
        }
        Core.setParsingBacklog(false);
        Core.undeferBuffer(getMessageStringAttribute("bid"));
    }

    @Override
    public void callbackReceivedFromThread(String type, Object object) {
        parserPrint("LINE RECIEVED: " + object.toString());

        currentMessagemap = (Map) object;
        parseStreamMessage();
    }

    public void setParsingBacklog(boolean parsing) {
        parsingBacklog = parsing;
    }

    private void parserPrint(String message) {
        System.out.println("[STREAMPARSER] " + message);
    }

    private String getMessageStringAttribute(String attr) {
        return currentMessagemap.get(attr).toString();
    }

    private Object getMessageObjectAttribute(String attr) {
        return currentMessagemap.get(attr);
    }
}
