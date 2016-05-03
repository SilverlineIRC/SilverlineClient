package gui.windows.main.renderers;

import irc.IrcBuffer;
import irc.IrcUser;
import gui.theme.Colors;
import gui.theme.Theme;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import util.GUIInterface;

/**
 * Custom renderer for rendering channel userlist.
 * @author kulttuuri
 */
public class UserListCellRenderer extends JLabel implements ListCellRenderer {
    
    private GUIInterface guiInterface;
    
    public UserListCellRenderer(GUIInterface guiInterface) {
        setOpaque(true);
        this.guiInterface = guiInterface;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        IrcUser user = (IrcUser)value;
        
        // Add bit spacing between list items
        setBorder(new EmptyBorder(8, 8, 8, 0));
	// TODO: Move to IrcMainGui Class
        // Font
        setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        
        // Group Header
        if (user.realname.equals("Ops") || user.realname.equals("Voiced") || user.realname.equals("Members")) {
            setBackground(Theme.USERS_PANEL_MEMBERS_HEADER_BACKGROUND);
            setForeground(Theme.USERS_PANEL_MEMBERS_HEADER_TEXT);
            setText(user.nick);
        }
        // Username
        else {
            setBackground(Theme.USERS_PANEL_USERNAME_BACKGROUND);
            setForeground(Theme.USERS_PANEL_USERNAME_TEXT);
            setText(" " + user.nick);
        }

        return this;
    }
}