package com.mrgames13.jimdo.bsbz_app.Tools;

import android.app.Activity;
import android.content.Intent;

import com.mrgames13.jimdo.bsbz_app.R;

public class ThemeUtils {

    //Konstanten
    public final static int FIRST_THEME = 0;
    public final static int SECOND_THEME = 1;
    public final static int THIRD_THEME = 2;

    //Variablen
    private static int apptheme;

    public static void changeToTheme(Activity activity, int theme) {
        apptheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        switch (apptheme) {
            case FIRST_THEME:
                activity.getApplication().setTheme(R.style.FirstTheme);
                activity.setTheme(R.style.FirstTheme);
                break;
          	case SECOND_THEME:
                activity.getApplication().setTheme(R.style.FirstTheme);
                activity.setTheme(R.style.SecondTheme);
                break;
          	case THIRD_THEME:
                activity.getApplication().setTheme(R.style.FirstTheme);
                activity.setTheme(R.style.ThirdTheme);
                break;
        }
    }
}