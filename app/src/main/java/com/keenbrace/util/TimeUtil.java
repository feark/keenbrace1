/**
 * @(#) TimeUtil.java Created on 2013-4-18
 *
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.keenbrace.util;

import android.util.Log;

/**
 * The class <code>TimeUtil</code>
 * 
 * @author linjunsui
 * @version 1.0
 */
public class TimeUtil
{



    /**
     * request intervals = 10 minutes
     */
    public static long            REQUEST_INTERVALS = 10 * 60 * 1000;

    /**
     * request timeout 10 seconds
     */
    public static int             REQUEST_TIMEOUT   = 10 * 1000;



    /**
     * last ntp time
     */
    protected static long         lastNtpTime       = 0L;

    /**
     * diff of the (localtime - ntp server time);
     */
    protected static long         localNtpDiff      = 0L;

    public static void resetTime()
    {
        Log.v("test", "........... resetTime from local ...........");
        lastNtpTime = 0;
        localNtpDiff = 0;
    }

    /**
     * getTime
     * 
     * @return
     */
    public static long getTime(boolean isRealTime)
    {
        Log.v("test", "................. getTime  .................");
        long now = System.currentTimeMillis();
        //        if (isRealTime)
        //        {
        //            if (Math.abs(now + localNtpDiff - lastNtpTime) > REQUEST_INTERVALS)
        //            {
        //                Log.v("test", "........... getTime from server ...........");
        //                try
        //                {
        //                    URL url = new URL("http://www.bjtime.cn");
        //                    URLConnection uc = url.openConnection();
        //                    uc.connect();
        //                    lastNtpTime = uc.getDate();
        //                    Log.v("test", "lastNtpTime ========================= " + DateUitl.getDateFormat(lastNtpTime));
        //                    localNtpDiff = lastNtpTime - now;
        //                } catch (Exception e)
        //                {
        //                    logger.error("Error while get time from ntp server:" + "http://www.bjtime.cn", e);
        //                    lastNtpTime = 0;
        //                    localNtpDiff = 0;
        //                    return now;
        //                }
        //            }
        //        }
        //        Log.v("test", "NowTime ========================= " + DateUitl.getDateFormat(now + localNtpDiff));
        //        return now + localNtpDiff;
        return now;
    }

}
