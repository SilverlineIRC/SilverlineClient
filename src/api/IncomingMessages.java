/*
 * Handles most of the incoming messages from IRCCloud API
 */
package api;

import static api.Core.getBuffer;
import static api.Core.getNetwork;
import static api.Core.guiinterface;
import connection.Session;
import irc.IrcBuffer;
import irc.IrcUser;
import irc.events.IrcEventChannelCreationTimestamp;
import irc.events.IrcEventChannelMode;
import irc.events.IrcEventChannelModeChange;
import irc.events.IrcEventJoin;
import irc.events.IrcEventKick;
import irc.events.IrcEventMessage;
import irc.events.IrcEventNickChange;
import irc.events.IrcEventNotice;
import irc.events.IrcEventPart;
import irc.events.IrcEventQuit;
import irc.events.IrcEventTopic;
import irc.events.IrcEventTopicChange;
import irc.events.IrcEventUserModeChange;
import irc.events.IrcEventWhoIs;
import java.util.List;
import java.util.Map;
import util.Crashlogger;
import util.DatetimeConverter;

/**
 *
 * @author Zuppi
 */
public class IncomingMessages {

    private static boolean parsingBacklog = false;

    /**
     * Called after oobresponse is recieved from IrcCloud
     *
     * @param oobMap Map containing the oobresponse data
     */
    public static void OobReceived(Map oobMap) {
        Connectionmanager.sendOOBResponse(oobMap.get("url").toString());
    }

    /**
     * Called after userStats are recieved from IrcCloud
     *
     * @param messagemap Map containing the user stats
     */
    public static void userStatsRecieved(Map messagemap) {
        Session.initSession(messagemap);

    }

    /**
     * Called when user parts a channel
     *
     * @param partmap Map containing parting info
     */
    public static void userPartedChannel(Map partmap) {
        if (!parsingBacklog) {
            IrcBuffer partedChannel = getNetwork(getCid(partmap)).getBuffer(getBid(partmap));

            //getNetwork(cid).removeIrcChannel(partedChannel);
        }
    }

    /**
     * Called when user quits from server
     *
     * @param quitmap Map containing quit info
     */
    public static void userQuitServer(Map quitmap) {
        String quitmessage = "SERVER QUIT " + quitmap.get("hostname").toString() + ": " + quitmap.get("msg").toString();
        IrcEventNotice serverquit = new IrcEventNotice((Long) quitmap.get("eid"), quitmessage);
        Core.assignEventToBuffer(getCid(quitmap), getBid(quitmap), serverquit);
    }

    /**
     * Called when message is received from IrcCloud
     *
     * @param messagemap Map containing message info
     */
    public static void ircMessageEventReceived(Map messagemap) {
        IrcEventMessage message = new IrcEventMessage(messagemap);
        Core.assignEventToBuffer(getCid(messagemap), getBid(messagemap), message);
    }

    /**
     * Called when an another user joins a channel
     *
     * @param joinmap Map containing join info
     */
    public static void ircJoinEventReceived(Map joinmap) {

        String cid = getCid(joinmap);
        String bid = getBid(joinmap);
        String nick = joinmap.get("nick").toString();
        String hostmask = joinmap.get("hostmask").toString();
        if (!parsingBacklog) {
            IrcUser newUser = new IrcUser(nick, "", hostmask, "", false, false);
            getNetwork(cid).getBuffer(bid).addChannelMember(newUser);
            if (Core.isCurrentBuffer(cid)) {
                guiinterface.addUserToCurrentChannel(newUser);
            }
        }
        IrcEventJoin joinEvent = new IrcEventJoin(getEid(joinmap), nick, hostmask);
        Core.assignEventToBuffer(cid, bid, joinEvent);
    }

