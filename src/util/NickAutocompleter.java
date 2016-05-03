
package util;

import irc.IrcUser;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles autocompletion of nicknames. First keypress searches the channel members nicknames for the given string, following keypresses scrolls trhough the results
 * @author Zuppi
 */
public class NickAutocompleter {
    private static List<String> nickList= new ArrayList<String>();
    private static int listIndex = 0;
    private static boolean autoCompleteActive = false;
    
    /**
     * Gets list of nicks corresponding to the search string
     * @param memberList List of members from the IrcChannel
     * @param searchString String to search from nicknames
     */
    public static void getAutocompletes(List<IrcUser> memberList, String searchString){       
        listIndex = 0;
        searchString = searchString.toLowerCase();
        for (IrcUser u : memberList){    
            if (u.nick.toLowerCase().startsWith(searchString)){
                nickList.add(u.nick);
            }
        }
        
        if (!nickList.isEmpty()){
            autoCompleteActive = true;
        }
            
    }
    
    /**
     * Gets next autocompletion from list
     * @return Next nickname from the list corresponding to the search string
     */
    public static String getAutocomplete(){
        if (listIndex == nickList.size()){
            listIndex = 0;
        }
        String autoCompletenick = nickList.get(listIndex);
        listIndex++;
        return autoCompletenick;
    }
    
    /**
     * Deactivates nick completion
     */
    public static void deactivateAutocomplete(){
        autoCompleteActive = false;
        listIndex = 0;
        nickList.clear();
    }
    
    /**
     * Is autocompletion currently active?
     * @return Boolean indicating autocompletion status
     */
    public static boolean isAutocompleteActive(){
        return autoCompleteActive;
    }
}
