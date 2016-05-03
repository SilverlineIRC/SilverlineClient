package settings;

import java.io.File;
import java.util.HashMap;
import util.AbstractSettingsHandler;

/**
 * Application related settings loaded from file.
 * @author Kulttuuri
 */
public class SettingsSilverline extends AbstractSettingsHandler {

    public SettingsSilverline(File pathToSettingsFile, String missingSettingDefaultValue) {
	super(pathToSettingsFile, missingSettingDefaultValue);
    }

    @Override
    public String getFileComment() {
        return "Settings for the application";
    }
    
    @Override
    public HashMap<String, String> getDefaultSettings() {
	HashMap<String, String> settings = new HashMap<String, String>();
	
	settings.put("theme", "default");
	
	return settings;
    }
    
    @Override
    public HashMap<String, String> getSettingComments() {
	HashMap<String, String> settings = new HashMap<String, String>();
	
	settings.put("theme", "Theme for the application. Default: default");
	
	return settings;
    }
    
    public String getTheme() {
	String theme = getValueForSetting("theme");
	return theme.equals("") ? "default" : theme;
    }
    
    public void setTheme(String themeName) {
	saveSetting("theme", themeName);
    }
}