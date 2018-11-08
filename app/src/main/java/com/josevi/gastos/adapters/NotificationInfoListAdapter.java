package com.josevi.gastos.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.josevi.gastos.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NotificationInfoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Pair<String, String>> notificationInfos;
    Activity activity;

    public NotificationInfoListAdapter(List<Pair<String, String>> notificationInfos, Activity activity) {
        this.notificationInfos = notificationInfos;
        this.activity = activity;
    }

    public void setNotificationInfos(List<Pair<String, String>> notificationInfos) {
        this.notificationInfos = notificationInfos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_view_notification_info, null);
        return new NotificationInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final NotificationInfoViewHolder notificationInfoViewHolder = (NotificationInfoViewHolder) holder;

        notificationInfoViewHolder.key.setText(notificationInfos.get(position).first);
        notificationInfoViewHolder.info.setText(notificationInfos.get(position).second);
    }

    @Override
    public int getItemCount() {
        return notificationInfos.size();
    }

    public static class NotificationInfoViewHolder extends RecyclerView.ViewHolder {

        TextView key, info;

        public NotificationInfoViewHolder(View v) {
            super(v);
            this.key = v.findViewById(R.id.notification_info_key);
            this.info = v.findViewById(R.id.notification_info_info);
        }
    }

}
