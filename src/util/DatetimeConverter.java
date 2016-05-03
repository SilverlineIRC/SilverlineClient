package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Zuppi
 */
public class DatetimeConverter {

    private static Calendar calendar;

    public static String convertEid(Long eid){
        if (calendar == null){
            calendar = Calendar.getInstance();
        }
        calendar.setTimeInMillis(eid/1000);
        SimpleDateFormat tformat = new SimpleDateFormat("kk:mm");
        return tformat.format(new Date(calendar.getTimeInMillis()));
    }
    
    public static String convertUnixEpoch(Long unixstamp){
        if (calendar == null){
            calendar = Calendar.getInstance();
        }
        
        calendar.setTimeInMillis(unixstamp*1000);
        SimpleDateFormat tformat = new SimpleDateFormat("dd.MM.yyyy kk:mm");
        return tformat.format(new Date(calendar.getTimeInMillis()));
    }
    
    
}
