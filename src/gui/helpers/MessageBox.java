package gui.helpers;

import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 * Class for throwing Swing popup messages.
 * @author kulttuuri
 */
public abstract class MessageBox
{
    /**
     * To generate notification type popup message.
     * @param title Popup title.
     * @param message Popup content.
     */
    public static void notice(String title, String message)
    {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * To ask for user input in a popup message.
     * @param title Title of the popup.
     * @param description popup description.
     * @return returns the text that user inputted in the textfield.
     */
    public static String input(String title, String description)
    {
        return JOptionPane.showInputDialog(null, description, title, 1);
    }

    /**
     * To show up a password textfield popup for the user.
     * @param title panel title.
     * @param description panel description.
     * @param showCancelButton Show or don't show cancel button.
     * @return password that user typed in the textfield.
     */
    public static String inputPassword(String title, String description, boolean showCancelButton)
    {
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.PAGE_AXIS));
        JPasswordField password = new JPasswordField(15);
        jpanel.add(new JLabel(description));
        jpanel.add(password);
        int action;
        if (showCancelButton)
        {
            action = JOptionPane.showConfirmDialog(null, jpanel, title, JOptionPane.DEFAULT_OPTION);
        }
        else
        {
            action = JOptionPane.showConfirmDialog(null, jpanel, title, JOptionPane.OK_CANCEL_OPTION);
        }
        return new String(password.getPassword());
    }

    /**
     * To confirm something from the user.
     * Message title & content will go through localization.
     * @param title Confirm dialog title.
     * @param content Confirm dialog content.
     * @return standard JOptionPane static int states, like OK_OPTION, CANCEL_OPTION...
     */
    public static int confirmDialog(String title, String content)
    {
        Object[] options =
        {
            "Yes", "No"
        };
        int result = 100;

        result = JOptionPane.showOptionDialog(null,
                "<html>"+content+"</html>",
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]);
        return result;
    }
}