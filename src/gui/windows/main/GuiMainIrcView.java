package gui.windows.main;

import gui.components.ThemeButton;
import gui.helpers.ComponentDragMovement;
import gui.theme.Theme;
import gui.windows.main.listeners.MouseHoverColorListener;
import gui.windows.main.panels.PanelTopbar;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import net.miginfocom.swing.MigLayout;

/**
 * Contains the main IRC GUI.
 * @author kulttuuri
 */
public class GuiMainIrcView {
    
    /** Main panel containing the whole GUI. */
    private JFrame frame;
    /** Top titlebar. */
    private JPanel titleBarPanel;
    /** Chat area for the channel. */
    private JEditorPane panelChatArea;
    /** Textfield where users can type message into the channel. */
    private JTextField chatTextField;
    /** List containing all users in the channel. */
    private JList listChannelUsers;
    /** List containing all user's networks and channels. */
    private JList listNetworkChannels;
    /** Scrollpanel where channel users list is wrapped inside. */
    private JScrollPane channelUsersScrollpanel;
    /** Scrollpanel where networkchannel list is wrapped inside. */
    private JScrollPane networkChannelScrollPanel;
    private JPanel panelChannelInfo;
    /** Contains the channel name and topic. */
    private JEditorPane editorpaneTopic;
    private JLabel channelTitle;
    private JButton buttonChannelLeave;
    private JButton buttonReconnect;
    private JButton buttonChannelSettings;
    private JButton buttonAddNetwork;
    
    private JPanel panelJoinNetwork;
    
    private Color backgroundColor = new Color(20,61,94);

    /** If this is enabled, all components will be prefilled with debug data. */
    private static boolean useDebugData = true;
    
    /**
     * Constructor to create the main IRC view GUI.
     * @param setVisible True if you want to instantly show the GUI, otherwise false.
     * @param useDebugData set to true if you want to make all components show debug data on initialization.
     */
    public GuiMainIrcView(boolean setVisible, boolean useDebugData) {
        GuiMainIrcView.useDebugData = useDebugData;
        createGUI();
        // Show the window
        frame.pack();
        frame.setVisible(setVisible);
        // Center window
        frame.setLocationRelativeTo(null);
	// Set focus to message textfield
	getChatTextfield().requestFocus();
    }
    
    /**
     * Shows or hides this GUI.
     * @param state  true to set visible, false otherwise.
     */
    public void setVisible(boolean state) {
        getMainFrame().setVisible(state);
    }
    
    /**
     * Creates the main IRC GUI view.
     */
    private void createGUI() {
        // Main JFrame
        createGUIMainFrame();
	
        // Titlebar
        createGUITitleTopBar();
	
        // Left networks & channels panel
        createGUIPanelNetworksChannels();
	
        // Right channel panel
        createGUIPanelChannel();
	
	// Right "Add Network" panel
	createGUIJoinNetwork();
    }
    
