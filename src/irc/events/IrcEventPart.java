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
public class IrcEventPart extends IrcEvent {

    private String nick;
    private String hostmask;
    private String msg;
    
    public IrcEventPart(Map partmap){
        setEid((Long)partmap.get("eid"));
        this.nick = partmap.get("nick").toString();
        this.hostmask = partmap.get("hostmask").toString();
        this.msg = partmap.get("msg").toString();
    }
    @Override
    public String getEventText() {
        return "Part: "+nick+" "+hostmask+" "+msg;
    }
    
}
