package util;

import api.Connectionmanager;
import api.Core;
import api.OutgoingMessages;
import gui.windows.joinNetwork.GUIJoinNetwork;
import gui.windows.loading.GUILoading;
import irc.IrcBuffer;
import irc.IrcNetwork;
import irc.IrcUser;
import gui.windows.main.clickmenus.ChannelListRightClickMenu;
import gui.windows.main.GuiMainIrcView;
import gui.windows.main.IrcGuiTextFormatting;
import gui.windows.main.renderers.NetworkChannelListCellRenderer;
import gui.windows.main.datamodels.NetworkChannelListDatamodel;
import gui.windows.main.renderers.UserListCellRenderer;
import gui.windows.main.datamodels.UserListDataModel;
import gui.windows.main.clickmenus.UserRightClickMenu;
import irc.events.IrcEvent;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.StringReader;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.html.HTMLEditorKit;

/**
 * Class to serve as a interface between IRC engine and GUI interface. Shows /
 * binds the needed data into irc GUI view.
 *
 * @author kulttuuri
 * @author Zuppi
 */
public class GUIInterface implements ActionListener, ListSelectionListener, KeyListener, MouseListener {

    /*** Reference to GUI where data will be shown in. */
    private final GuiMainIrcView gui;
    /*** Reference to current IRC channel that is currently selected in the GUI. */
    private IrcBuffer currentSelectedChannel;
    
    /*** Datamodel for network / channel list. */
    NetworkChannelListDatamodel networkChanListDatamodel;
    /*** Custom renderer for network / channel list. */
    NetworkChannelListCellRenderer networkChanListRenderer;
    
    /*** Datamodel for channel users list. */
    UserListDataModel userlistDatamodel;
    /*** Custom renderer for channel users list. */
    UserListCellRenderer userListCellRenderer;
    
    public GUIInterface(boolean debugMode) {
        gui = new GuiMainIrcView(debugMode, debugMode);

        addActionListenersForGuiComponents();
        addCustomRenderersDatamodelsForGuiComponents();
    }

    private void addActionListenersForGuiComponents() {
        // To listen for enter presses in channel textfield
        gui.getChatTextfield().addKeyListener(this);
        gui.getChatTextfield().setFocusTraversalKeysEnabled(false);
	
        // To listen for selections in network / channel list
        gui.getNetworkChannels().addListSelectionListener(this);
	
        // To listen for mouse clicks on channel list
        gui.getNetworkChannels().addMouseListener(this);
	
        // To listen for changes in channel user list
        gui.getChannelUsers().addListSelectionListener(this);
	
        // To listen for mouse clicks on channel user list
        gui.getChannelUsers().addMouseListener(this);
	
	// To listen for mouse clicks on leave channel / network / conversation button
	gui.getButtonLeaveChannel().addActionListener(this);
	
	// To listen for mouse clicks on reconnect button
	gui.getButtonReconnect().addActionListener(this);
	
	// To listen for mouse clicks on channel settings button
	gui.getButtonChannelSettings().addActionListener(this);
	
	gui.getButtonAddNetwork().addMouseListener(this);
    }

    private void addCustomRenderersDatamodelsForGuiComponents() {
        // Set custom datamodel & renderer for network / channel list
        networkChanListDatamodel = new NetworkChannelListDatamodel();
        gui.getNetworkChannels().setModel(networkChanListDatamodel);
        networkChanListRenderer = new NetworkChannelListCellRenderer(this);
        gui.getNetworkChannels().setCellRenderer(networkChanListRenderer);

        // Set custom datamodel & renderer for channel userlist
        userlistDatamodel = new UserListDataModel();
        gui.getChannelUsers().setModel(userlistDatamodel);
        userListCellRenderer = new UserListCellRenderer(this);
        gui.getChannelUsers().setCellRenderer(userListCellRenderer);
    }

    public void showIrcGui(boolean setVisible) {
        gui.setVisible(setVisible);
    }
    
    public void showGuiJoinNetwork() {
	currentSelectedChannel = null;
    }

    public void ircChannelEventReceived(IrcBuffer channel, IrcEvent event) {
        if (channel == currentSelectedChannel) {
            addMessageToCurrentChannel(event);
        }
    }

    public IrcBuffer getCurrentSelectedChannel() {
        return currentSelectedChannel;
    }

    public void connectionToIrccloudCompleted(List<IrcNetwork> networks, IrcBuffer lastOpenChannel) {
        addNetworksChannels(networks);
        changeCurrentChannel(lastOpenChannel);
    }

