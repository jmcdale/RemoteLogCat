package com.jmcdale.remotelogcat.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.jmcdale.remotelogcat.Fragments.LogCatFragment;
import com.jmcdale.remotelogcat.R;
import com.jmcdale.remotelogcat.Utils.SharedPrefsUtil;

public class LogCatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String baseAddress = intent.getStringExtra(SharedPrefsUtil.KEY_SERVER_BASE_ADDRESS);
        String port = intent.getStringExtra(SharedPrefsUtil.KEY_SERVER_PORT);

        if (TextUtils.isEmpty(baseAddress)) {
            throw new IllegalArgumentException("Server address is required.");
        }

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, LogCatFragment.newInstance(baseAddress, port))
                    .commit();
        }
    }

    public static Intent newIntent(Context context, String baseAddress, String port) {
        Intent intent = new Intent(context, LogCatActivity.class);
        intent.putExtra(SharedPrefsUtil.KEY_SERVER_BASE_ADDRESS, baseAddress);
        intent.putExtra(SharedPrefsUtil.KEY_SERVER_PORT, port);
        return intent;
    }
}
