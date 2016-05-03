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
public class IrcEventKick extends IrcEvent{

    private String kicker;
    private String msg;
    private String kicked;
    public IrcEventKick(Map kickmap){
        setTimestampEid((Long)kickmap.get("eid"));
        this.kicker = kickmap.get("kicker").toString();
        this.msg = kickmap.get("msg").toString();
        this.kicked = kickmap.get("nick").toString();
    }
    @Override
    public String getEventText() {
        return kicker+" kicked "+kicked+" "+msg;
    }
    
}
