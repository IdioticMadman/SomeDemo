package com.example.customview;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by robert on 2017/4/4.
 */

public class MeasureUtil {

    public static int[] getScreenSize(Activity activity) {
        int[] screenSize = new int[2];
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenSize[0] = displayMetrics.widthPixels;
        screenSize[1] = displayMetrics.heightPixels - 180;
        return screenSize;
    }
}
