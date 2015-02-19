package com.jmcdale.remotelogcat.Utils;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.LruCache;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class AppUtils {

    private static final String LOGCAT_REGEX = "^([A-Z])/([^\\(]+)\\(([^\\)]+)\\):(.*)$";
    //    private static final Matcher LOGCAT_MATCHER = Pattern.compile(LOGCAT_REGEX).matcher("");
    private static final LruCache<String, Integer> TAG_COLORS = new LruCache<>(100);
    private static final Integer[] COLORS = new Integer[]{Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.WHITE};
    private static final LinkedList<Integer> COLOR_LIST = new LinkedList<>(Arrays.asList(COLORS));


    public static final String KNOWN_TAG_DALVIK = "dalvikvm";
    public static final String KNOWN_TAG_PROCESS = "Process";
    public static final String KNOWN_TAG_ACTIVITY_MANAGER = "ActivityManager";
    public static final String KNOWN_TAG_ACTIVITY_THREAD = "ActivityThread";

    public static final String TAG_LEVEL_I = "I";
    public static final String TAG_LEVEL_D = "D";
    public static final String TAG_LEVEL_E = "E";
    public static final String TAG_LEVEL_W = "W";
    public static final String TAG_LEVEL_V = "V";

    static {
        TAG_COLORS.put(KNOWN_TAG_DALVIK, Color.BLUE);
        TAG_COLORS.put(KNOWN_TAG_PROCESS, Color.BLUE);
        TAG_COLORS.put(KNOWN_TAG_ACTIVITY_MANAGER, Color.CYAN);
        TAG_COLORS.put(KNOWN_TAG_ACTIVITY_THREAD, Color.CYAN);

        TAG_COLORS.put(TAG_LEVEL_I, Color.BLUE);
        TAG_COLORS.put(TAG_LEVEL_D, Color.CYAN);
        TAG_COLORS.put(TAG_LEVEL_E, Color.RED);
        TAG_COLORS.put(TAG_LEVEL_W, Color.YELLOW);
//        TAG_COLORS.put(TAG_LEVEL_V, Color.BLUE);
    }

    public static synchronized CharSequence formatLogCatLine(String line) {

        Matcher LOGCAT_MATCHER = Pattern.compile(LOGCAT_REGEX).matcher(line);
        LOGCAT_MATCHER.find();
//        LOGCAT_MATCHER.reset(line).find();

//        if(!LOGCAT_MATCHER.matches()){
//            return line;
//        }
        String tagType = null;
        String tag = null;
        String owner = null;
        String message = null;
        try {
            //TODO check groupCount
            tagType = LOGCAT_MATCHER.group(1);
            tag = LOGCAT_MATCHER.group(2);
            owner = LOGCAT_MATCHER.group(3);
            message = LOGCAT_MATCHER.group(4);
        } catch (Exception e) {
            return line;
        }

        CharacterStyle span = new ForegroundColorSpan(getColorForTag(tag));
        SpannableString formattedTag = new SpannableString(tag);
        formattedTag.setSpan(span, 0, formattedTag.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        span = new BackgroundColorSpan(getColorForTag(tagType));
        SpannableString formattedTagType = new SpannableString("[" + tagType + "]");
        formattedTagType.setSpan(span, 0, formattedTagType.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        CharSequence s = TextUtils.concat(formattedTag, " ", formattedTagType, " ", owner, " - ", message);

        return s;
    }

    public static int getColorForTag(String tag) {
        Integer color = TAG_COLORS.get(tag);
        if (color == null) {
            color = getNextColor();
            TAG_COLORS.put(tag, color);
        }
        return color;
    }

    public static int getNextColor() {
        int color = COLOR_LIST.pop();
        COLOR_LIST.offer(color);
        return color;
    }
}
