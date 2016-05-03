/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.util.Map;

/**
 * Class that holds the info for the client user
 */
public class Session {

    /**
     * User information
     */
    private static String userID; //User ID
    private static String username; //Irccloud username(email)
    private static String nickname; //Default nickname to use
    private static String realname; //Default realname to use
    private static String email; //Email
    private static String sessionID; //ID for current session
    private static String lastOpenBid; //ID of the last open buffer
    private static String sessionToken; //Token for the session
    private static String password; //Password for the user

    //Called when stat_user is recieved from API
    public static void initSession(Map sessionmap) {
        userID = sessionmap.get("id").toString();
        realname = sessionmap.get("name").toString();
        email = sessionmap.get("email").toString();
        lastOpenBid = sessionmap.get("last_selected_bid").toString();
        System.out.println("SESSION SET");
    }

    /**
     * Sets username for the user
     *
     * @param newUsername New username
     */
    public static void setUsername(String newUsername) {
        username = newUsername;
    }

    /**
     * Sets nickname for the user
     *
     * @param newNickname New nickname
     */
    public static void setNickname(String newNickname) {
        nickname = newNickname;
    }

    /**
     * Sets the realname for the user
     *
     * @param newRealname New username
     */
    public static void setRealname(String newRealname) {
        realname = newRealname;
    }

    /**
     * Sets email for the user
     *
     * @param newEmail New email
     */
    public static void setEmail(String newEmail) {
        email = newEmail;
    }

    /**
     * Sets session id for the user
     *
     * @param newSessionID New sessionID
     */
    public static void setSessionID(String newSessionID) {
        sessionID = newSessionID;
    }

    /**
     * Sets last open bid for the user
     *
     * @param newlastOpenBid The last bid opened, from IRCCloud API
     */
    public static void setLastOpenBid(String newlastOpenBid) {
        lastOpenBid = newlastOpenBid;
    }

    /**
     * Sets session token for the user
     *
     * @param newSessiontoken The session token from IRCCloud API
     */
    public static void setSessionToken(String newSessiontoken) {
        sessionToken = newSessiontoken;
    }

    /**
     * Sets password for the user
     *
     * @param newPassword The new password for the user
     */
    public static void setPassword(String newPassword) {
        password = newPassword;
    }

    /**
     * @return Users username
     */
    public static String getUsername() {
        return username;
    }

    /**
     * @return Users nickname
     */
    public static String getNickname() {
        return nickname;
    }

    /**
     * @return Users realname
     */
    public static String getRealname() {
        return realname;
    }

    /**
     * @return Users email
     */
    public static String getEmail() {
        return email;
    }

    /**
     * @return Users sessionid
     */
    public static String getSessionID() {
        return sessionID;
    }

    /**
     * @return Users lastopenbid
     */
    public static String getLastOpenBid() {
        return lastOpenBid;
    }

    /**
     * @return Users session token
     */
    public static String getSessionToken() {
        return sessionToken;
    }

    /**
     * @return Users password
     */
    public static String getPassword() {
        return password;
    }
}
