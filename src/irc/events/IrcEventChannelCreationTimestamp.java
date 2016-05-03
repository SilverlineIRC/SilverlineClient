/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package irc.events;

import util.DatetimeConverter;

/**
 *
 * @author Zuppi
 */
public class IrcEventChannelCreationTimestamp extends IrcEvent{

    private String creationTimestamp;
    
    public IrcEventChannelCreationTimestamp(Long eid, Long unixstamp) {
        this.eid = eid;
        this.creationTimestamp = DatetimeConverter.convertUnixEpoch(unixstamp);
    }

    @Override
    public String getEventText() {
        return "Channel created on: "+creationTimestamp;
    }
    
}
