/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package irc;

import java.util.Map;

/**Class that hold the information for a Irc user, not a client user but an other chatter in the network */
public class IrcUser {
    /**Irc User information*/
    public String nick;
    public String realname;
    public String ircserver;
    public String mode;
    public boolean isAway;
    public boolean isSeparator;
    
    /**
     * Constructor to create a irc user
     * @param nick Users nickanme
     * @param realname Users realname
     * @param ircserver Users irc server
     * @param mode Users modes
     * @param isAway Is user away
     */
    public IrcUser(String nick, String realname, String ircserver, String mode, boolean isAway, boolean isSeparator){
        this.nick = nick;
        this.realname = realname;
        this.ircserver = ircserver;
        this.mode = mode;
        this.isAway = isAway;
        this.isSeparator = isSeparator;
        
    }
    
    /** @param nick The new nickname for the ircuser*/
    public void changeNick(String nick){
        this.nick = nick;
    }
    
    public void setModes(String newmode){
        this.mode = newmode;
    }
    /** @return Is the user opped? */  
    public boolean isOpped(){
        return mode.contains("o");
    }
    /** @return Is the user voiced? */  
    public boolean isVoiced(){
        return mode.contains("v");
    }
    /**@return Is the user a user list separator?*/
    public boolean isUserListSeparator(){
        return isSeparator;
    }
}
