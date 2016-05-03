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
public class IrcEventChannelMode extends IrcEvent {

    private String modes;
    private String channel;
    
    public IrcEventChannelMode(Map modemap) {
        setEid((Long)modemap.get("eid"));
        this.modes = modemap.get("newmode").toString();
        this.channel = modemap.get("channel").toString();
    }
    

    @Override
    public String getEventText() {
        return "Modes for "+channel+": "+modes;
    }
    
}
