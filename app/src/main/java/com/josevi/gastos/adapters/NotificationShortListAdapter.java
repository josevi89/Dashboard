package com.josevi.gastos.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.josevi.gastos.R;
import com.josevi.gastos.models.Notification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.josevi.gastos.utils.Constantes.timeDateFormat;

public class NotificationShortListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Notification> notifications;
    Activity activity;
    List<String> notificationsToDelete;

    public NotificationShortListAdapter(List<Notification> notifications, Activity activity) {
        this.notifications = notifications;
        this.activity = activity;
        notificationsToDelete = new ArrayList<String>();
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_view_notification_short, null);
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final NotificationViewHolder notificationViewHolder = (NotificationViewHolder) holder;
        final Notification notification = notifications.get(position);

        notificationViewHolder.tag.setText(activity.getResources()
                .getStringArray(R.array.notification_tag_array)[notification.getTag().ordinal()]);
        Calendar notificationDate = Calendar.getInstance();
        notificationDate.setTime(notification.getDate());
        String date = String.valueOf(notificationDate.get(Calendar.DAY_OF_MONTH)) +" "
                +activity.getResources().getStringArray(
                        R.array.months_short_array)[notificationDate.get(Calendar.MONTH)] +" "
                + timeDateFormat.format(notificationDate.getTime());
        notificationViewHolder.date.setText(date);
        notificationViewHolder.title.setText(notification.getTitle());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView tag, title, date;

        public NotificationViewHolder(View v) {
            super(v);
            this.tag = v.findViewById(R.id.notification_short_tag);
            this.title = v.findViewById(R.id.notification_short_title);
            this.date = v.findViewById(R.id.notification_short_date);
        }
    }
}
