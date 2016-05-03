package util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

/**
 * Other misc functions.
 * @author kulttuuri
 */
public class Other
{
    /**
    * Loads external jar libraries to classpath.
    * @param files HashMap containing path to file and class names.
    * File should contain the path to the .jar archive inside and class
    * name should be in this format: org.apache.commons.io.FileUtils (package.and.classname)
    */
    public static void loadExternalClassesToClasspath(HashMap<File, String> files)
    {
        File file; String className = ""; boolean loadOK = false;
        for (java.util.Map.Entry<File, String> entry : files.entrySet())
        {
            loadOK = false; file = entry.getKey(); className = entry.getValue();
            try
            {
                Class.forName(className);
                loadOK = true;
            }
            catch (Exception e)
            {
                
                try
                {
                    if (!file.exists())
                        throw new IOException("File does not exist: " + file.getAbsolutePath());
                    if (addURLToClasspath(file.toURI().toURL()))
                        System.out.println("Loaded class " + entry.getKey().getAbsolutePath() + " manually.");
                    else
                        throw new Exception("Could not load class!");
                }
                catch (Exception ex)
                {
                    System.out.println("Error loading class " + entry.getKey().getAbsolutePath() + " to classpath. Error: " + ex.getMessage());
                }
            }
            if (loadOK)
                System.out.println("Class " + entry.getValue() + " has already been loaded to classpath succesfully. Sweet!");
        }
    }

    /**
    * Attempts to add the specified URL to the system classpath.
    * @param file - The URL to add to the system classpath.
    * @return True is returned when the URL was successfully added to the 
    * system classpath, false otherwise..
    */
    private static boolean addURLToClasspath(URL file) throws Exception
    {
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> sysclass = URLClassLoader.class;
        Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
        method.setAccessible(true);
        method.invoke(sysloader, new Object[] { file });
        return true;
    }
}