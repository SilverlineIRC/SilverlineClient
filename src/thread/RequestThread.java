package thread;

import api.APIConstants;
import connection.Session;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONValue;

/**
 *
 * @author Zuppi
 */
public class RequestThread extends Thread {

    private final HttpClient httpclient;
    // TODO: Not used anywhere?
    private String address;
    private String requestType;
    private HttpPost httppost;
    private HttpGet httpget;
    private String action;
    /**
     * Class where all responses from this thread are being sent.
     */
    private AbstractThreadCallbackListener receiverClass;

    public RequestThread(AbstractThreadCallbackListener receiverClass, HttpClient httpclient, String requestType, String action, String address) {
        this.httpclient = httpclient;
        this.address = address;
        this.requestType = requestType;
        this.action = action;
        this.receiverClass = receiverClass;

        if (requestType.equals("POST")) {
            httppost = new HttpPost(address);
            if (Session.getSessionID() != null){
             httppost.setHeader("Cookie", "session=" + Session.getSessionID());
             }
        } else {
            httpget = new HttpGet(address);
            httpget.setHeader("Cookie", "session=" + Session.getSessionID());
        }
    }

    /**
     * Sends data to class that wants to receive notifications from this thread.
     *
     * @param type Event typename.
     * @param object Object containing data to be sent to receiver class.
     */
    private void sendDataToReceiverClass(String type, Object object) {
        if (receiverClass != null) {
            receiverClass.callbackReceivedFromThread(type, object);
        }
    }

    public void addStringEntity(String entityContent) {
        StringEntity str = new StringEntity(entityContent, ContentType.APPLICATION_FORM_URLENCODED);   
        httppost.setEntity(str);
    }

    public void addHeader(String headername, String param) {
        if (requestType.equals("POST")) {
            httppost.addHeader(headername, param);
        } else {
            httpget.addHeader(headername, param);
        }
    }

    @Override
    public void run() {
        try {
            HttpResponse response = null;
            if (requestType.equals("POST")) {
                
                System.out.println("[REQUESTTHREAD]executing request " + httppost.getRequestLine());
                response = httpclient.execute(httppost);
            } else {
                System.out.println("[REQUESTTHREAD]executing request " + httpget.getRequestLine());
                response = httpclient.execute(httpget);
            }
            HttpEntity entity = null;
            if (action.equals(APIConstants.RECEIVER_CLASS_TYPE_OOB_RESPONSE)) {
                entity = new GzipDecompressingEntity(response.getEntity());
            } else {
                entity = response.getEntity();
            }
            sendDataToReceiverClass(action, parseResponseEntity(entity));
            EntityUtils.consume(entity);
            if (requestType.equals("POST")){
                httppost.releaseConnection();
            }
            else{
                httpget.releaseConnection();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Parses the response that you get from irccloud.
     *
     * @param responseEntity
     * @return Map containing JSON response data.
     * @throws IOException on IO exception.
     */
    private Object parseResponseEntity(HttpEntity responseEntity) throws IOException {
        String entityString = IOUtils.toString(responseEntity.getContent(), "UTF-8");
        System.out.println("[REQUESTTHREAD]PARSED RESPONSE:" + entityString);
        
        Object obj = JSONValue.parse(entityString);
        return obj;
    }
}
