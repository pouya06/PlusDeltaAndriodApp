package com.example.pouyakarimi.testappone.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pouyakarimi on 10/27/15.
 */
public class DateUtil {
    public static String currentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
