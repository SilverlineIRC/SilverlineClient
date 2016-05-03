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
public class IrcEventChannelModeChange extends IrcEvent {

    private String changer;
    private String newmode;
    private String channel;
    
    public IrcEventChannelModeChange(Map modemap, String changer){
        setTimestampEid((Long)modemap.get("eid"));
        this.channel = modemap.get("channel").toString();
        this.changer = changer;
        this.newmode = modemap.get("newmode").toString();
    }
    
    
    @Override
    public String getEventText() {
        return channel+ "modes set to "+newmode+" by "+changer;
    }
    
}
