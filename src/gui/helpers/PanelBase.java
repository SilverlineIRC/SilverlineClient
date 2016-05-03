package gui.helpers;

import gui.windows.main.GuiMainIrcView;
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;


/**
 * Base class for all application JPanels.
 * @author Kulttuuri
 */
public abstract class PanelBase extends JPanel
{
    public PanelBase(MigLayout layout) {
        setColors();
        createPanel(layout);
        addComponents();
    }
    
    public abstract void setColors();
    public abstract void createPanel(MigLayout layout);
    public abstract void addComponents();
}