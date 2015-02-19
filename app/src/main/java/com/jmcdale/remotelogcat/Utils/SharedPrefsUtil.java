package com.jmcdale.remotelogcat.Utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class SharedPrefsUtil {

    public static final String KEY_SERVER_BASE_ADDRESS = "KEY_SERVER_BASE_ADDRESS";
    public static final String KEY_SERVER_PORT = "KEY_SERVER_PORT";

    public static String getServerBaseAddress(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_SERVER_BASE_ADDRESS, "");
    }

    public static String getServerPort(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_SERVER_PORT, "");
    }
}