    /**
     * Called when an another user leaves a channel
     *
     * @param partmap Map contaning part info
     */
    public static void ircPartEvent(Map partmap) {
        String cid = getCid(partmap);
        String bid = getBid(partmap);

        if (!parsingBacklog) {

            IrcBuffer partchannel = getNetwork(cid).getBuffer(bid);
            IrcUser userToRemove = partchannel.getChannelMember(partmap.get("nick").toString());
            partchannel.removeChannelMember(userToRemove);
            if (Core.isCurrentBuffer(cid)) {
                guiinterface.removeUserFromCurrentChannel(userToRemove);
            }
        }
        IrcEventPart partEvent = new IrcEventPart(partmap);
        Core.assignEventToBuffer(cid, bid, partEvent);
    }

    /**
     * Called when someone gets kicked for a channel the user is in
     *
     * @param kickmap Map contaning kick info
     */
    public static void ircKickEvent(Map kickmap) {
        String cid = getCid(kickmap);
        String bid = getBid(kickmap);

        if (!parsingBacklog) {
            IrcBuffer kickchannel = getNetwork(cid).getBuffer(bid);
            IrcUser userToRemove = kickchannel.getChannelMember(kickmap.get("nick").toString());
            kickchannel.removeChannelMember(userToRemove);
            if (Core.isCurrentBuffer(cid)) {
                guiinterface.removeUserFromCurrentChannel(userToRemove);
            }
        }
        IrcEventKick kickEvent = new IrcEventKick(kickmap);
        Core.assignEventToBuffer(cid, bid, kickEvent);
    }

    /**
     * Called when someone quits from a channel the user is in
     *
     * @param quitmap Map containing quit info
     */
    public static void ircQuitEvent(Map quitmap) {
        String cid = getCid(quitmap);
        String bid = getBid(quitmap);

        if (!parsingBacklog) {
            IrcBuffer quitchannel = getNetwork(cid).getBuffer(bid);
            IrcUser userToRemove = quitchannel.getChannelMember(quitmap.get("nick").toString());
            quitchannel.removeChannelMember(userToRemove);
            if (Core.isCurrentBuffer(cid)) {
                guiinterface.removeUserFromCurrentChannel(userToRemove);
            }
        }
        IrcEventQuit quitEvent = new IrcEventQuit(quitmap);
        Core.assignEventToBuffer(cid, bid, quitEvent);
    }

    /**
     * Called when a channel creation timestamp is received
     *
     * @param channelcreatemap Map containing info on creation timestamp
     */
    public static void ircChannelCreationTimestampEventReceived(Map channelcreatemap) {

        IrcEventChannelCreationTimestamp timestampEvent = new IrcEventChannelCreationTimestamp(getEid(channelcreatemap), (Long) channelcreatemap.get("timestamp"));
        Core.assignEventToBuffer(getCid(channelcreatemap), getBid(channelcreatemap), timestampEvent);
    }

    /**
     * Called when someone on a channel changes their nick
     *
     * @param nickchangemap Map containing nick change information
     */
    public static void ircNickChangeEventReceived(Map nickchangemap) {
        String oldnick = nickchangemap.get("oldnick").toString();
        String newnick = nickchangemap.get("newnick").toString();
        String cid = getCid(nickchangemap);

        if (!parsingBacklog) {
            if (getNetwork(cid).userNick.equals(oldnick)) {
                guiinterface.setCurrentNickname(newnick);
            }
            if (Core.isCurrentBuffer(cid)) {
                guiinterface.changeUserNickInCurrentChannel(oldnick, newnick);
            }
            getNetwork(cid).changeIrcUserNick(oldnick, newnick);
        }
        IrcEventNickChange nickEvent = new IrcEventNickChange(getEid(nickchangemap), oldnick, newnick);
        Core.assignEventToBuffer(cid, getBid(nickchangemap), nickEvent);
    }

    /**
     * Called when a channels topic is changed
     *
     * @param topicmap
     */
    public static void ircTopicChangeEventReceived(Map topicmap) {

        String cid = getCid(topicmap);
        String bid = getBid(topicmap);
        if (!parsingBacklog) {
            getNetwork(cid).getBuffer(bid).setTopic(topicmap.get("topic").toString(), topicmap.get("author").toString());
        }
        IrcEventTopicChange topicEvent = new IrcEventTopicChange(topicmap);
        Core.assignEventToBuffer(cid, bid, topicEvent);
    }

