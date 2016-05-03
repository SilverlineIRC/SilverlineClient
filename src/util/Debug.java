package util;

import java.util.HashSet;

/**
 * Class containing static methods for printing debug messages & arraylist & hashmap contents with stacktrace.
 * @author Kulttuuri
 */
public class Debug
{
	/** Is debugging enabled? Disable this when doing a development / stable build. */
	private static boolean debuggingEnabled = true;
	/** Debug message types to be used with {@link #addDebugMessage(String, String)}. **/
	private static HashSet<String> enabledDebugMessageTypes = new HashSet<String>();
	static
	{
		//enabledDebugMessageTypes.add(TYPE_SIGN);
	}
	
	/**
	 * Prints a debug message for the user if {@link #debuggingEnabled debugging is enabled}
	 * and {@link #enabledDebugMessageTypes enabled debug message types} contains the sent type.
	 * @param type Debug message type. Must be inside
	 * {@link #enabledDebugMessageTypes enabled debug message types} for message to be printed.
	 * @param message Message to be printed into the console.
	 */
	public static void addDebugMessage(String type, String message)
	{
		if (!debuggingEnabled) return;
		if (!enabledDebugMessageTypes.contains(type.toLowerCase())) return;
		
		StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
		
		System.out.println("Debug message type: " + type + " and msg: " + message + " ( " + stackTraces[2] + " )");
	}
	
    /**
     * Gets the stack trace as a String.
     * @param delimiter Delimiter for stack trace elements.
     * @return Stacktrace as a String.
     */
    public static String getStackCall(String delimiter)
    {
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        String returnString = "";
        for (int i = 0; i < stackTraces.length; i++)
        {
            if (i > 2)
            {
                returnString += stackTraces[i].toString() + delimiter;
            }
        }
        return returnString;
        //return stackTraces[3].toString();
    }

    /**
     * Debug output message for any primitive type. Prints the given message and stacktrace with default delimiter "\n".
     * @param message message to print into console.
     */
    public static void printDebug(Object message)
    {
        try
        {
            System.out.println(message + getStackCall("\n"));
        }
        catch (Exception e)
        {
            System.out.println("Error printing debug message: " + message);
            e.printStackTrace();
        }
    }

    /**
     * Prints the stacktrace with default delimiter "\n".
     */
    public static void printStackTrace()
    {
        System.out.println(getStackCall("\n"));
    }
}