package com.jmcdale.remotelogcat.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jmcdale.remotelogcat.Models.LogCatLine;
import com.jmcdale.remotelogcat.R;
import com.jmcdale.remotelogcat.Utils.AppUtils;

import java.util.LinkedList;

public class LogCatAdapter extends RecyclerView.Adapter<LogCatAdapter.ViewHolder> {

    private static final int MAX_LINES = 500;

    private LinkedList<LogCatLine> logCatLines = new LinkedList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_logcat_line, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.tvLogLevel.setText(logCatLines.get(position).getLogLevel());
        viewHolder.tvTag.setText(logCatLines.get(position).getTag());
        viewHolder.tvPid.setText("(" + String.valueOf(logCatLines.get(position).getPid()) + ")");
        viewHolder.tvLog.setText(logCatLines.get(position).getLog());

        int logLevelBgColor = Color.WHITE;
        int logLevelFgColor = Color.BLACK;
        switch (logCatLines.get(position).getLogLevel()){
            case "I":
                logLevelBgColor = Color.BLUE;
                logLevelFgColor = Color.WHITE;
                break;
            case "W":
                logLevelBgColor = Color.YELLOW;
                logLevelFgColor = Color.BLACK;
                break;
            case "D":
                logLevelBgColor = Color.DKGRAY;
                logLevelFgColor = Color.WHITE;
                break;
            case "E":
                logLevelBgColor = Color.RED;
                logLevelFgColor = Color.WHITE;
                break;
            case "V":
                break;
//            default:

        }
        viewHolder.tvLogLevel.setBackgroundColor(logLevelBgColor);
        viewHolder.tvLogLevel.setTextColor(logLevelFgColor);

        viewHolder.tvTag.setTextColor(AppUtils.getColorForTag(logCatLines.get(position).getTag()));
    }

    @Override
    public int getItemCount() {
        return logCatLines.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvLogLevel;
        TextView tvTag;
        TextView tvPid;
        TextView tvLog;

        public ViewHolder(View itemView) {
            super(itemView);

            tvLogLevel = (TextView) itemView.findViewById(R.id.tvLogLevel);
            tvTag = (TextView) itemView.findViewById(R.id.tvTag);
            tvPid = (TextView) itemView.findViewById(R.id.tvPid);
            tvLog = (TextView) itemView.findViewById(R.id.tvLog);
        }
    }

    //addToTop
//    public void addLine(String line) {
//        l++;
//        line = l + " " + line;
//        if (logCatLines.size() > MAX_LINES) {
//            logCatLines.removeLast();
//            notifyItemRemoved(logCatLines.size());
//        }
//        logCatLines.push(line);
//        notifyItemInserted(0);
//    }

    //addToBottom
    public void addLine(LogCatLine line) {
        if (logCatLines.size() > MAX_LINES) {
            logCatLines.pop();
            notifyItemRemoved(0);
        }
        logCatLines.offer(line);
        notifyItemInserted(logCatLines.size() - 1);
    }

    //addToBottom
//    public void addLine(String line) {
//        if (logCatLines.size() > MAX_LINES) {
//            logCatLines.pop();
//            notifyItemRemoved(0);
//        }
//        logCatLines.offer(line);
//        notifyItemInserted(logCatLines.size() - 1);
//    }

}