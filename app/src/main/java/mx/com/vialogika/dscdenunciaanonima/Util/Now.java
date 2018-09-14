package mx.com.vialogika.dscdenunciaanonima.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Now {
    public static String getDate(){
        SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd");
        return today.format(new Date());
    }

    public static String getHour(){
        SimpleDateFormat now = new SimpleDateFormat("HH:mm");
        return now.format(new Date());
    }

    public static String getCurrentDateTime(){
        SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return now.format(new Date());
    }
}
