package gui.windows.main.renderers;

import api.Core;
import irc.IrcBuffer;
import gui.theme.Colors;
import gui.theme.Theme;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import util.GUIInterface;

/**
 * Custom renderer for rendering network / channel JList.
 * @author kulttuuri
 */
public class NetworkChannelListCellRenderer extends JLabel implements ListCellRenderer {
    
    private GUIInterface guiInterface;
    
    public NetworkChannelListCellRenderer(GUIInterface guiInterface) {
        setOpaque(true);
        this.guiInterface = guiInterface;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        IrcBuffer channel = (IrcBuffer)value;
        
        // Font
	// TODO: Move to mainIrcGui clas
        setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        
        // Status channel for the network (for ex. Quakenet or Ircnet)
        if (channel.isStatusChannel()) {
            setText(Core.getNetwork(channel.cid).getNetworkName());
            setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/icons/world.png")));
            setBackground(Theme.NETWORK_PANEL_STATUS_BACKGROUND);
            setForeground(Theme.NETWORK_PANEL_STATUS_TEXT);
        }
        // Channel (for ex. #channel)
        else {
            setText("     " + channel.getBufferName());
            setIcon(null);
            setBackground(Theme.NETWORK_PANEL_CHANNEL_BACKGROUND);
            setForeground(Theme.NETWORK_PANEL_CHANNEL_TEXT);
            if (channel.isArchived()){
                setForeground(Color.GRAY);
            }
        }
        
        // Selected channel
        if (channel == guiInterface.getCurrentSelectedChannel()) {
            setBackground(Theme.NETWORK_PANEL_SELECTED_BACKGROUND);
            setForeground(Theme.NETWORK_PANEL_SELECTED_TEXT);
        }
	
	if (channel != guiInterface.getCurrentSelectedChannel()) {
	    if (channel.hasUnseenMessages()) {
		EmptyBorder border = new EmptyBorder(8, 0, 8, 0);
		setFont(new Font(getFont().getFamily(), Font.BOLD, getFont().getSize()));
		setBorder(new CompoundBorder(new MatteBorder(0, 4, 0, 0, Theme.COLOR_GREEN), border));
	    }
	    else {
		setFont(new Font(getFont().getFamily(), Font.PLAIN, getFont().getSize()));
		EmptyBorder border = new EmptyBorder(8, 10, 8, 0);
		setBorder(border);
	    }
	}

        return this;
    }
}