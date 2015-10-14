package com.android_dev.tatenuufrn.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by adelinosegundo on 10/14/15.
 */
public class DateHelper {
    public static String getCurrentDateInFormat(String format){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format);
        String formattedDate = df.format(c.getTime());

        System.out.println("Formated date => " + formattedDate);
        return formattedDate;
    }
}
