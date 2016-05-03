package gui.helpers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * For adding drag movement to a component. This means that user can
 * hold left click on the component and it makes target frame move around.
 * @author Kulttuuri
 */
public class ComponentDragMovement extends MouseAdapter {
    
    private int pX;
    private int pY;
    private JFrame targetFrame;
    
    public static void addDragMovementForComponent(JComponent mainComponent, JFrame targetFrame) {
	ComponentDragMovement comp = new ComponentDragMovement(targetFrame);
        mainComponent.addMouseListener(comp);
        mainComponent.addMouseMotionListener(comp);
    }
    
    private ComponentDragMovement(JFrame targetFrame) {
	this.targetFrame = targetFrame;
    }
    
    // Drag handling
    @Override
    public void mousePressed(MouseEvent me) {
	// Get x,y and store them
	pX = me.getX();
	pY = me.getY();
    }
    
    // Add MouseMotionListener for detecting drag
    @Override
    public void mouseDragged(MouseEvent me) {
	targetFrame.setLocation(targetFrame.getLocation().x + me.getX() - pX, targetFrame.getLocation().y + me.getY() - pY);
    }
}