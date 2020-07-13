package com.aueui.clock.Utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;

public class StatusBarUtils {

    public static void StatusBarTransMode(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

    }


}
