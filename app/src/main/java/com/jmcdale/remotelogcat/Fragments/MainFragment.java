package com.jmcdale.remotelogcat.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jmcdale.remotelogcat.Activities.LogCatActivity;
import com.jmcdale.remotelogcat.R;
import com.jmcdale.remotelogcat.Utils.SharedPrefsUtil;

public class MainFragment extends Fragment implements View.OnClickListener {

    private EditText etAddress;
    private EditText etPort;
    private Button btnConnect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        etAddress = (EditText) rootView.findViewById(R.id.etAddress);
        etAddress.setText(SharedPrefsUtil.getServerBaseAddress(getActivity()));

        etPort = (EditText) rootView.findViewById(R.id.etPort);
        etPort.setText(SharedPrefsUtil.getServerPort(getActivity()));

        btnConnect = (Button) rootView.findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConnect:
                String address = etAddress.getText().toString();
                String port = etPort.getText().toString();

                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(getActivity(), "Server address is required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                edit.putString(SharedPrefsUtil.KEY_SERVER_BASE_ADDRESS, address);
                edit.putString(SharedPrefsUtil.KEY_SERVER_PORT, port);
                edit.apply();

                Intent intent = LogCatActivity.newIntent(getActivity(), address, port);
                startActivity(intent);
                break;
        }
    }
}
