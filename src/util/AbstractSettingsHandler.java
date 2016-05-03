package util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for easily handling settings from files.
 * @author Kulttuuri
 */
public abstract class AbstractSettingsHandler
{
    /** Map containing all settings and their values in memory. */
    private HashMap<String, String> settings = new HashMap<String, String>();
    /** Map containing all setting comments in memory. */
    private HashMap<String, String> settingComments = new HashMap<String, String>();
    /** Path to settings file. Passed in {@link #AbstractSettingsHandler(File, String) constructor}. */
    public File pathToSettingsFile;
    /** Default value for setting that is missing. Passed in {@link #AbstractSettingsHandler(File, String) constructor}. */
    private String missingSettingDefaultValue;
    /** Set to true after settings have been {@link #loadSettings() loaded}. */
    private boolean settingsLoaded = false;
    /** Set to true after comments have been {@link #formatComments() formatted}. */
    private boolean commentsFormatted = false;
    
    /**
     * Constructor to initialize the settings handler.
     * @param pathToSettingsFile File object containing path to the settings file.
     * @param missingSettingDefaultValue Default value for missing settings.
     */
    public AbstractSettingsHandler(File pathToSettingsFile, String missingSettingDefaultValue)
    {
        this.pathToSettingsFile = pathToSettingsFile;
        this.missingSettingDefaultValue = missingSettingDefaultValue;
        init();
        
        storeComments();
        formatComments();
        
        // Load settings into memory
        loadSettings();
        // Write them back to file, to append any missing settings
        saveSettingsToFile();
    }
    
    /**
     * Stores the comments from {@link #getSettingComments() } to {@link #settingComments}.
     */
    private void storeComments()
    {
        settingComments = getSettingComments();
    }
    
    /**
     * Formats comments got by method {@link #getSettingComments() }.
     */
    private void formatComments()
    {
        if (commentsFormatted)
        {
            System.out.println("Tried to reformat comments. Skipping.");
            return;
        }
        
        HashMap<String, String> formattedComments = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : settingComments.entrySet())
        {
            // We also replace = with : so that our comments will not be mistaken for settings.
            formattedComments.put(entry.getKey(), getFormattedComment(entry.getValue()).replace("=", ":"));
        }
        
