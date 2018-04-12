package com.cfp.metpollen.view.utilities;

import android.util.Log;

import com.cfp.metpollen.R;
import com.cfp.metpollen.view.interfaces.BackgroundUpdateListener;

/**
 * Created by AhmedAbbas on 11/28/2017.
 */

public class BackgroundUtils {

    private static int backgroundId = R.drawable.bg_c;
    private static String backgroundUrl = null;
    private static int backgroundIdBlur = R.drawable.bg_c_blur;
    private static BackgroundUpdateListener backgroundUpdateListener;
    private static int time = 0;

    public static String getBackgroundUrl() {
        return backgroundUrl;
    }

    public static void setBackgroundUrl(String backgroundUrl) {
        BackgroundUtils.backgroundUrl = backgroundUrl;
    }

    public static void setBackgroundUpdateListener(BackgroundUpdateListener backgroundUpdateListener) {
        BackgroundUtils.backgroundUpdateListener = backgroundUpdateListener;
    }

    public static int getBackgroundIdBlur() {
        return backgroundIdBlur;
    }

    public static void setBackgroundIdBlur(int blur) {
        backgroundIdBlur = blur;
    }

    public static int getBackgroundId() {
        return backgroundId;
    }

    public static void setBackgroundId(int background) {
        backgroundId = background;
    }

    public static void updateBackground(int time) {
        switch (time) {
            case 0:
                setBackgroundId(R.drawable.bg_c);
                setBackgroundIdBlur(R.drawable.bg_c_blur);
                break;
            case 5:
                setBackgroundId(R.drawable.isb_day);
                setBackgroundIdBlur(R.drawable.isb_day_blur);
                break;
            case 12:
                setBackgroundId(R.drawable.khi_day);
                setBackgroundIdBlur(R.drawable.khi_day_blur);
                break;
            case 16:
                setBackgroundId(R.drawable.isb_night);
                setBackgroundIdBlur(R.drawable.isb_night_blur);
                break;
        }
        backgroundUpdateListener.updatebackground();
    }

    public static void settime() {
        if (time == 0) {
            time = 5;
        } else if (time == 5) {
            time = 12;
        } else if (time == 12) {
            time = 16;
        } else if (time == 16) {
            time = 0;
        }
        Log.i("time = ", time + "");

        updateBackground(time);
    }
}
