package com.aman.sandwichclub.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefsUtils {

    private static final String CURRENT_LAYOUT_ID = "CURRENT_LAYOUT_ID";
    public static final int LIST_LAYOUT_ID = 1;
    public static final int GRID_LAYOUT_ID = 2;

    public static void saveItemType(Context context, int selectedLayout) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(CURRENT_LAYOUT_ID, selectedLayout);
        editor.apply();
    }

    public static int getItemType(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(CURRENT_LAYOUT_ID, LIST_LAYOUT_ID);
    }


}
