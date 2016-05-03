/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package irc.events;

/**
 *
 * @author Zuppi
 */
public class IrcEventMeMessage extends IrcEvent{
     private String message;
     
    public IrcEventMeMessage(Long eid, String nick, String message){
        setEid(eid);
        this.username = nick;
        this.message = message;
    }
    
    @Override
    public String getEventText() {
        return message;
    }
    
}
