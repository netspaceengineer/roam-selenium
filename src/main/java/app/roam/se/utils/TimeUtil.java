package app.roam.se.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static String getTimeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return formatter.format(date);
    }
    public static String getTimeStamp(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = new Date();
        return formatter.format(date);
    }
    public static String getMSTimeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssmm");
        Date date = new Date();
        return formatter.format(date);
    }
}
