package gui.components;

import gui.theme.Colors;
import gui.theme.Theme;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

/**
 * Themed buttons for the application.
 * @author Kulttuuri
 */
public class ThemeButton extends JButton {
    
    /**
     * Creates new themed button.
     * @param text Text for the button. Set to "" for empty.
     * @param textColor Text color. If null, will be set to black.
     * @param backgroundColor Background color for the button. If null, will be set to application background color.
     */
    public ThemeButton(String text, Color textColor, Color backgroundColor) {
	super(text);
	
	if (backgroundColor == null) backgroundColor = Theme.APPLICATION_DEFAULT_BACKGROUND;
	if (textColor == null) textColor = Theme.APPLICATION_DEFAULT_TEXT;
	setSkin(textColor, backgroundColor);
    }
    
    private void setSkin(Color textColor, Color backgroundColor) {
	this.setForeground(textColor);
	this.setBackground(backgroundColor);
	this.setOpaque(true);
	this.setBorderPainted(false);
    }
    
    public void setFont(ThemeFont font) {
	setFont(new Font(font.toString(), Font.PLAIN, Theme.DEFAULT_FONT_SIZE));
    }
}