    /**
     * Called when a channels topic is received from API
     *
     * @param topicmap
     */
    public static void ircTopicEventReceived(Map topicmap) {
        IrcEventTopic topicEvent = new IrcEventTopic(topicmap);
        Core.assignEventToBuffer(getCid(topicmap), getBid(topicmap), topicEvent);
    }

    /**
     * Called when a channels modes a received from API
     *
     * @param modemap
     */
    public static void ircModeEventReceived(Map modemap) {
        /*System.out.println(getBid(modemap));
         System.out.println(modemap.get("newmode").toString());
         if (getBuffer(getBid(modemap)) == null){
         System.out.println("aASDASDDA");
         }*/
        getBuffer(getBid(modemap)).setChannelModes(modemap.get("newmode").toString());
        IrcEventChannelMode modeEvent = new IrcEventChannelMode(modemap);
        Core.assignEventToBuffer(getCid(modemap), getBid(modemap), modeEvent);
    }

    /**
     * Called when channel modes are changed
     *
     * @param modemap
     */
    public static void ircChannelModeChangeEventRecieved(Map modemap) {
        if (!parsingBacklog) {
            getBuffer(getBid(modemap)).setChannelModes(modemap.get("newmode").toString());
        }
        String changer = "";
        if (modemap.get("from") != null) {
            changer = modemap.get("from").toString();
        }
        else {
            changer = modemap.get("server").toString();
        }
        IrcEventChannelModeChange modeEvent = new IrcEventChannelModeChange(modemap, changer);
        Core.assignEventToBuffer(getCid(modemap), getBid(modemap), modeEvent);
    }

    /**
     * Called when usermodes are changed
     *
     * @param usermodemap
     */
    public static void ircUserModeChangeEventRecieved(Map usermodemap) {
        String cid = getCid(usermodemap);
        String bid = getBid(usermodemap);
        if (!parsingBacklog) {
            String target = usermodemap.get("nick").toString();
            String newmode = usermodemap.get("newmode").toString();
            getBuffer(bid).getChannelMember(target).setModes(newmode);
            if (Core.isCurrentBuffer(cid)) {
                guiinterface.changeUserModeInCurrentChannel(target, newmode);
            }
        }
        String changer;
        if (usermodemap.get("from") == null) {
            changer = "Q";
        }
        else {
            changer = usermodemap.get("from").toString();
        }
        IrcEventUserModeChange usermode = new IrcEventUserModeChange(usermodemap, changer);
        Core.assignEventToBuffer(cid, bid, usermode);
    }

    /**
     * A sort of callback from API, called when user wants to archive a buffer
     *
     * @param deletemap
     */
    public static void archiveChannelMessageRecieved(Map deletemap) {
        IrcBuffer archiveChannel = getNetwork(getCid(deletemap)).getBuffer(getBid(deletemap));
        archiveChannel.setArchived(true);
        guiinterface.archiveChannel(archiveChannel);
    }

    /**
     * A sort of callback from API, called when user wants to unarchive a buffer
     *
     * @param deletemap
     */
    public static void unarchiveChannelMessageRecieved(Map deletemap) {
        IrcBuffer unarchiveChannel = getNetwork(getCid(deletemap)).getBuffer(getBid(deletemap));
        unarchiveChannel.setArchived(false);
        guiinterface.archiveChannel(unarchiveChannel);
    }

    /**
     * A sort of callback from API, called when user wants to delete a buffer
     *
     * @param deletemap
     */
    public static void bufferDeleteMessageRecieved(Map deletemap) {
        IrcBuffer deleteChannel = getNetwork(getCid(deletemap)).getBuffer(getBid(deletemap));
        getNetwork(getCid(deletemap)).removeBuffer(deleteChannel);
        guiinterface.removeChannel(deleteChannel);
    }

