package com.jmcdale.remotelogcat.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jmcdale.remotelogcat.Adapters.LogCatAdapter;
import com.jmcdale.remotelogcat.Models.LogCatLine;
import com.jmcdale.remotelogcat.R;
import com.jmcdale.remotelogcat.Utils.SharedPrefsUtil;

import java.net.URISyntaxException;

public class LogCatFragment extends Fragment {

    private static String TAG = LogCatFragment.class.getSimpleName();

//    private static String IP_ADDRESS = "192.168.1.129";//home
    //    private static String IP_ADDRESS = "172.26.1.63";//work
//private static String IP_ADDRESS = "172.26.1.46";//work mini
//    private static String PORT = "3000";
//    private static String SOCKET_ADDRESS = String.format("http://%s:%s", IP_ADDRESS, PORT);

    private static String NEW_LOGCAT_LINE = "NEW_LOGCAT_LINE";

    private Gson gson = new GsonBuilder().create();

    private Socket mSocket;

    private RecyclerView rvLogCat;
    private LogCatAdapter adapter;

    private String baseAddress;
    private String port;

    private boolean lockScroll = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if(args == null){
            throw new IllegalArgumentException("Server address is required.");
        }

        baseAddress = args.getString(SharedPrefsUtil.KEY_SERVER_BASE_ADDRESS);
        port = args.getString(SharedPrefsUtil.KEY_SERVER_PORT);

        connectSocketAndListen();

        adapter = new LogCatAdapter();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                if (rvLogCat != null && lockScroll) {
                    rvLogCat.scrollToPosition(adapter.getItemCount() - 1);
                }
            }
        });

        setRetainInstance(true);
    }

    private String makeSocketAddress() {
        return String.format("http://%s:%s", baseAddress, port);
    }

    private void connectSocketAndListen() {
        if(mSocket == null){
            try {
                mSocket = IO.socket(makeSocketAddress());
            } catch (URISyntaxException e) {
            }
        }
        if (!mSocket.connected()) {
            mSocket.connect();
            mSocket.on(NEW_LOGCAT_LINE, new Emitter.Listener() {
                @Override
                public void call(final Object... args) {

                    for (Object arg : args) {
                        final LogCatLine line = gson.fromJson(arg.toString(), LogCatLine.class);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addLine(line);
                            }
                        });
                    }

                }
            });
        }
    }

    private void disconnectSocketAndListeners() {
        if (mSocket.connected()) {
            mSocket.disconnect();
            mSocket.off(NEW_LOGCAT_LINE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_logcat, container, false);

        rvLogCat = (RecyclerView) rootView.findViewById(R.id.rvLogCat);
        rvLogCat.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvLogCat.setAdapter(adapter);
        rvLogCat.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                    lockScroll = true;
                } else {
                    lockScroll = false;
                }
            }
        });
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        disconnectSocketAndListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        connectSocketAndListen();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public static LogCatFragment newInstance(String baseAddress, String port) {
        Bundle b = new Bundle();
        b.putString(SharedPrefsUtil.KEY_SERVER_BASE_ADDRESS, baseAddress);
        b.putString(SharedPrefsUtil.KEY_SERVER_PORT, port);

        LogCatFragment frag = new LogCatFragment();
        frag.setArguments(b);
        return frag;
    }
}
