package gui.windows.main.clickmenus;

import api.OutgoingMessages;
import gui.helpers.MessageBox;
import irc.IrcBuffer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * 
 * @author Zuppi, Kulttuuri
 */
public class ChannelListRightClickMenu extends JPopupMenu implements ActionListener {
    
    private IrcBuffer targetChannel;
    JMenuItem leave = null;
    JMenuItem archive = null;
    JMenuItem delete = null;   
    JMenuItem joinChannel = null;
    
    public ChannelListRightClickMenu(IrcBuffer targetChannel) {
        this.targetChannel = targetChannel;
	if (targetChannel.isStatusChannel()) {
	    joinChannel = new JMenuItem("Join Channel");
	    joinChannel.addActionListener(this);
	    add(joinChannel);
	}
	else if (targetChannel.isQueryChannel()){
            if (targetChannel.isArchived()){
                archive = new JMenuItem("Unarchive");
                archive.addActionListener(this);
                add(archive);           
            }
            else{
                archive = new JMenuItem("Archive");
                archive.addActionListener(this);
                add(archive);           
                
            }
            delete = new JMenuItem("Delete");
            delete.addActionListener(this);
            add(delete);
            
        }
	else {
            if (targetChannel.isArchived()) {
                archive = new JMenuItem("Unarchive");
                archive.addActionListener(this);
                add(archive);           
            }
	    else {
                leave = new JMenuItem("Leave");
                leave.addActionListener(this);
                add(leave);  
                archive = new JMenuItem("Archive");
                archive.addActionListener(this);
                add(archive);           
            }
            delete = new JMenuItem("Delete");
            delete.addActionListener(this);
            add(delete);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
	if (ae.getSource() == joinChannel) {
	    String channelToJoin = MessageBox.input("Channel Name", "Enter name of the channel you want to join");
	    System.out.println("USER WANTS TO JOIN CHANNEL: " + channelToJoin);
	}
	else if (ae.getSource() == archive) {
            if (targetChannel.isArchived()){
                OutgoingMessages.unarchiveChannel(targetChannel);
            }
            else{
                OutgoingMessages.archiveChannel(targetChannel);
            }
        }
        else if (ae.getSource() == delete) {
            OutgoingMessages.deleteChannel(targetChannel);
        }
        else if (ae.getSource() == leave) {
            OutgoingMessages.partChannel(targetChannel);
        }
    }
}