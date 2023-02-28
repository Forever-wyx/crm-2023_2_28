package com.xing.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDateUtils {

    /**
     * 格式化时间 格式："yyyy-MM-dd HH:mm:ss"
     * @param date
     * @return
     */
    public static String formatDateTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 格式化时间 格式："yyyy-MM-dd"
     * @param date
     * @return
     */
    public static String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 格式化时间 格式："HH:mm:ss"
     * @param date
     * @return
     */
    public static String formatTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }
}
