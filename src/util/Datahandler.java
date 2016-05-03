package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.FileUtils;

/**
 * Useful data handling functions.
 * @author Kulttuuri
 */
public class Datahandler {

    /**
     * Gets setting from ini/property file. Properties will be read with case sensitivity, so remember that.
     * @param pathToFile Path to file.
     * @param setting Setting key to get.
     * @return Returns the key if found. If key was not found, returns null.
     */
    public static String iniGet(String pathToFile, String setting)
    {
        Properties ini = new Properties();

        // Try to read settings
        try
        {
            FileInputStream fis = new FileInputStream(pathToFile);
            ini.load(fis);
            fis.close();
            // Return setting value if it was found
            return ini.getProperty(setting);
        }
        catch (IOException ioerror)
        {
            System.out.println(ioerror);
        }

        // If value was not found, return ""
        return "";
    }

    /**
     * Sets setting to ini/property file. Uses "=" as separator. Creates the file if it does not exist.
     * @param pathToFile Path to file.
     * @param setting Setting key.
     * @param settingValue Setting value.
     */
    public static void iniSet(String pathToFile, String setting, String settingValue)
    {
        iniSet(pathToFile, setting, settingValue, "=");
        /*
         Properties ini = new Properties();
         setting = setting.toLowerCase();
         //settingValue = settingValue.toLowerCase();

         // Try to read settings and put new value there
         try
         {
         FileInputStream fis = new FileInputStream(file);
         ini.load(fis);
         fis.close();
         ini.put(setting, settingValue);
         }
         catch (IOException ioerror)
         {
         }

         // Try to write settings back into the file
         try
         {
         FileOutputStream foutput = new FileOutputStream(file);
         ini.store(foutput, null); foutput.close();
         }
         catch (IOException ioerror)
         {
         System.out.println(ioerror);
         }
         */
    }

    /**
     * Sets setting to ini/property file. Creates the file if it does not exist.
     * @param pathToFile Path to file.
     * @param setting Setting key.
     * @param settingValue Setting value.
     * @param separator Separator character between key and value.
     */
    public static void iniSet(String pathToFile, String setting, String settingValue, String separator)
    {
        // If file does not exist, create it
        createFile(pathToFile);
        
        // Escape the value
        settingValue = org.apache.commons.lang3.StringEscapeUtils.escapeJava(settingValue);
        
        // Read lines from file to list
        List<String> data = readLinesFromFileToList(pathToFile);
        if (data != null)
        {
            String line = "";
            // Go thru the list
            for (int i = 0; i < data.size(); i++)
            {
                line = data.get(i);
                // Skip empty lines
                if (line == null || line.equals("") || line.startsWith("#"))
                    continue;
                // If line starts with the setting that we are looking for, replace setting value in list, write list back to file and return
                if (line.startsWith(setting + separator))
                {
                    data.set(i, setting + separator + settingValue);
                    writeLinesFromListToFile(pathToFile, data);
                    return;
                }
            }
        }
        // if list was empty or setting not found, add setting to list and write list back to file
        //appendToFile(pathToFile, setting + "=" + settingValue);
        data.add(setting + "=" + settingValue);
        writeLinesFromListToFile(pathToFile, data);
    }
    
