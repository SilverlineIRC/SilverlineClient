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
public class IrcEventTopicChange extends IrcEvent{

    private String channel;
    private String topic;
    private String author;
    
    public IrcEventTopicChange(Map topicmap){
        setTimestampEid((Long)topicmap.get("eid"));
        this.channel = topicmap.get("chan").toString();
        this.topic = topicmap.get("topic").toString();
        this.author = topicmap.get("author").toString();
    }
    @Override
    public String getEventText() {
        return author+" changed topic of "+channel+" to "+topic;
    }
    
}
