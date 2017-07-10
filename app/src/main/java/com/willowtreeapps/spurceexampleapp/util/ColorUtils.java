package com.willowtreeapps.spurceexampleapp.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.willowtreeapps.spurceexampleapp.R;

import java.util.Random;

/**
 * Created by teddywilson on 7/10/17.
 */

public class ColorUtils {

    public static final int ICON_COLOR_COUNT = 6;

    public static int randomIconColor(Context context) {
        int val = (int) (Math.random() * 5);
        switch (val) {
            case 0:
                return ContextCompat.getColor(context, R.color.icon_color_1);

            case 1:
                return ContextCompat.getColor(context, R.color.icon_color_2);

            case 2:
                return ContextCompat.getColor(context, R.color.icon_color_3);

            case 3:
                return ContextCompat.getColor(context, R.color.icon_color_4);

            case 4:
                return ContextCompat.getColor(context, R.color.icon_color_5);

            case 5:
                return ContextCompat.getColor(context, R.color.icon_color_6);

        }
        throw new RuntimeException("Invalid color value");
    }

}
