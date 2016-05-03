package gui.windows.main.datamodels;

import irc.IrcUser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 * Datamodel for channel userlist in GUI.
 *
 * @author kulttuuri
 */
public class UserListDataModel extends AbstractListModel {

    /**
     * Inner class for helping to sort the users by nicknames.
     */
    private class compareUserAlphabets implements Comparator<IrcUser> {

        @Override
        public int compare(IrcUser o1, IrcUser o2) {
            return (o1.nick).compareToIgnoreCase(o2.nick);
        }

        private int fixString(String in) {
            return Integer.parseInt(in.substring(0, in.indexOf('_')));
        }
    }
    private List<IrcUser> usersInView = new ArrayList<IrcUser>();
    private compareUserAlphabets compareHelper = new compareUserAlphabets();

    private void sortData() {
        
        List<IrcUser> ops = new ArrayList<IrcUser>();
        List<IrcUser> voiced = new ArrayList<IrcUser>();
        List<IrcUser> regular = new ArrayList<IrcUser>();

        for (IrcUser user : usersInView) {
            if (user.isOpped()) {
                ops.add(user);
            } 
            else if (user.isVoiced()) {
                voiced.add(user);
            }
            else if (user.isUserListSeparator()){
                //Left blank on purpose, catches separators so they dont get sorted
            }
            else {
                regular.add(user);
            }
        }

        Collections.sort(ops, compareHelper);
        Collections.sort(voiced, compareHelper);
        Collections.sort(regular, compareHelper);

        if (ops.size() > 0) {
            ops.add(0, new IrcUser("Ops (" + ops.size() + ")", "Ops", "", "", false, true));
        }
        if (voiced.size() > 0) {
            voiced.add(0, new IrcUser("Voiced (" + voiced.size() + ")", "Voiced", "", "", false, true));
        }
        if (regular.size() > 0) {
            regular.add(0, new IrcUser("Members (" + regular.size() + ")", "Members", "", "", false, true));
        }

        // Add all users back to view
        usersInView.clear();
        usersInView.addAll(ops);
        usersInView.addAll(voiced);
        usersInView.addAll(regular);
    }

    public IrcUser getUserInViewIndex(int index) {
        return usersInView.get(index);
    }

    public void addUserToView(IrcUser user) {
        int index = usersInView.size();
        this.usersInView.add(user);
        sortData();
        fireIntervalAdded(this, index, index + 1);
    }

    public void addUsersToView(List<IrcUser> users) {
        int startIndex = usersInView.size();
        this.usersInView.addAll(users);
        sortData();
        fireIntervalAdded(this, startIndex, startIndex + users.size());
    }

    // TODO: test.
    public void removeAllUsersFromView() {
        int oldChanSize = usersInView.size();
        this.usersInView.clear();
        fireIntervalRemoved(this, 0, oldChanSize);
    }

    public void removeUserFromView(IrcUser user) {
        // Find channel's index
        int index = 0;
        for (int i = 0; i < usersInView.size(); i++) {
            if (usersInView.get(i) == user) {
                index = i;
                break;
            }
        }

        this.usersInView.remove(user);
        fireIntervalRemoved(this, index, index);
    }

    public void changeUserNick(String oldnick, String newnick) {
        int index = 0;
        for (IrcUser u : usersInView){
            if (u.nick.equals(oldnick)){
                u.changeNick(newnick);
                break;
            }
        }
        sortData();
        for (int i = 0; i < usersInView.size(); i++) {
            if (usersInView.get(i).nick.equals(newnick)) {
                index = i;
                break;
            }
        }     
        fireContentsChanged(this, index, index);
    }
    
    public void changeUserMode(String nick, String newmode){
        int index = 0;
        for (IrcUser u : usersInView){
            if (u.nick.equals(nick)){
                u.setModes(newmode);
                break;
            }
        }
        sortData();
        for (int i = 0; i < usersInView.size(); i++) {
            if (usersInView.get(i).nick.equals(nick)) {
                index = i;
                break;
            }
        }     
        fireContentsChanged(this, index, index);
        
    }

    @Override
    public int getSize() {
        return this.usersInView.size();
    }

    @Override
    public Object getElementAt(int index) {
        return this.usersInView.get(index);
    }
}