    /**
     * Creates main JFrame for the window.
     */
    private void createGUIMainFrame() {
        frame = new JFrame("Silverline");
	// Hides titlebar and ability to drag and resize the window. We'll handle those ourself.
        frame.setUndecorated(true);
	
        getMainFrame().setLayout(new MigLayout(
	    "insets 0",
	    "",
	    ""
	));
        
        getMainFrame().getContentPane().setBackground(backgroundColor);
        getMainFrame().setBackground(backgroundColor);
        getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * Creates title topbar for the application.
     */
    private void createGUITitleTopBar() {
        titleBarPanel = new PanelTopbar(new MigLayout(
                "",         // Constraints
                "100%",     // Grow column to 100% width
                "40px!"     // Row height hardcoded
        ));
        frame.add(titleBarPanel, "span");
	
	// Add drag support for titlebar so that user can move the window
	// around by clicking and holding it
        ComponentDragMovement.addDragMovementForComponent(titleBarPanel, frame);
    }
    
    /**
     * Creates the left panel which contains networks and channels.
     */
    private void createGUIPanelNetworksChannels() {
        JPanel panelLeft = new JPanel(new MigLayout("insets 0 4 4 0", "", ""));
        panelLeft.setBackground(backgroundColor);
        getMainFrame().add(panelLeft, "width 180!, height 500:500:, grow");
        
        // Content for the panel
	    if (useDebugData) listNetworkChannels = new JList(new Object[] { "Quakenet", "#tiko-jns", "#bcns09", "Espernet", "#mcp-modding" });
	    else listNetworkChannels = new JList();
	    listNetworkChannels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    listNetworkChannels.setBackground(Theme.NETWORK_PANEL_BACKGROUND);
	    networkChannelScrollPanel = new JScrollPane(getNetworkChannels());
	    networkChannelScrollPanel.setBorder(null);
	    panelLeft.add(networkChannelScrollPanel, "width 100%, height 100%");

	    buttonAddNetwork = new ThemeButton("Add Network", Color.white, Theme.NETWORK_PANEL_BACKGROUND);
	    buttonAddNetwork.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/icons/add.png")));
	    buttonAddNetwork.addMouseListener(new MouseHoverColorListener(buttonAddNetwork, Color.white, Theme.COLOR_GREEN));
	    panelLeft.add(buttonAddNetwork, "dock south, align left");
    }
    
    /**
     * Creates the right channel panel containing channel messages, users and textfield to send a message.
     */
    private void createGUIPanelChannel() {
        JPanel panelRight = new JPanel(new MigLayout("insets 0 0 0 4"));
        panelRight.setBackground(backgroundColor);
        getMainFrame().add(panelRight, "grow, hidemode 3");
        
        // Content
	    
	    // Top channel info panel
            createGUIChannelInfo();
            panelRight.add(panelChannelInfo, "span");
       
            // Chat area for the channel
            panelChatArea = new JEditorPane();
            panelChatArea.setBackground(backgroundColor);
            getChatArea().setContentType("text/html");
            getChatArea().setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
            getChatArea().setEditable(false);
            getChatArea().setBackground(Theme.CHAT_PANEL_BACKGROUND);
            JScrollPane chatScrollPanel = new JScrollPane(getChatArea());
            chatScrollPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
            chatScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            chatScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            if (useDebugData) {
                getChatArea().setText("<p style='padding-left: 5px;'><b style='color: #AAAAB8; font-size: 80%; line-height: 1.6'>13:06</b> <b style='font-weight: bold; line-height: 1.25;'>Zuppi</b> testi testi</p><p><small>13:06</small> Kulttuuri testilinkki: www.google.com</p>");
            }
            panelRight.add(chatScrollPanel, "grow, width 600px:600px:, height 500px:500px:");

            // Right list containing users in the channel
            if (useDebugData) listChannelUsers = new JList(new Object[] { "Ops", "Zuppi", "Kulttuuri", "Members", "testaaja" });
            else listChannelUsers = new JList();
            channelUsersScrollpanel = new JScrollPane(getChannelUsers());
            channelUsersScrollpanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
            listChannelUsers.setBackground(Theme.USERS_PANEL_BACKGROUND);
            panelRight.add(channelUsersScrollpanel, "width 150!, height 500:500:, hidemode 3");

            // Bottom panel for chat textfield
            JPanel bottomPanel = new JPanel(new MigLayout(
		    "insets 0 0 4 0",
		    "100%",
		    ""));
            bottomPanel.setBackground(backgroundColor);
            panelRight.add(bottomPanel, "newline, span, growx, pushy");

	    // Bottom textfield for sending messages to channel
	    chatTextField = new JTextField();
	    Border matteBorder = BorderFactory.createMatteBorder(0, 0, 0, 0, Theme.NETWORK_PANEL_BACKGROUND);
	    Border innerPaddingBorder = BorderFactory.createEmptyBorder(2, 5, 2, 5);
	    chatTextField.setBorder(BorderFactory.createCompoundBorder(matteBorder, innerPaddingBorder));
	    bottomPanel.add(getChatTextfield(), "grow");
    }
    
    private void createGUIChannelInfo() {
        panelChannelInfo = new JPanel(new MigLayout(
                "gap 0, insets 4 11 4 13",  // Constraints
                "100%",			    // Grow column to 100% width
                "55px!"			    // Row height hardcoded
	));
        panelChannelInfo.setBackground(Theme.CHANNEL_INFO_BACKGROUND);
	panelChannelInfo.setForeground(Theme.CHANNEL_INFO_TEXT);
        // Content
            
            // JEditorPane containing the channel name and topic
            editorpaneTopic = new JTextPane();
            editorpaneTopic.setContentType("text/html");
            editorpaneTopic.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
            editorpaneTopic.setEditable(false);
            
            JScrollPane chatScrollPanel = new JScrollPane(editorpaneTopic);
            chatScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            if (useDebugData) { editorpaneTopic.setText("#Silverline"); }
	    editorpaneTopic.setBackground(Theme.CHANNEL_INFO_BACKGROUND);
	    //panelChannelInfo.add(editorpaneTopic, "grow");
            
            channelTitle = new JLabel("");
	    channelTitle.setForeground(Color.white);
            panelChannelInfo.add(channelTitle, "grow");
            
            // Leave Channel / Quit Conversation / Quit Network Button
            buttonChannelLeave = new ThemeButton("Leave", Color.white, Theme.NETWORK_PANEL_STATUS_BACKGROUND);
	    buttonChannelLeave.addMouseListener(new MouseHoverColorListener(buttonChannelLeave, Color.white, Theme.COLOR_RED));
            panelChannelInfo.add(buttonChannelLeave, "align right, split");
	    
	    // Reconnect Button
            buttonReconnect = new ThemeButton("Reconnect", Color.white, Theme.NETWORK_PANEL_STATUS_BACKGROUND);
	    buttonReconnect.addMouseListener(new MouseHoverColorListener(buttonReconnect, Color.white, Theme.COLOR_GREEN));
            panelChannelInfo.add(buttonReconnect, "align right");
	    
	    // Settings Button
            buttonChannelSettings = new ThemeButton("Settings", Color.white, Theme.NETWORK_PANEL_STATUS_BACKGROUND);
	    buttonChannelSettings.addMouseListener(new MouseHoverColorListener(buttonChannelSettings, Color.white, Theme.COLOR_GREEN));
            panelChannelInfo.add(buttonChannelSettings, "align right");
    }
    
    private void createGUIJoinNetwork() {
        panelJoinNetwork = new JPanel(new MigLayout(
                "",			    // Constraints
                "100%",			    // Grow column to 100% width
                "550px:550px"		    // Row
	));
	
	JLabel labelNetworkAddress = new JLabel("Hostname");
	JLabel labelPort = new JLabel("Port");
	JCheckBox checkboxSSL = new JCheckBox("SSL");
	
	panelJoinNetwork.add(labelNetworkAddress, "");
	panelJoinNetwork.add(labelPort, "wrap");
	
	JTextField textFieldHostname = new JTextField();
	JTextField textfieldPort = new JTextField();
	
	panelJoinNetwork.add(textFieldHostname, "");
	panelJoinNetwork.add(textfieldPort, "");
	panelJoinNetwork.add(checkboxSSL, "wrap");
	
	panelJoinNetwork.setVisible(false);
	getMainFrame().add(panelJoinNetwork, "grow, hidemode 3");
    }
    
    /** @return main GUI JFrame. */
    public JFrame getMainFrame() { return frame; }

    /** @return chat messages area for current channel / user / network status. */
    public JEditorPane getChatArea() { return panelChatArea; }

    /** @return chat textfield where user will type messages into. */
    public JTextField getChatTextfield() { return chatTextField; }

    /** @return ScrollPanel which wraps the channel users jlist inside. */
    public JScrollPane getChannelUsersScrollPanel() { return channelUsersScrollpanel; }
    
    /** @return ScrollPanel which wraps the network channel jlist inside. */
    public JScrollPane getNetworkChannelScrollPanel() { return networkChannelScrollPanel; }
    
    /** @return JList containing channel users. */
    public JList getChannelUsers() { return listChannelUsers; }

    /** @return JList containing networks & channels. */
    public JList getNetworkChannels() { return listNetworkChannels; }
    
    /** @return JEditorPane which contains the channel name and topic. */
    public JEditorPane getEditorPaneTopic() { return editorpaneTopic; }
    
    public JLabel getChannelTitleLabel() { return channelTitle; }
    
    public JButton getButtonLeaveChannel() { return buttonChannelLeave; }
    
    public JButton getButtonReconnect() { return buttonReconnect; }
    
    public JButton getButtonChannelSettings() { return buttonChannelSettings; }
    
    public JButton getButtonAddNetwork() { return buttonAddNetwork; }
}