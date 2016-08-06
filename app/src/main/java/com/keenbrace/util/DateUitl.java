package com.keenbrace.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUitl
{
   

    public static String getCreateTime2()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String timeString = simpleDateFormat.format(new Date());
        return timeString;
    }
    public static String getCreateTime2(long times)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String timeString = simpleDateFormat.format(times);
        return timeString;
    }
    public static String getCreateTime3(String phone)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String timeString = simpleDateFormat.format(new Date()) + "_" + phone;
        return timeString;
    }

    public static String getDateFormat(long timeMillis)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeString = simpleDateFormat.format(new Date(timeMillis));
        return timeString;
    }

    public static String getDateFormat2(Date date)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeString = simpleDateFormat.format(date);
        return timeString;
    }

    public static String getDateFormat3(long timeMillis)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String timeString = simpleDateFormat.format(new Date(timeMillis));
        return timeString;
    }

    public static String getDateFormat4(long timeMillis)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String timeString = simpleDateFormat.format(new Date(timeMillis));
        return timeString;
    }

 

    public static long getTimesInMillis(String time) throws ParseException
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(simpleDateFormat.parse(time));
        long millis = cal.getTimeInMillis();
        return millis;
    }
    public static long getTimesInMillis2(String time) throws ParseException
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(simpleDateFormat.parse(time));
        long millis = cal.getTimeInMillis();
        return millis;
    }


    public static long getDateTimeFromString2Long(String dateTime) throws ParseException
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(simpleDateFormat.parse(dateTime));
        long millis = cal.getTimeInMillis();
        return millis;
    }

    public static String getDateTimeFromLong2String(long dateTime)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeString = simpleDateFormat.format(new Date(dateTime));
        return timeString;
    }

    public static String getDateTimeFromString2String(String date)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日HH:mm");
        String timeString = "";
        try
        {
            long dateTime = getDateTimeFromString2Long(date);
            timeString = simpleDateFormat.format(new Date(dateTime));
        } catch (ParseException e)
        {

        }
        return timeString;
    }

    public static long getDateTimeFromString2LongWithHHMMSS(String HHMMSS) throws ParseException
    {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = sdfDate.format(System.currentTimeMillis());
        String dateTime = dateString + " " + HHMMSS;
        long dateTimeLong = getDateTimeFromString2Long(dateTime);
        if (dateTimeLong < System.currentTimeMillis())
        {
            dateTimeLong += 24 * 60 * 60 * 1000;
        }
        Log.v("test", "test time = " + getDateTimeFromLong2String(dateTimeLong));
        return dateTimeLong;
    }

    public static long getDateTimeFromString(String startTime, String HHMMSS) throws ParseException
    {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = sdfDate.format(getDateTimeFromString2Long(startTime));
        String dateTime = dateString + " " + HHMMSS;
        long dateTimeLong = getDateTimeFromString2Long(dateTime);
        if (dateTimeLong < System.currentTimeMillis())
        {
            dateTimeLong = getDateTimeFromString2LongWithHHMMSS(HHMMSS);
        }
        Log.v("test", "test time = " + getDateTimeFromLong2String(dateTimeLong));
        return dateTimeLong;
    }

    public static String getDateFormat4Id(long timeMillis)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeString = simpleDateFormat.format(new Date(timeMillis));
        return timeString;
    }

    public static int getCurrentMonth()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        String timeString = simpleDateFormat.format(new Date());
        return Integer.valueOf(timeString);
    }
   public static String getDatetoString(long times)
   {
	   SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	   String timeString = simpleDateFormat.format(new Date(times));
       return timeString;
   }
   public static long getDateTolong(String date) throws ParseException
   {
	   SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
       Calendar cal = Calendar.getInstance();
       cal.setTime(simpleDateFormat.parse(date));
       long millis = cal.getTimeInMillis();
       return millis;
   }
   public static long getTimesInMillis4(String time) throws ParseException
   {
	   SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	       Calendar cal = Calendar.getInstance();
       cal.setTime(simpleDateFormat.parse(time));
       long millis = cal.getTimeInMillis();
       return millis;
   }
   public static String getMonthDay(long times)
   {
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
       String timeString = simpleDateFormat.format(times);
       return timeString;
   }
    public static String formatToM(float b) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
        return df.format(b);

    }
}