    /**
     * Called after receiving info of channel members from API
     *
     * @param membermap
     */
    public static void memberUpdatesRecieved(Map updatemap) {
        try{
          //System.out.println(membermap);
        List<IrcUser> userlist = getBuffer(getBid(updatemap)).members;
        Map membermap = (Map) updatemap.get("updates");
        for (IrcUser u : userlist) {
            if (membermap.containsKey(u.nick)) {
                Map memberdetails = (Map) membermap.get(u.nick);
                u.nick = memberdetails.get("nick").toString();
                u.isAway = Boolean.parseBoolean(memberdetails.get("away").toString());
                u.ircserver = memberdetails.get("userhost").toString();
                u.realname = memberdetails.get("realname").toString();
            }
        }           
        }
        catch(Exception e){
                Crashlogger.logCrash(e, updatemap);
        }
    }

    /**
     * Called when receiving notices from API
     *
     * @param noticemap
     */
    public static void ircNotice(Map noticemap) {
        Long eid = getEid(noticemap);
        String msg = noticemap.get("msg").toString();
        IrcEventNotice eventnotice = new IrcEventNotice(eid, msg);
        Core.assignEventToBuffer(getCid(noticemap), getBid(noticemap), eventnotice);
    }

    /**
     * Called when user has requested someones whois from IRC
     *
     * @param whoismap
     */
    public static void whoisResponse(Map whoismap) {
        String nick = whoismap.get("nick").toString();
        String realname = whoismap.get("user_realname").toString();
        String host = whoismap.get("user_host").toString();
        String whoismessage = "";
        if (whoismap.get("user_logged_in_as") != null) {
            String auth = whoismap.get("user_logged_in_as").toString();
            whoismessage = "WHOIS FOR: " + nick + "\n" + "Realname: " + realname + "\n" + "Authed as: " + auth + "\n" + "Address: " + nick + "@" + host;
        }
        else {
            whoismessage = "WHOIS FOR: " + nick + "\n" + "Realname: " + realname + "\n" + "Address: " + nick + "@" + host;
        }
        String currentbid = Core.guiinterface.getCurrentSelectedChannel().bid;

        IrcEventWhoIs whois = new IrcEventWhoIs(getEid(whoismap), whoismessage);
        Core.assignEventToBuffer(getCid(whoismap), currentbid, whois);
    }

    /**
     * Called when receiving message of the day from the server when connecting
     *
     * @param motdmap
     */
    public static void motdResponse(Map motdmap) {
        String cid = getCid(motdmap);
        String bid = getBid(motdmap);
        Long eid = getEid(motdmap);

        String start = motdmap.get("start").toString();
        List lines = (List) motdmap.get("lines");

        String end = motdmap.get("msg").toString();
        IrcEventNotice motdstart = new IrcEventNotice(eid, start);
        Core.assignEventToBuffer(cid, bid, motdstart);

        for (Object line : lines) {
            String linestring = line.toString();
            IrcEventNotice motdline = new IrcEventNotice(eid, linestring);
            Core.assignEventToBuffer(cid, bid, motdline);
        }
        IrcEventNotice motdend = new IrcEventNotice(eid, end);
        Core.assignEventToBuffer(cid, bid, motdend);
    }

    /**
     * Called when initializing a buffer
     *
     * @param initmap
     */
    public static void bufferInit(Map initmap) {
        Core.initBuffer(getBid(initmap), DatetimeConverter.convertUnixEpoch((Long) initmap.get("timestamp")), initmap);
    }

    /**
     * Sets backlog parsing status
     *
     * @param parsing New backlog parsing status
     */
    public static void setParsingBacklog(boolean parsing) {
        parsingBacklog = parsing;
    }

    /**
     * Gets Connection ID (Cid) from a map
     *
     * @param map Map to be searched for a cid
     * @return Cid converted into a string
     */
    private static String getCid(Map map) {
        return map.get("cid").toString();
    }

    /**
     * Gets Buffer ID (Bid) from a map
     *
     * @param map Map to be searched for a bid
     * @return Bid converted into a string
     */
    private static String getBid(Map map) {
        return map.get("bid").toString();
    }

    /**
     * Gets Eid from a map
     *
     * @param map Map to be searched for a eid
     * @return Eid converted into a string
     */
    private static Long getEid(Map map) {
        return (Long) map.get("eid");
    }

}
