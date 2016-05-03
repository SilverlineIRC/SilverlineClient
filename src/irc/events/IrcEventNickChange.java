/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package irc.events;

/**
 *
 * @author Zuppi
 */
public class IrcEventNickChange extends IrcEvent{
    
    private String oldnick;
    private String newnick;
    
    public IrcEventNickChange(Long eid, String oldnick, String newnick){
        setTimestampEid(eid);
        this.oldnick = oldnick;
        this.newnick = newnick;
    }

    @Override
    public String getEventText() {
        return oldnick+" is now known as "+newnick;
    }
    
}
