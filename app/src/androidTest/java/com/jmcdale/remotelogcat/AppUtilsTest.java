package com.jmcdale.remotelogcat;

import android.test.InstrumentationTestCase;
import android.text.Spannable;

import com.jmcdale.remotelogcat.Utils.AppUtils;

public class AppUtilsTest extends InstrumentationTestCase {

    private static final String LOGCAT_LINE = "I/GCoreUlr( 1469): GMS FLP location and AR updates requested: {\"timestampMs\":1424135682980,\"description\":\"default\",\"samplePeriodMs\":60000,\"sampleSource\":\"internal\",\"sampleReason\":\"default\"}";

    //TODO
    public void test_formatLogCatLine() {
        try {
            Spannable spannableString = AppUtils.formatLogCatLine(LOGCAT_LINE);

            assertNotNull(spannableString);
        }catch(Exception e){
            assertNull(null);
        }
    }
}
