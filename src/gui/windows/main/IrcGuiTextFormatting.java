package gui.windows.main;

import irc.IrcBuffer;
import irc.events.IrcEvent;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Contains static classes to set the formatting for certain IRC texts (like topic, message...)
 * @author kulttuuri
 */
public class IrcGuiTextFormatting {
    
    public static String getFormattedTitle(IrcBuffer channel) {
        if (channel.isQueryChannel()) {
            return ""
                + "<html>"
                + "Private conversation with <b>" + channel.getBufferName() + "</b>"
                + "</html>"; 
        }
        else if (channel.isStatusChannel()) {
            return "";
        }
        else {
            return ""
                + "<html>"
                + "<b>" + channel.getBufferName() + "</b>"
                + "&nbsp;" + channel.getTopic()
                + "</html>";
        }
    }
    
    public static String getFormattedMessage(IrcEvent event)
    {
        String msgStyle = "padding-left: 7px; margin-top: 5px; font-size: 14px;";
        if (event instanceof irc.events.IrcEventQuit || event instanceof irc.events.IrcEventJoin || event instanceof irc.events.IrcEventChannelModeChange || event instanceof irc.events.IrcEventJoin || event instanceof irc.events.IrcEventUserModeChange)
        {
            msgStyle = "padding-left: 7px; margin-top: 8px; margin-bottom: 3px; font-size: 13px; color: gray;";
        }
        
        String timestamp = "<span>" + event.getEventTimestamp() + "</span>&nbsp;&nbsp;";
        String username = event.getUsername();
        username = "<b>" + username + "</b>&nbsp;";
        String message = event.getEventText();
        message = "<p style='"+msgStyle+"'>" + timestamp + username + StringEscapeUtils.escapeHtml4(message) + "</p>";
        return message;
    }
}