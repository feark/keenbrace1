package com.keenbrace.core.datepicker;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.keenbrace.core.datepicker.util.LogUtils;


public class ScreenHelper {

    /**
     * The type Screen.
     */
    public static class Screen {
        /**
         * The Width pixels.
         */
        public int widthPixels;
        /**
         * The Height pixels.
         */
        public int heightPixels;
        /**
         * The Density dpi.
         */
        public int densityDpi;
        /**
         * The Density.
         */
        public float density;
    }

    /**
     * Gets screen pixels.
     *
     * @param context the context
     * @return the screen pixels
     */
    public static Screen getScreenPixels(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        Screen screen = new Screen();
        screen.widthPixels = dm.widthPixels;// e.g. 1080
        screen.heightPixels = dm.heightPixels;// e.g. 1920
        screen.densityDpi = dm.densityDpi;// e.g. 480
        screen.density = dm.density;// e.g. 2.0
        LogUtils.debug("width=" + screen.widthPixels + ", height=" + screen.heightPixels
                + ", densityDpi=" + screen.densityDpi + ", density=" + screen.density);
        return screen;
    }

}
