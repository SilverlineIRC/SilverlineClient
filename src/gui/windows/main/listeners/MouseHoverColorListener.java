package gui.windows.main.listeners;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;

/**
 * Adds hover text / background color for a component.
 * @author Kulttuuri
 */
public class MouseHoverColorListener implements MouseListener {
    
    JComponent component;
    Color textColor;
    Color backgroundColor;
    Color hoverTextColor;
    Color hoverBackgroundColor;
    
    public MouseHoverColorListener(JComponent component, Color hoverTextColor, Color hoverBackgroundColor) {
	this.component = component;
	this.textColor = component.getForeground();
	this.backgroundColor = component.getBackground();
	this.hoverTextColor = hoverTextColor;
	this.hoverBackgroundColor = hoverBackgroundColor;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
	component.setForeground(hoverTextColor);
	component.setBackground(hoverBackgroundColor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
	component.setForeground(textColor);
	component.setBackground(backgroundColor);
    }
}