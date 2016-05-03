/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package irc.events;

/**
 *
 * @author Zuppi
 */
public class IrcEventWhoIs extends IrcEvent{
    
    String whoismessage;

    public IrcEventWhoIs(Long eid, String message){
        setTimestampEid(eid);
        this.whoismessage = message;
    }
    @Override
    public String getEventText() {
        return whoismessage;
    }
    
}
