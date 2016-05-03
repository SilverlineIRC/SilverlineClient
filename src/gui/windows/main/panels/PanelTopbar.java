package gui.windows.main.panels;

import gui.theme.Theme;
import gui.helpers.PanelBase;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;

/**
 * 
 * @author Kulttuuri
 */
public class PanelTopbar extends PanelBase {
    
    private Color COLOR_BACKGROUND;
    private Color COLOR_TEXT;

    public PanelTopbar(MigLayout layout) {
        super(layout);
    }
    
    @Override
    public void setColors() {
        COLOR_BACKGROUND = Theme.TITLEBAR_BACKGROUND;
        COLOR_TEXT = Theme.TITLEBAR_TEXT;
    }
    
    @Override
    public void createPanel(MigLayout layout) {
        setLayout(layout);
        setBorder(BorderFactory.createEmptyBorder());
        
        setBackground(COLOR_BACKGROUND);
    }

    @Override
    public void addComponents() {
        JLabel labelAppTitle = new JLabel("<html>&nbsp;Silverline IRCCloud Client</html>");
        labelAppTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images/irccloud_logo.png")));
        labelAppTitle.setForeground(COLOR_TEXT);
        add(labelAppTitle, "grow");

        JButton buttonCloseApplication = new JButton(new javax.swing.ImageIcon(getClass().getResource("/gui/icons/cross.png")));
        buttonCloseApplication.setBackground(COLOR_BACKGROUND);
        buttonCloseApplication.setOpaque(true);
        buttonCloseApplication.setBorderPainted(false);

        buttonCloseApplication.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(1);
            }
        });
	
        add(buttonCloseApplication, "gapleft push");
    }
}