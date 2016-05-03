package irc;

import irc.events.IrcEvent;
import irc.events.IrcEventMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class that stores Irc Channel information.
 * @author Zuppi
 */
public class IrcBuffer {

    
    public static String NEWCHANNEL = "newchannel";
    public static String BACKLOGCHANNEL = "backlogchannel";
    
    
    /**Variables holding the channel information*/   
    public String cid;
    public String bid;
    public String buffertype;
    public String buffername;
    public Boolean deferred;
    public Boolean timeout;
    public String timestamp;
    private Boolean archived;
    public Long mineid;
    public Long buffercreated;
    private Long lastSeenEid;
    private Long lastEid;
    
    private boolean isStatusBuffer;
    private boolean isQueryBuffer;
    //private boolean unseenMessages;
    
    /**Channel topic*/
    String topic;
    /**Author for the channel topic*/
    String topicSetter;
    /** Modes set for the channel*/   
    String channelModes;
    
    /**Holds all the channelevents to be shown on the chat window */
    public List<IrcEvent> channelEvents;
    /**Holds all channel members*/
    public List<IrcUser> members;

    /**
     * Constructor for backlog channel
     * @param channelmap Map containing the channel information
     * @param network Network for the channel
     */
    public IrcBuffer(Map channelmap, String channeltype) {
        this.cid = channelmap.get("cid").toString();
        this.bid = channelmap.get("bid").toString();
        this.buffertype = channelmap.get("buffer_type").toString();
        this.buffername = channelmap.get("name").toString();
        this.lastEid=0L;
        this.lastSeenEid=0L;
        if (buffertype.equals("channel")){
            this.isStatusBuffer = false;
            this.isQueryBuffer = false;
        }
        else if (buffertype.equals("conversation")){
            this.isStatusBuffer = false;
            this.isQueryBuffer = true;
        }
        else if (buffertype.equals("console")){
            this.isStatusBuffer = true;
            this.isQueryBuffer = false;
        }
        
        if (channeltype.equals(NEWCHANNEL)){
            
            if (buffertype.equals("channel")){
                //this.buffername = channelmap.get("name").toString();
                this.topic = "";
                this.topicSetter = "";
                this.buffertype = "channel";
                
            }
            /*else if (buffertype.equals("conversation")){
                this.buffername = channelmap.get("name").toString();
                this.buffertype = "conversation";
            }
            else if (buffertype.equals("console")){
                
            }*/  
            this.archived = false;
            //this.unseenMessages = false;
        }
        else if (channeltype.equals(BACKLOGCHANNEL)){
                buffername = channelmap.get("name").toString();
                deferred = Boolean.valueOf(channelmap.get("deferred").toString());
                timeout = Boolean.valueOf(channelmap.get("timeout").toString());
                archived = Boolean.valueOf(channelmap.get("archived").toString());
                mineid = Long.parseLong(channelmap.get("min_eid").toString());
                buffercreated = Long.parseLong(channelmap.get("created").toString());
                lastSeenEid = Long.parseLong(channelmap.get("last_seen_eid").toString());
        }
        channelEvents = new ArrayList<IrcEvent>();
        members = new ArrayList<IrcUser>();

        System.out.println(buffertype.toUpperCase()+" " + buffername + " CREATED WITH BID: "+bid);
    }

    /**
     * Sets channels information according to received channel init
     * @param members List of channel members
     * @param topic Map containing topic, topic setter and timestamp.
     * @param modes Mode string for the channel
     */
    /*public void initChannel(List members, Map topic, String modes, String timestamp){
        addChannelMembers(members);
        if (topic.get("text") != null) {
           setTopic(topic.get("text").toString(), topic.get("nick").toString());
        }
        else{
           setTopic("",""); 
        }
        setChannelModes(modes);
        this.timestamp = timestamp;
    }*/
    
