package main;

import api.Core;
import gui.windows.loading.GUILoading;
import gui.windows.login.GUILogin;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import javax.swing.ToolTipManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import settings.SettingsIrcBot;
import settings.SettingsSilverline;

/**
 * Main entry point for the application.
 * @author Zuppi, Kulttuuri
 */
public class SilverlineMain {
    
    public static SettingsSilverline settings;
    public static SettingsIrcBot settingsBot;

    private static DefaultHttpClient httpclient;
    private static KeyStore keystore;
    //private static IrcEngine ircengine;

    public static void main(String[] args) {
        try
        {
            init();
            
            // TODO: DEBUG / REMOVE
            //if (true) { Test.showMainIRCViewWithTestData(); return; }
	    //if (true) { GUILoading.showLoadingGUI("Connecting to Irccloud..."); return; }
            GUILogin guilogin = new GUILogin();
            Core.setLoginGUI(guilogin);
            //guilogin.setEngine(ircengine);
            
            guilogin.setVisible(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Initializes the whole application. Sets keystores, initializes connection to irccloud...
     * @throws Exception 
     */
    private static void init() throws Exception
    {
	// Initialize setting handlers
	settings = new SettingsSilverline(new File("settings/application.ini"), "");
	settingsBot = new SettingsIrcBot(new File("settings/ircbot.ini"), "");
	
	// Show tooltips instantly
	ToolTipManager.sharedInstance().setInitialDelay(0);
        
        
        
            
	
        PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
        cm.setMaxTotal(5);
        httpclient = new DefaultHttpClient(cm);
        keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream instream = new FileInputStream(new File("cloudcerts"));
        try {
            keystore.load(instream, "silverline".toCharArray());
        } finally {
            try {
                instream.close();
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
        }
        SSLSocketFactory socketFactory = new SSLSocketFactory(keystore);
        Scheme sch = new Scheme("https", 443, socketFactory);
        httpclient.getConnectionManager().getSchemeRegistry().register(sch);
        Core.initCore(httpclient);
        
        //ircengine = new IrcEngine(httpclient);
        
    }
}