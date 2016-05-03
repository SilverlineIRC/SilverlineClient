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
public class IrcEventQuit extends IrcEvent{

    private String nick;
    private String hostmask;
    private String msg;
    
    public IrcEventQuit(Map quitmap){
        setTimestampEid((Long)quitmap.get("eid"));
        this.nick = quitmap.get("nick").toString();
        this.hostmask = quitmap.get("hostmask").toString();
        this.msg = quitmap.get("msg").toString();
    }
    
    @Override
    public String getEventText() {
        return "Quit: "+nick+" ("+hostmask+") "+msg;
    }
    
}
