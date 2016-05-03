/*
 * Contains constants for API endpoints and response types for callbacks
 * 
 */
package api;

public class APIConstants {

    /**
     * HTTPS login url that will be used to login into irccloud api.
     */
    public static final String IRCCLOUD_URL = "https://www.irccloud.com";
    public static final String LOGIN_URL = "https://www.irccloud.com/chat/login";
    public static final String SAY_URL = "https://www.irccloud.com/chat/say";
    public static final String TOKEN_URL = "https://www.irccloud.com/chat/auth-formtoken";
    public static final String HEARTBEAT_URL = IRCCLOUD_URL + "/chat/heartbeat";
    public static final String NICKCHANGE_URL = IRCCLOUD_URL + "/chat/nick";
    public static final String JOINCHANNEL_URL = IRCCLOUD_URL + "/chat/join";
    public static final String PARTCHANNEL_URL = IRCCLOUD_URL + "/chat/part";
    public static final String NETWORKCONNECT_URL = IRCCLOUD_URL + "/chat/add-server";
    public static final String NETWORKRECONNECT_URL = IRCCLOUD_URL + "/chat/reconnect";
    public static final String BUFFERDELETE_URL = IRCCLOUD_URL + "/chat/delete-buffer";
    public static final String WHOIS_URL = IRCCLOUD_URL + "/chat/whois";
    public static final String ARCHIVE_URL = IRCCLOUD_URL + "/chat/archive-buffer";
    public static final String UNARCHIVE_URL = IRCCLOUD_URL + "/chat/unarchive-buffer";
    public static final String BACKLOG_URL = IRCCLOUD_URL + "/chat/backlog";

    /**
     * Enum for receiver event where irccloud server sent us response to login data.
     */
    public static final String RECEIVER_CLASS_TYPE_TOKEN_RESPONSE = "TOKEN_RESPONSE";
    public static final String RECEIVER_CLASS_TYPE_LOGIN_RESPONSE = "LOGIN_RESPONSE";
    public static final String RECEIVER_CLASS_TYPE_OOB_RESPONSE = "OOB_RESPONSE";
    public static final String RECEIVER_CLASS_TYPE_HEARTBEAT_RESPONSE = "HEARTBEAT_RESPONSE";
    public static final String RECEIVER_CLASS_TYPE_SAY_RESPONSE = "SAY_RESPONSE";
    public static final String RECEIVER_CLASS_TYPE_NICKCHANGE_RESPONSE = "NICKCHANGE_RESPONSE";
    public static final String RECEIVER_CLASS_TYPE_JOINCHANNEL_RESPONSE = "JOINCHANNEL_RESPONSE";
    public static final String RECEIVER_CLASS_TYPE_PARTCHANNEL_RESPONSE = "PARTCHANNEL_RESPONSE";
    public static final String RECEIVER_CLASS_TYPE_NETWORKCONNECT_RESPONSE = "NETWORKCONNECT_RESPONSE";
    public static final String RECEIVER_CLASS_TYPE_BUFFERDELETE_RESPONSE = "BUFFERDELETE_RESPONSE";
    public static final String RECEIVER_CLASS_TYPE_NETWORKRECONNECT_RESPONSE = "NETWORKRECONNECT_RESPONSE";
    public static final String RECEIVER_CLASS_TYPE_WHOIS_RESPONSE = "WHOIS_RESPONSE";
    public static final String RECEIVER_CLASS_TYPE_ARCHIVE_RESPONSE = "ARCHIVE_RESPONSE";
    public static final String RECEIVER_CLASS_TYPE_UNARCHIVE_RESPONSE = "UNARCHIVE_RESPONSE";
    public static final String RECEIVER_CLASS_TYPE_BACKLOG_RESPONSE = "BACKLOG_RESPONSE";
}
