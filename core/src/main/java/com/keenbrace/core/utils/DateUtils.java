package com.keenbrace.core.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ken on 16/1/20.
 */
public class DateUtils {

    public static Date convertFileDate(int year, int month, int day, int hour, int minute, int second){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
        Date date = new Date();
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String convertSpan(float span){
        int tspan = (int)span;
        int hour =   tspan/3600;
        int minute = tspan%3600/60;
        int second = tspan%3600%60;
        StringBuilder builder = new StringBuilder();
        if (hour!=0){
            builder.append(hour+"时");
        }
        if (minute!=0){
            builder.append(minute+"分");
        }
        if (second!=0) {
            builder.append(second + "秒");
        }
        return builder.toString();
    }

    public static Date convertServerDate(String serverDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = new Date();
        try {
            date = format.parse(serverDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String convert2DateString(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date==null){
            return format.format(new Date());
        }
        return format.format(date);
    }

    public static String convertTotalTime(long totalDuration){
        int hour;int minute;int second;
        hour = (int)(totalDuration/(3600));
        minute = (int)(totalDuration%(3600)/60);
        second = (int)(totalDuration%3600%60);
        DecimalFormat format = new DecimalFormat("00");
        return format.format(hour)+":"+format.format(minute)+":"+format.format(second);
    }

    public static Long parseTotalTime(String totalTime){
        if (totalTime==null||totalTime.length()==0||!totalTime.contains(":")){
            return 0l;
        }
        String[] tems = totalTime.split(":");
        int hour = StringUtils.toInt(tems[0]);
        int minute = StringUtils.toInt(tems[1]);
        int second = StringUtils.toInt(tems[2]);

        return (long)(hour*3600+minute*60+second);
    }


    public static String getCurrentDate(String strFormat){
        SimpleDateFormat format = new SimpleDateFormat(strFormat);
        Date date = new Date();
        return format.format(date);
    }


    public static String formatTimeLength(String timeLength){
        if (StringUtils.isEmpty(timeLength)){
            return null;
        }
        String time[] = timeLength.split(":");
        if (time==null||time.length<3){
            return null;
        }
        return time[0]+"小时"+time[1]+"分"+time[2]+"秒";
    }


    public static String formatEndTime(Date collectTime, long length){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (collectTime==null){
            return null;
        }
        Date date = new Date(collectTime.getTime()+length*1000);
        return format.format(date);
    }

    public static String getNoYearTime(String time){
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = format1.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format2 = new SimpleDateFormat("MM-dd HH:mm:ss");
        return format2.format(date);
    }


    public static String getFriendlyTime(String time){
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = format1.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String format = null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        format = "MM-dd HH:mm";

        if (hour > 17) {
            format = "MM-dd 晚上 hh:mm";

        }else if(hour >= 0 && hour <= 6){
            format = "MM-dd 凌晨 hh:mm";
        } else if (hour > 11 && hour <= 17) {
            format = "MM-dd 下午 hh:mm";
        } else {
            format = "MM-dd 上午 hh:mm";
        }
        SimpleDateFormat format2  = new SimpleDateFormat(format);
        return format2.format(date);
    }

    public static boolean isSameDay(String strDate, Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date pDate;
        try {
            pDate = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            pDate = new Date();
            return false;
        }
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(pDate);
        calendar2.setTime(date);
        if (calendar1.get(Calendar.YEAR)==calendar2.get(Calendar.YEAR)
                &&calendar1.get(Calendar.MONTH)==calendar2.get(Calendar.MONTH)
                &&calendar1.get(Calendar.DAY_OF_MONTH)==calendar2.get(Calendar.DAY_OF_MONTH)){
            return true;
        }
        return false;
    }

    public static Date convertServerDate2(String serverDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = format.parse(serverDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String convert2DateString2(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (date==null){
            return format.format(new Date());
        }
        return format.format(date);
    }
}
