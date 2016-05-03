/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Zuppi
 */
public class Crashlogger {
    
    private final static File crashlogFolder = new File("./errlogs/");
    private final static boolean loggingToFile = false;
      
    public static void logCrash(Exception e, Map crashmap){
        if (loggingToFile){
            if (!crashlogFolder.exists()){
                crashlogFolder.mkdir();
            }
            logToFile(e, crashmap);
        }
        else{
            logToConsole(e, crashmap);
        }
    }
    
    private static void logToConsole(Exception e, Map crashmap){
        System.out.println("-----CAUGHT ERROR-----");
        System.out.println("REASON: "+e.toString());
        System.out.println("STACKTRACE:");
        for (StackTraceElement ste : e.getStackTrace()) {
            System.out.println(ste);
        }
        if (crashmap != null){
            System.out.println("MAP:");
            System.out.println(crashmap.toString());
        }
        System.out.println("-----ERROR LOG ENDING-----");
        System.exit(1);     
    }
    
    private static void logToFile(Exception e, Map crashmap){
        try{
            System.out.println("WRITING ERROR LOG TO FILE");
            SimpleDateFormat errorformat = new SimpleDateFormat("ddMMyyyykkmm");
            Date date = new Date();
            
            
            PrintWriter writer = new PrintWriter(crashlogFolder.getCanonicalPath()+"\\"+errorformat.format(date)+".txt", "UTF-8");
            writer.println("-----ERROR LOG STARTING-----");
            writer.println("REASON: "+e.toString());
            writer.println("STACKTRACE:");
            for (StackTraceElement ste : e.getStackTrace()) {
                writer.println(ste);
            }
            writer.println("-----ERROR LOG ENDING-----");
            writer.close();
            System.out.println("WRITING COMPELETE");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        System.exit(1);
    }
}