    public void changeCurrentChannel(IrcBuffer newChannel) {
        if (currentSelectedChannel == newChannel) {
            return;
        }
        
        if (currentSelectedChannel != null){
             currentSelectedChannel.setEidAsLatest();
        }
       
        this.currentSelectedChannel = newChannel;

        if (newChannel.isStatusChannel()) {
            gui.getChannelUsersScrollPanel().setVisible(false);
	    gui.getButtonLeaveChannel().setToolTipText("Quit Network");
        }
	else if (newChannel.isQueryChannel()) {
	    gui.getChannelUsersScrollPanel().setVisible(false);
	    gui.getButtonLeaveChannel().setToolTipText("Leave Conversation");
	}
	else {
	    gui.getButtonLeaveChannel().setToolTipText("Leave Channel");
            userlistDatamodel.removeAllUsersFromView();
            userlistDatamodel.addUsersToView(newChannel.members);
            gui.getChannelUsersScrollPanel().setVisible(true);
        }
	
	if (newChannel.isArchived()) {
	    gui.getButtonLeaveChannel().setVisible(false);
	    gui.getButtonReconnect().setVisible(true);
	}
	else {
	    gui.getButtonLeaveChannel().setVisible(true);
	    gui.getButtonReconnect().setVisible(true);
	}

        // Add messages to channel
        gui.getChatArea().setText("");
        if (newChannel.channelEvents.size() > 0) {
            addMessagesToCurrentChannel(newChannel.channelEvents);
        }

        updateChannelTopic();

        gui.getMainFrame().getContentPane().validate();
        gui.getMainFrame().getContentPane().repaint();
	gui.getChatTextfield().requestFocus();
        Connectionmanager.sendHeartBeat(newChannel);
    }

    public void addMessagesToCurrentChannel(List<IrcEvent> eventMessages) {
        for (IrcEvent ircEvent : eventMessages) {
            addMessageToCurrentChannel(ircEvent);
        }
    }

    /**
     * Adds message to current channel.
     */
    public void addMessageToCurrentChannel(IrcEvent ircEvent) {
        String message = IrcGuiTextFormatting.getFormattedMessage(ircEvent);
        HTMLEditorKit editor = (HTMLEditorKit) gui.getChatArea().getEditorKit();
        StringReader reader = new StringReader(message);

        try {
            editor.read(reader, gui.getChatArea().getDocument(), gui.getChatArea().getDocument().getLength());
            gui.getChatArea().setCaretPosition(gui.getChatArea().getDocument().getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("ADDING MESSAGE TO GUI: " + ircEvent.getEventText());
    }

    public void removeAllCurrentChannelMessages() {
        gui.getChatTextfield().setText("");
    }

    public void addUserToCurrentChannel(IrcUser userToBeAdded) {
        userlistDatamodel.addUserToView(userToBeAdded);
    }

    public void addUsersToCurrentChannel(List<IrcUser> usersToBeAdded) {
        // ... Datamodel
    }

    public void removeUserFromCurrentChannel(IrcUser userToBeRemoved) {
        userlistDatamodel.removeUserFromView(userToBeRemoved);
    }

    public void removeAllCurrentChannelUsers() {
        gui.getChannelUsers().removeAll();
    }

    public void changeUserNickInCurrentChannel(String oldnick, String newnick) {
        userlistDatamodel.changeUserNick(oldnick, newnick);
    }

    public void changeUserModeInCurrentChannel(String nick, String newmode) {
        userlistDatamodel.changeUserMode(nick, newmode);
    }

    public void addNetworkChannel(IrcBuffer channel) {
        networkChanListDatamodel.addChannelToView(channel);
        changeCurrentChannel(channel);
    }

    public void addNetworksChannels(List<IrcNetwork> listOfNetworks) {
        for (IrcNetwork network : listOfNetworks) {
            /*for (IrcBuffer networkChannel : network.channels) {
             System.out.println("Adding channel to GUI view " + networkChannel.getBufferName());
             networkChanListDatamodel.addChannelToView(networkChannel);
             }*/
            networkChanListDatamodel.addChannelsToView(network.networkBuffers);
        }
    }

    public void removeAllCurrentNetworkChannels() {
        gui.getNetworkChannels().removeAll();
    }
    
    /**
     * Removes channel from GUI view.
     * @param channel IrcBuffer to be removed from GUI view.
     */
    public void removeChannel(IrcBuffer channel) {
        networkChanListDatamodel.removeChannelFromView(channel);
    }
    
    public void archiveChannel(IrcBuffer channel){
        networkChanListDatamodel.archiveChannelInView(channel);
    }
    
    public void unarchiveChannel(IrcBuffer channel){
        networkChanListDatamodel.unarchiveChannelInView(channel);
    }

    /**
     * Updates channel topic into channel GUI.
     */
    public void updateChannelTopic() {
	if (IrcGuiTextFormatting.getFormattedTitle(currentSelectedChannel).length() > 140) {
	    gui.getChannelTitleLabel().setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
	}
	else {
	    gui.getChannelTitleLabel().setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
	}
	gui.getChannelTitleLabel().setText(IrcGuiTextFormatting.getFormattedTitle(currentSelectedChannel));
        gui.getEditorPaneTopic().setText(IrcGuiTextFormatting.getFormattedTitle(currentSelectedChannel));
    }

    public void setCurrentNickname(String currentNickname) {
        // TODO: Implement or remove if not needed anymore.
    }
    
    public void showLoadingGui(String text) {
	gui.setVisible(false);
	GUILoading.showLoadingGUI(text);
    }
    
    public void hideLoadingGui() {
	gui.setVisible(true);
	GUILoading.hideLoadingGUI();
    }
    
    public void loadingComplete() {
	// ...
    }
    
    public void setLoadProgress(int progress) {
	// ...
    }
    
    // ##################################
    // # ACTIONLISTENER IMPLEMENTATIONS #
    // ##################################
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println("Action performed where source was: " + e.getSource());
	if (e.getSource() == gui.getButtonLeaveChannel()) {
	    if (currentSelectedChannel.isStatusChannel()) {
		System.out.println("QUIT NETWORK!");
	    }
	    else if (currentSelectedChannel.isQueryChannel()) {
		System.out.println("LEAVE CONVERSATION!");
	    }
	    else {
                OutgoingMessages.partChannel(currentSelectedChannel);
		System.out.println("QUIT CHANNEL!");
	    }
	}
	else if (e.getSource() == gui.getButtonChannelSettings()) {
	    System.out.println("OPEN CHANNEL SETTINGS DROPDOWN MENU!");
	}
	else if (e.getSource() == gui.getButtonReconnect()) {
            OutgoingMessages.joinChannel(Core.getNetwork(currentSelectedChannel.cid), currentSelectedChannel.buffername, "");
            
	    System.out.println("RECONNECT!");
	}
    }

    // #########################################
    // # LISTSELECTIONLISTENER IMPLEMENTATIONS #
    // #########################################
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        //System.out.println("List selection performed where source was: " + e.getSource());
        if (!e.getValueIsAdjusting() && e.getSource() == gui.getNetworkChannels()) {
            changeCurrentChannel(networkChanListDatamodel.getChannelInViewIndex(gui.getNetworkChannels().getSelectedIndex()));
        } else if (!e.getValueIsAdjusting() && e.getSource() == gui.getChannelUsers()) {
            //System.out.println(userlistDatamodel.getUserInViewIndex(gui.getChannelUsers().getSelectedIndex()).nick);
        }
    }

