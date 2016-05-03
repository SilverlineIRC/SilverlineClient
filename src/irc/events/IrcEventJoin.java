/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package irc.events;

/**
 *
 * @author Zuppi
 */
public class IrcEventJoin extends IrcEvent{

    public String nick;
    private String hostmask;
    
    public IrcEventJoin(Long eid, String nick, String hostmask){
        setTimestampEid(eid);
        this.nick = nick;
        this.hostmask = hostmask;
    }
    @Override
    public String getEventText() {
        return "Join: "+nick+" ("+hostmask+")";
    }
    
}
