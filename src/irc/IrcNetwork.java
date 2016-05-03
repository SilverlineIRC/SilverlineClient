/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package irc;

import api.Core;
import irc.IrcBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**Class that stores Irc Network information*/
public class IrcNetwork {
    
    
    
    /**Variables holding the network information*/   
    public String cid;
    public String hostname;
    public String port;
    public Boolean  ssl;
    public String connectionName;
    public String userNick;
    public String userRealname;
    //public String serverPass;
    public String nickservPass;
    public String perform;
    //public ArrayList<String> ignores;
    public Boolean away;
    public String status;
    public String failinfo;
    public String latency;
    public String ircserver;
    public String identprefix;
    public String connusername;
    public String userhost;
    public String usermask;
    public int buffercount;
    
    /**Holds the channels that the user has joined in the network */
    public List<IrcBuffer> networkBuffers;
    
    /**
     * Constructor to create the IrcNetwork
     * @param networkMap Map containing the network info
     */
    public IrcNetwork(Map networkMap){
        cid = networkMap.get("cid").toString();
        hostname = networkMap.get("hostname").toString();
        port = networkMap.get("port").toString();
        ssl = Boolean.valueOf(networkMap.get("ssl").toString());
        connectionName = networkMap.get("name").toString();
        userNick = networkMap.get("nick").toString();
        userRealname = networkMap.get("realname").toString();
        nickservPass = networkMap.get("nickserv_pass").toString();
        if (networkMap.get("join_commands") != null){
            perform = networkMap.get("join_commands").toString();
        }      
        //Ignores requires converting the string to array, TODO
        away = Boolean.valueOf(networkMap.get("away").toString());
        status = networkMap.get("status").toString();
        failinfo = networkMap.get("fail_info").toString();
        //latency = networkMap.get("lag"); Needs to be checked for non-integer values (ie. undefined)
        ircserver = networkMap.get("ircserver").toString();
        identprefix = networkMap.get("ident_prefix").toString();
        connusername = networkMap.get("user").toString();
        userhost = networkMap.get("userhost").toString();
        usermask = networkMap.get("usermask").toString();
        buffercount = Integer.parseInt(networkMap.get("num_buffers").toString());
        networkBuffers = new ArrayList<IrcBuffer>();
        System.out.println("SERVER "+hostname+" CREATED WITH CID: "+cid);
    }
    
    /**
     * @return Network name
     */
    public String getNetworkName(){
        return connectionName;
    }
    /**
     * Adds a normal channel to the network
     * @param channel Channel  to be added
     */
    public void addBuffer(IrcBuffer channel){
        if (channel.isStatusChannel()){
            networkBuffers.add(0, channel);
            System.out.println("STATUS CHANNEL CREATED FOR "+hostname);
        }
        else{
            networkBuffers.add(channel);
             System.out.println("CHANNEL "+channel.buffername+" ADDED TO "+hostname);
        }       
        /*System.out.println(hostname);
        for (int i=0;i<networkChannels.size();i++){
            System.out.println(networkChannels.get(i).buffername);
        }*/
        
    }
    
    /**
     * Returns a channel that responds to the given buffer id
     * @param bid Buffer id of the channel
     * @return The channel if it's found, null otherwise
     */
    public IrcBuffer getBuffer(String bid){
        for (IrcBuffer c : networkBuffers){
            if (c.bid.equals(bid)){
                return c;
            }
        }
        return null;
    }
    
    public List<IrcBuffer> getAllBuffers(){
        return networkBuffers;
    }
    
    /**
     * Removes a channel from the network
     * @param removeChannel Channel to be removed
     */
    public void removeBuffer(IrcBuffer removeChannel){
        networkBuffers.remove(removeChannel);
    }
    
    
    
    /**
     * Changes the users nickname in the network
     * @param oldnick Users old nickname
     * @param newnick Users new nickname
     */
    public void changeIrcUserNick(String oldnick, String newnick){
        if (oldnick.equals(userNick)){
            userNick = newnick;
        }
        for (IrcBuffer c : networkBuffers){
            for (IrcUser u : c.members){
                if (u.nick.equals(oldnick)){
                    u.changeNick(newnick);
                }
            }
        }
        
    }
    
    public void changeNetworkStatus(String newStatus){
        System.out.println("STATUS OF "+hostname+" CHANGED TO: "+newStatus);
        this.status = newStatus;
        
    }
    
    
    
    
}
