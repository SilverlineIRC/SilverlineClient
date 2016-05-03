/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package irc.events;

import java.util.Map;

/**
 *
 * @author Zuppi
 */
public class IrcEventMessage extends IrcEvent{
    private String message;
    private boolean isHighlight;

    public IrcEventMessage(Map messagemap){
        setTimestampEid((Long)messagemap.get("eid"));
        this.username = messagemap.get("from").toString();
        this.message = messagemap.get("msg").toString();
        if (messagemap.get("highlight") != null){
            this.isHighlight = true;
        }
        else{
            this.isHighlight = false;
        }
        
    }
    @Override
    public String getEventText() {
        return message;
    }
    
}
