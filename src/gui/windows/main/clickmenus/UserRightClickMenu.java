/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.windows.main.clickmenus;

import api.OutgoingMessages;
import irc.IrcBuffer;
import irc.IrcUser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
//import main.IrcEngine;
import util.GUIInterface;

/**
 * Class for showing the pop up menu when user rightclicks an other other user
 * from userlist
 *
 * @author Zuppi
 */
public class UserRightClickMenu extends JPopupMenu implements ActionListener {

   // private IrcEngine engine;
    private IrcBuffer targetChannel;
    private IrcUser targetUser;
    JMenuItem query = null;
    JMenuItem whois = null;
    JMenuItem op = null;
    JMenuItem voice = null;

    public UserRightClickMenu(IrcBuffer targetChannel, IrcUser targetUser) {
       //this.engine = engine;
        this.targetChannel = targetChannel;
        this.targetUser = targetUser;
        query = new JMenuItem("Query", new ImageIcon(getClass().getResource("/gui/icons/comment.png")));
        query.addActionListener(this);
        add(query);
        whois = new JMenuItem("Whois");
        whois.addActionListener(this);
        add(whois);
        JSeparator sepa1 = new JSeparator();
        add(sepa1);

        if (targetUser.isOpped()) {
            op = new JMenuItem("Take ops");
        } else {
            op = new JMenuItem("Give ops");
        }
        op.addActionListener(this);
        add(op);
        if (targetUser.isVoiced()) {
            voice = new JMenuItem("Take voice");
        } else {
            voice = new JMenuItem("Give voice");
        }
        voice.addActionListener(this);
        add(voice);



    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == query) {
            OutgoingMessages.sendMessage(targetChannel, "/query " + targetUser.nick);
        } 
        else if (ae.getSource() == whois) {
            OutgoingMessages.requestWhois(targetChannel.cid, targetUser.nick);
        } 
        else if (ae.getSource() == op) {
            if (targetUser.isOpped()) {
                OutgoingMessages.sendMessage(targetChannel, "/mode " + targetChannel.getBufferName() + " -o " + targetUser.nick);
            } 
            else {
                OutgoingMessages.sendMessage(targetChannel, "/mode " + targetChannel.getBufferName() + " +o " + targetUser.nick);
            }         
        }
        else if (ae.getSource() == voice){
            if (targetUser.isVoiced()) {
                OutgoingMessages.sendMessage(targetChannel, "/mode " + targetChannel.getBufferName() + " -v " + targetUser.nick);
            } 
            else {
                OutgoingMessages.sendMessage(targetChannel, "/mode " + targetChannel.getBufferName() + " +v " + targetUser.nick);
            }
        }
    }
}
