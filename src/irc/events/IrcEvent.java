package irc.events;

import util.DatetimeConverter;

/**
 * Base class for all IrcEvents
 * 
 */
public abstract class IrcEvent {
    protected Long eid;
    protected String timestamp="";
    protected String username = "";
    
    /**
     * Abstract class that must be implemented in all the extending classes
     */
    public abstract String getEventText();
    
    /**
     * Returns the events timestamp
     * @return Timestamp in HH:MM format
     */
    public String getEventTimestamp(){
        return timestamp;
    };
    
    public void setEid(Long eid){
        this.eid = eid;
    }
    
    public void setTimestampEid(Long eid){
        timestamp = DatetimeConverter.convertEid(eid);
        this.eid = eid;
    }
    
    public void setTimestmapUnix(Long unixtimestamp){
        timestamp = DatetimeConverter.convertUnixEpoch(unixtimestamp);
    }
    
    public Long getEventEid(){
        return eid;
    }
    
    /**
     * Returns username for the event if the event has one, otherwise returns an empty string
     * @return Username for event
     */
    public String getUsername(){
        return username;
    }
    
    /**
     * Checks if the event has a username set
     * @return True if the event has username set, otherwise false
     */
    public boolean hasUsername(){
        return !username.isEmpty();
    }
        
}
