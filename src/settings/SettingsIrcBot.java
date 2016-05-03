package settings;

import java.io.File;
import java.util.HashMap;
import util.AbstractSettingsHandler;

/**
 * Bot related settings loaded from file.
 * @author Kulttuuri
 */
public class SettingsIrcBot extends AbstractSettingsHandler {

    public SettingsIrcBot(File pathToSettingsFile, String missingSettingDefaultValue) {
	super(pathToSettingsFile, missingSettingDefaultValue);
    }

    @Override
    public String getFileComment() {
        return "Bot settings";
    }
    
    @Override
    public HashMap<String, String> getDefaultSettings() {
	HashMap<String, String> settings = new HashMap<String, String>();
	
	settings.put("bot-enabled", "false");
	
	return settings;
    }
    
    @Override
    public HashMap<String, String> getSettingComments() {
	HashMap<String, String> settings = new HashMap<String, String>();
	
	settings.put("enabled", "Is bot functionality enabled? Default: false");
	
	return settings;
    }
    
    public boolean getBotEnabled() {
	return getValueForSettingAsBoolean("bot-enabled", false);
    }
}