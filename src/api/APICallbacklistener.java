/*
 * Used to listen callbacks from certain API-calls
 * 
 */
package api;

;
import connection.Session;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import thread.AbstractThreadCallbackListener;


public class APICallbacklistener implements AbstractThreadCallbackListener {

    @Override
    public void callbackReceivedFromThread(String type, Object object) {
        if (object instanceof JSONObject) {
            Map errormap = (Map) object;
            if (errormap.get("success").equals(false)) {
                callbackPrint("CONNECTION TO IRCCLOUD FAILED. REASON: " + errormap.get("message"));
                System.exit(0);
            }
        }
        if (type.equals(APIConstants.RECEIVER_CLASS_TYPE_LOGIN_RESPONSE)) {
            Map map = (Map) object;

            if (map.containsKey("success")) {
                boolean success = (Boolean) map.get("success");

                if (success == true) {
                    System.out.println("LOGIN SUCCESS!");
                    Session.setSessionID((String) map.get("session"));
                    Core.loginSuccesful();
                }
                else {
                    System.out.println("Login failed. Please check your username and password.");
                }
            }
        }

        else if (type.equals(APIConstants.RECEIVER_CLASS_TYPE_TOKEN_RESPONSE)) {
            Map map = (Map) object;
            if (map.containsKey("success")) {
                Session.setSessionToken((String) map.get("token"));
                Connectionmanager.sendLogin(Session.getUsername(), Session.getPassword());
            }

        }
        else if (type.equals(APIConstants.RECEIVER_CLASS_TYPE_OOB_RESPONSE)) {

            List ooblist = (List) object;
            Core.streamparser.parseOOBResponse(ooblist);
        }
        else if (type.equals(APIConstants.RECEIVER_CLASS_TYPE_BACKLOG_RESPONSE)) {
            List backloglist = (List) object;
            Core.streamparser.parseBacklogResponse(backloglist);
        }
        else {
            callbackPrint("LINE RECIEVED: " + type);
            callbackPrint(object.toString());
        }
    }

    /**
     * All printlines done in this class are done through this to tag them for easier debugging.
     * @param message Message to be printed
     */
    private void callbackPrint(String message) {
        System.out.println("[APICALLBACK] " + message);
    }
}