    /**
     * Write list to file.
     * @param fileName Name of the file. List will be written here. Will overwrite old file content.
     * @param list List to write into file line by line.
     */
    public static void writeLinesFromListToFile(String fileName, List list)
    {
        try
        {
            File file = new File(fileName);
            FileUtils.writeLines(file, list);
        }
        catch (IOException e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    
    /**
     * Reads file to list and returns the list. If file is not found, creates the file.
     * @param fileName File to make as an list.
     * @return Returns the list made out of the given file.
     */
    public static List readLinesFromFileToList(String fileName)
    {
        List list = new ArrayList<String>();
        try
        {
            File file = new File(fileName); createFile(fileName);
            list = FileUtils.readLines(file, "UTF-8");
        }
        catch (IOException e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Loads data to map from file separated by the separator.
     * @param fileName Path to file where you want to read the data.
     * @param separator Separator character. For ex: "="
     * @return Map containing the data.
     */
    public static Map<String, String> loadDataInMapFromFile(String fileName, String separator)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        // Try to load data into map
        try
        {
            // Open file
            FileInputStream in = new FileInputStream(fileName);

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String nextLine = "";
            String[] splitData = new String[2];

            // Go thru file
            while (nextLine != null)
            {
                nextLine = br.readLine();
                // Insert into map only lines that are not null and are format setting=value
                if (nextLine != null)
                {
                    try
                    {
                        splitData = nextLine.split(separator);
                        if (splitData.length < 2) 
                            map.put(splitData[0], "");
                        else
                            map.put(splitData[0], splitData[1]);
                    }
                    catch (Exception e) { }
                }
            }

            // Close streams
            in.close();
            br.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // Return the map
        return map;
    }
    
    /**
     * Loads data to map from file separated by the separator.
     * @param fileName Path to file where you want to read the data.
     * @param separator Separator character. For ex: "="
     * @return Map containing the data.
     */
    public static Map<String, String> loadDataInMapFromFile(String fileName, String separator, String characterEncoding)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        // Try to load data into map
        try
        {
            // Open file
            FileInputStream in = new FileInputStream(fileName);

            BufferedReader br = new BufferedReader(new InputStreamReader(in, characterEncoding));
            String nextLine = "";
            String[] splitData = new String[2];

            // Go thru file
            while (nextLine != null)
            {
                nextLine = br.readLine();
                // Insert into map only lines that are not null and are format setting=value
                if (nextLine != null)
                {
                    try
                    {
                        splitData = nextLine.split(separator);
                        if (splitData.length < 2) 
                            map.put(splitData[0], "");
                        else
                            map.put(splitData[0], splitData[1]);
                    }
                    catch (Exception e) { }
                }
            }

            // Close streams
            in.close();
            br.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // Return the map
        return map;
    }
    
    /**
     * Writes map back to file.<br>
     * Map will be written line by line to file.<br>
     * Format: key<separator>value
     * @param filename Path to the file where you want to write the map. If does not exist, will be created.
     * @param data Map containing the data that you want write into the file.
     * @param separator Separator character how you want to separate the data written into the file. for ex: "="
     * @param comments Comments for settings. In format setting=comment. Values should already be formatted to be in comment format. These will be added on top of every setting which has comment.
     * @param formattedFileTitleContent Comment that would be added on top of the file to describe what it does. Can be null or empty ("") to determine what no top comment will be added.
     * This title comment should already be formatted to be in comment format.
     */
    public static void writeMapToFileWithComments(String filename, HashMap<String, String> data, String separator, HashMap<String, String> formattedComments, String formattedFileTitleContent)
    {
        try
        {
            // Create / Clear file
            File f = new File(filename); f.createNewFile();

            // Initialize writer objects
            FileWriter fw = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fw);
            String newline = System.getProperty("line.separator");

            // File title comment
            if (formattedFileTitleContent != null && !formattedFileTitleContent.isEmpty())
            {
                out.write(formattedFileTitleContent + newline + newline);
            }
            
            // Save map data into the file line by line
            for (String key : data.keySet())
            {
                //System.out.println("SAVING: " + key);
                if (key == null || key.isEmpty()) continue; // Skip empty settings
                
                if (formattedComments.containsKey(key))
                {
                    //System.out.println("SAVINGCOMMENT: " + formattedComments.get(key));
                    out.write(formattedComments.get(key) + newline);
                }
                out.write(key + separator + data.get(key) + newline + newline);
                //System.out.println("data: " + key + separator + data.get(key) + newline);
            }
            // Close buffers
            out.close();
            fw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Writes map back to file.<br>
     * Map will be written line by line to file.<br>
     * Format: key<separator>value
     * @param filename Path to the file where you want to write the map. If does not exist, will be created.
     * @param data Map containing the data that you want write into the file.
     * @param separator Separator character how you want to separate the data written into the file. for ex: "="
     */
    public static void writeMapToFile(String filename, HashMap<String, String> data, String separator)
    {
        try
        {
            // Create / Clear file
            File f = new File(filename); f.createNewFile();

            // Initialize writer objects
            FileWriter fw = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fw);
            String newline = System.getProperty("line.separator");

            // Save map data into the file line by line
            for (String key : data.keySet())
            {
                if (key == null || key.isEmpty()) continue; // Skip empty settings
                
                out.write(key + separator + data.get(key) + newline);
                //System.out.println("data: " + key + separator + data.get(key) + newline);
            }
            // Close buffers
            out.close();
            fw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Loads data to map from file separated by the separator. Skips comments.
     * @param fileName Path to file where you want to read the data.
     * @param separator Separator character. For ex: "="
     * @param ignoredCommentStartFormat  Character which is used to determine which lines are comments, to not load them.
     * @return Map containing the data.
     */
    public static Map<String, String> loadDataInMapFromFile(String fileName, String separator, String characterEncoding, String ignoredCommentStartFormat)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        // Try to load data into map
        try
        {
            // Open file
            FileInputStream in = new FileInputStream(fileName);

            BufferedReader br = new BufferedReader(new InputStreamReader(in, characterEncoding));
            String nextLine = "";
            String[] splitData = new String[2];

            // Go thru file
            while (nextLine != null)
            {
                nextLine = br.readLine();
                // Insert into map only lines that are not null and are format setting=value
                if (nextLine != null)
                {
                    if (nextLine.startsWith(ignoredCommentStartFormat)) continue;
                    
                    try
                    {
                        splitData = nextLine.split(separator);
                        if (splitData.length < 2) 
                            map.put(splitData[0], "");
                        else
                            map.put(splitData[0], splitData[1]);
                    }
                    catch (Exception e) { }
                }
            }

            // Close streams
            in.close();
            br.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // Return the map
        return map;
    }

    /**
     * Creates new file and path to the file if the file was not found.
     * @param pathToFile Path to the file.
     * @return true if file was created, otherwise false if the file
     * already existed or error occurred creating the file.
     */
    public static boolean createFile(String pathToFile)
    {
        try
        {
            File file = new File(pathToFile);
            if (!file.exists())
            {
                file.getParentFile().mkdirs();
                file.createNewFile();
                return true;
            }
        }
        catch (Exception e)
        {
            System.out.println("Could not create file: " + pathToFile);
            e.printStackTrace();
        }
        return false;
    }
    
}