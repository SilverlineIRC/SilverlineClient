/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package irc.events;

/**
 *
 * @author Zuppi
 */
public class IrcEventNotice extends IrcEvent {
    
    private String message;
    
    public IrcEventNotice(Long eid, String msg){
        setTimestampEid(eid);
        this.message = msg;
        
    }

    @Override
    public String getEventText() {
       return message;
    }
    
}
