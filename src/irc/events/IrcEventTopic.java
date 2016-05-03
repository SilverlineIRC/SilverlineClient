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
public class IrcEventTopic extends IrcEvent{

    private String channel;
    private String topictext;
    private String author;
    
    public IrcEventTopic(Map topicmap) {
        this.setEid((Long)topicmap.get("eid"));
        this.channel = topicmap.get("chan").toString();
        this.topictext = topicmap.get("text").toString();
        this.author = topicmap.get("author").toString();
    }

    
    @Override
    public String getEventText() {
        return "Topic for "+channel+": '"+topictext+"' set by "+author;
    }
    
}