    public void initChannel(Map initmap, String timestamp){
        addChannelMembers((List)initmap.get("members"));
        //addChannelMembers(members);
        Map inittopic = (Map)initmap.get("topic");
        if (inittopic.get("text") != null) {
           setTopic(inittopic.get("text").toString(), inittopic.get("nick").toString());
        }
        else{
           setTopic("",""); 
        }
        setChannelModes(initmap.get("mode").toString());
        this.timestamp = timestamp;
    }
    /**
     * @return Channel name
     */
    public String getBufferName() {
        return buffername;
    }

    /**
     * Adds an event to the list of events
     * @param event Event to be added
     */
    public void addIrcEvent(IrcEvent event) {
        channelEvents.add(event);    
        lastEid = event.getEventEid();
        
        /*if (lastEid > lastSeenEid){
            unseenMessages = true;
        }*/
    }

    /**
     * Adds a singular user to the channel members
     * @param member Member to be added
     */
    public void addChannelMember(IrcUser member) {
        System.out.println("ADDED MEMBER " + member.nick + " TO " + buffername);
        members.add(member);
    }

    /**
     * Adds a list of users to the channel
     * @param memberlist List of users to be added
     */
    public void addChannelMembers(List memberlist) {
        for (Object obj : memberlist) {
            Map membermap = (Map) obj;
            String nick = membermap.get("nick").toString();
            String realname = membermap.get("realname").toString();
            String ircserver = membermap.get("ircserver").toString();
            String mode = membermap.get("mode").toString();
            boolean isAway = Boolean.parseBoolean(membermap.get("away").toString());
            IrcUser user = new IrcUser(nick, realname, ircserver, mode, isAway, false);
            members.add(user);
        }
    }

    /**
     * Removes a member from the channel
     * @param userToRemove Member to be removed
     */
    public void removeChannelMember(IrcUser userToRemove) {
        members.remove(userToRemove);
    }
    
    /**
     * Searches channel members for the given nick
     * @param nick User to be searched from the chanel
     * @return User data if member is found, null otherwise
     */
    public IrcUser getChannelMember(String nick){
        for (IrcUser user : members) {
            if (user.nick.equals(nick)) {
                return user;             
            }
        }
        return null;
    }

    /**
     * Sets the channel topic
     * @param topic Topic to be set
     * @param author The topic author
     */
    public void setTopic(String topic, String author) {
        this.topic = topic;
        this.topicSetter = author;
    }
    
    /**
     * Gets the channel topic
     * @return Channel topic
     */
    public String getTopic(){
        return topic;
    }
    
    /**
     * Sets the latest eid as the last eid seen. Used when changing channels
     */
    public void setEidAsLatest(){
        lastSeenEid = lastEid;
    }
    
    public void setLastSeenEid(Long eid){
        
        System.out.println("LATEST EID SET AS "+eid+" FOR "+buffername+" "+bid);
        lastSeenEid = eid;
    }

    
    /**
     * 
     * @return Latest eid user has seen on this channel
     */
    public Long getLastSeenEid(){
        return lastSeenEid;
    }
    
    /**
     * 
     * @return Latest eid on the channel
     */
    public Long getLatestEid(){
        return lastEid;
    }
    
    public boolean hasUnseenMessages() {
        if (lastEid != null && lastSeenEid != null){
            
            return lastEid > lastSeenEid;
        }
        return false;
	// Try / catch because when starting the client these could be null
	/*try {
	    
	}
	catch (Exception e) {
            e.printStackTrace();
	    return false;
	}*/
    }
    
    /**
     * Sets the channel modes
     * @param modes The set modestring
     */
    public void setChannelModes(String modes){
        this.channelModes = modes;
    }

    /**
     * @return Returns true if channel is a status channel
     */
    public boolean isStatusChannel() {
        return isStatusBuffer;
    }
    
    /**
     * @return Returns true if channel is a query channel
     */
    public boolean isQueryChannel(){
        return isQueryBuffer;
    }
    
    /**
     * 
     * @return Returns true if channel is archived
     */
    public boolean isArchived(){
        return archived;
    }
    
    /**
     * Sets the channels archived status
     * @param channelArchived New channel archived status
     */
    public void setArchived(Boolean channelArchived){
        archived = channelArchived;
    }
}