    // ###############################
    // # KEYLISTENER IMPLEMENTATIONS #
    // ###############################
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        if (e.getSource() == gui.getChatTextfield() && !gui.getChatTextfield().getText().equals("")){
            if (e.getKeyCode() == KeyEvent.VK_ENTER){
                try {
                OutgoingMessages.sendMessage(currentSelectedChannel, gui.getChatTextfield().getText());
                gui.getChatTextfield().setText("");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else if (e.getKeyCode() == KeyEvent.VK_TAB){
                try{
                    if (!NickAutocompleter.isAutocompleteActive()){
                        NickAutocompleter.getAutocompletes(currentSelectedChannel.members, gui.getChatTextfield().getText());
                    }                 
                    gui.getChatTextfield().setText(NickAutocompleter.getAutocomplete());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else{
                if (NickAutocompleter.isAutocompleteActive()){
                    NickAutocompleter.deactivateAutocomplete();
                }
            }
        }
    }

    // #################################
    // # MOUSELISTENER IMPLEMENTATIONS #
    // #################################
    
    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        if (me.getSource() == gui.getChannelUsers()) {
            int rownum = gui.getChannelUsers().locationToIndex(me.getPoint());
            if (!userlistDatamodel.getUserInViewIndex(rownum).isUserListSeparator()) {
                if (me.getButton() == MouseEvent.BUTTON1) {
                    if (me.getClickCount() == 2) {
                        String nick = userlistDatamodel.getUserInViewIndex(rownum).nick;
                        OutgoingMessages.sendMessage(currentSelectedChannel, "/query " + nick);
                    }
                } 
                else if (me.getButton() == MouseEvent.BUTTON3) {
                    UserRightClickMenu usermenu = new UserRightClickMenu(currentSelectedChannel, userlistDatamodel.getUserInViewIndex(rownum));
                    usermenu.show(me.getComponent(), me.getX(), me.getY());
                }
            }
        } else if (me.getSource() == gui.getNetworkChannels()) {
            int rownum = gui.getChannelUsers().locationToIndex(me.getPoint());
            if (me.getButton() == MouseEvent.BUTTON1) {
                
            } 
            else if (me.getButton() == MouseEvent.BUTTON3) {
                ChannelListRightClickMenu channelmenu = new ChannelListRightClickMenu(networkChanListDatamodel.getChannelInViewIndex(rownum));
                channelmenu.show(me.getComponent(), me.getX(), me.getY());
            }
        }
	else if (me.getSource() == gui.getButtonAddNetwork()) {
	    gui.setVisible(false);
	    GUIJoinNetwork joinNet = new GUIJoinNetwork(gui);
	    joinNet.setVisible(true);
	}
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
}