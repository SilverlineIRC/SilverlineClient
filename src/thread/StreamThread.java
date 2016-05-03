package thread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import connection.Streamparser;

/**
 *
 * @author Zuppi
 */
public class StreamThread extends Thread {

    private final DefaultHttpClient httpclient;
    private String session;
    /** Class where all responses from this thread are being sent. */
    private AbstractThreadCallbackListener receiverClass;

    public StreamThread(DefaultHttpClient httpclient, Streamparser streamparser, String session) {
        this.httpclient = httpclient;
        this.receiverClass = streamparser;
        this.session = session;      
    }

    /**
     * Sends data to class that wants to receive notifications from this thread.
     * @param type Event typename.
     * @param object  Object containing data to be sent to receiver class.
     */
    private void sendDataToReceiverClass(String type, Object object) {
        if (receiverClass != null) {
            receiverClass.callbackReceivedFromThread(type, object);
        }
    }
    
    @Override
    public void run() {
        HttpPost httppost = new HttpPost("https://www.irccloud.com/chat/stream");
         StringEntity str = new StringEntity("session="+session, ContentType.APPLICATION_FORM_URLENCODED);
         httppost.setEntity(str);
        httppost.setHeader("Cookie", "session=" + session);

        try {
            HttpResponse response = httpclient.execute(httppost);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            JSONParser parser = new JSONParser();
            while ((line = rd.readLine()) != null) {
                if (!line.isEmpty()) {
                    Object obj = JSONValue.parse(line);
                    sendDataToReceiverClass("STREAMLINE", obj);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