        settingComments = formattedComments;
        commentsFormatted = true;
    }
    
    /**
     * Returns missing setting default value.
     * @return missing setting default value.
     * @see {@link #missingSettingDefaultValue}
     */
    public String getMissingSettingDefaultValue()
    {
    	return missingSettingDefaultValue;
    }
    
    /**
     * Loads settings from {@link #pathToSettingsFile settings file} into {@link #settings Map containing settings}.
     */
    public void loadSettings()
    {
        // Create file if it does not exist
        if (!pathToSettingsFile.exists())
        {
            System.out.println("Creating missing settings file: " + pathToSettingsFile);
            Datahandler.createFile(pathToSettingsFile.getAbsolutePath());
        }
        
        HashMap<String, String> missingSettings = getMissingSettings();

        // Print out all missing settings and add them into the list of settings
        if (missingSettings.size() > 0)
        {
            for (java.util.Map.Entry<String, String> entry : missingSettings.entrySet())
            {
                System.out.println("Appended setting: " + entry.getKey() + "=" + entry.getValue() + " to file: " + pathToSettingsFile.getAbsolutePath());
            }
        }
        
        // Load data into map
        if (getCommentStartFormat() != null)
            settings = (HashMap<String, String>)Datahandler.loadDataInMapFromFile(pathToSettingsFile.getAbsolutePath(), "=", "UTF-8", "#");
        else
            settings = (HashMap<String, String>)Datahandler.loadDataInMapFromFile(pathToSettingsFile.getAbsolutePath(), "=", "UTF-8");
        
        settings.putAll(missingSettings);
        
        /*
        System.out.println("LOADED SETTINGS START!!!");
        Iterator it = settings.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pairs = (Map.Entry) it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
        }
        System.out.println("LOADED SETTINGS END!!!");*/
        
        settingsLoaded = true;
    }

    /**
     * Gets a list of missing settings.
     * @return HashMap containing setting=value that were missing from the file, if any.
     */
    private HashMap<String, String> getMissingSettings()
    {
        HashMap<String, String> missingSettings = new HashMap<String, String>();
        HashMap<String, String> defSettings = getDefaultSettings();
        
        for (int j = 0; j < defSettings.size(); j++)
        {
            for (String key : defSettings.keySet())
            {
                try
                {
                    if (Datahandler.iniGet(pathToSettingsFile.getAbsolutePath(), key) == null)
                    {
                        missingSettings.put(key, defSettings.get(key));
                        //Datahandler.iniSet(pathToSettingsFile.getAbsolutePath(), key, defSettings.get(key));
                    }
                }
                catch (Exception e)
                {
                    printError("Problem getting default setting: " + e.getMessage(), false);
                }
            }
        }
        return missingSettings;
    }

    /**
     * After settings have been {@link #loadSettings() loaded}, this returns true.
     * Otherwise if settings are not loaded, returns false.
     * @return boolean.
     */
    public boolean hasSettingsBeenLoaded()
    {
        return settingsLoaded;
    }

    /**
     * Saves setting to file and loads settings file again.
     * @param setting Setting to be saved.
     * @param value Value for setting to be saved.
     * @return true if there were no problems saving the value, otherwise false.
     */
    protected boolean saveSetting(String setting, String value)
    {
        if (!hasSettingsBeenLoaded())
        {
            printError("Load settings before trying to save! Did try to save: " + setting + "=" + value, true);
            return false;
        }

        if (!settings.containsKey(setting))
        {
            printError("Trying to set setting which does not exist: " + setting + " with value: " + value, false);
            return false;
        }
        this.settings.put(setting, value);
        saveSettingsToFile();
        loadSettings();
        return true;
    }
    
    /**
     * Saves settings from the {@link #settings Map containing settings} to {@link #pathToSettingsFile settings file}.
     */
    private void saveSettingsToFile()
    {
        /*Iterator it = settingComments.entrySet().iterator();
        System.out.println("COMMENTS!!!");
        while (it.hasNext())
        {
            Map.Entry pairs = (Map.Entry) it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
        }
        System.out.println("COMMENTS END!!!!");*/
        if (settingComments.size() > 0)
        {
            Datahandler.writeMapToFileWithComments(pathToSettingsFile.getAbsolutePath(), settings, "=", settingComments, getFileComment());
        }
        else
        {
            Datahandler.writeMapToFile(pathToSettingsFile.getAbsolutePath(), settings, "=");
        }
    }
    
    /**
     * Returns File object containing path to settings file.
     * @return File.
     */
    protected File getSettingsFile()
    {
        return pathToSettingsFile;
    }
    
    /**
     * Returns value for a setting.<br>
     * Checks also if settings have been loaded and that setting is not missing.
     * @param setting Setting which you want to get value for.
     * @return value for setting as String. If setting was missing or settings are not yet loaded,
     * will return {@link #missingSettingDefaultValue default value for missing setting}.
     */
    protected String getValueForSetting(String setting)
    {
        if (!hasSettingsBeenLoaded())
        {
            printError("Load settings before trying to get value! Did try to get: " + setting, true);
            return missingSettingDefaultValue;
        }

        if (!settings.containsKey(setting))
        {
            printError("Setting was missing: " + setting, false);
            return missingSettingDefaultValue;
        }
        if (settings.get(setting) == null)
            return "";
        else
            return settings.get(setting);
    }

    /**
     * Returns value for a setting as boolean.<br>
     * Checks also if settings have been loaded and that setting is not missing.
     * @param setting Setting which you want to get value for.
     * @param defaultValue Default value for missing or incorrect settings.
     * @return value for setting. If setting was missing or settings are not yet loaded,
     * will return the given default value.
     */
    protected boolean getValueForSettingAsBoolean(String setting, boolean defaultValue)
    {
        if (!hasSettingsBeenLoaded())
        {
            printError("Load settings before trying to get value! Did try to get: " + setting, true);
            return defaultValue;
        }

        if (!settings.containsKey(setting))
        {
            printError("Setting was missing: " + setting, false);
            return defaultValue;
        }
        if (settings.get(setting) == null)
            return defaultValue;
        else
        {
            try
            {
                boolean returnSetting = Boolean.parseBoolean(settings.get(setting));
                return returnSetting;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                //System.out.println("Setting '" + setting + "' was not boolean. Returning false.");
                return defaultValue;
            }
        }
    }
    
     /**
     * Returns value for a setting as int.<br>
     * Checks also if settings have been loaded and that setting is not missing.
     * @param setting Setting which you want to get value for.
     * @return value for setting as int. If setting was missing or settings are not yet loaded,
     * will return the given default value.
     */
    protected int getValueForSettingAsInt(String setting, int defaultValue)
    {
        if (!hasSettingsBeenLoaded())
        {
            printError("Load settings before trying to get value! Did try to get: " + setting, true);
            return defaultValue;
        }

        if (!settings.containsKey(setting))
        {
            printError("Setting was missing: " + setting, false);
            return defaultValue;
        }
        if (settings.get(setting) == null)
            return defaultValue;
        else
        {
            try
            {
                int returnSetting = Integer.parseInt(settings.get(setting));
                return returnSetting;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                //System.out.println("Setting '" + setting + "' was not boolean. Returning false.");
                return defaultValue;
            }
        }
    }
    
    /**
     * Helper method to print errors.
     * @param error Error message.
     * @param printStackTrace Print also StackTrace?
     */
    private void printError(String error, boolean printStackTrace)
    {
        System.out.println("Error in handling settings for file: " + pathToSettingsFile.getAbsolutePath() + ". Error description: " + error);
        if (printStackTrace)
            Debug.printStackTrace();
    }
    
    /**
     * Override this in your class to set how your comment will be outputted in the settings file. For ex like: # commentText, // commentText and so forth.
     * @param comment Comment text.
     * @return Comment that will be written in settings file on top of the setting. Without overriding this is in format: "# comment" without hyphens.
     */
    public String getFormattedComment(String comment)
    {
        return "# " + comment;
    }
    
    /**
     * Override this in your class to set how comments start in your configuration file to not load them.
     * @return How comments start in your configuration file. For ex. with # or //.
     */
    public String getCommentStartFormat()
    {
        return null;
    }
    
    /**
     * Override this in your class to set a global comment for your file to determine what settings it contains, what modifies it etc.
     * This comment is automatically added on top of your file.
     * @return comment.
     */
    public String getFileComment()
    {
        return null;
    }
    
    /**
     * Gets comments for settings. Add them in format setting=comment. Remember to also override <b>{@link #getFormattedComment(java.lang.String) getFormattedComment}</b> to
     * set how your comment will be outputted in the settings file and <b>{@link #getCommentStartFormat() getCommentStartFormat}</b>.
     * @return 
     */
    public HashMap<String, String> getSettingComments()
    {
        return new HashMap<String, String>();
    }
    
    /**
     * Contains the default setting values.
     * Every default setting will be appended into file if they do not exist
     * when {@link #loadSettings() loading map settings from file.}
     * @return HashMap<String, String> containing setting and value for it.
     */
    public abstract HashMap<String, String> getDefaultSettings();
    
    /**
     * Can be overridden in your settings implementation to do something before any settings are being loaded into memory. Called in constructor.
     */
    public void init()
    {
    }
}