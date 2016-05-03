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
public class IrcEventUserModeChange extends IrcEvent{
    
    private String changer;
    private String modediff;
    private String target;
        
    public IrcEventUserModeChange(Map modemap, String changer){
        setTimestampEid((Long)modemap.get("eid"));
        this.target = modemap.get("nick").toString();
        this.changer = changer;
        this.modediff = modemap.get("diff").toString();
    }

    @Override
    public String getEventText() {
        return changer+" ["+modediff+"] "+target;
    }
    
}
