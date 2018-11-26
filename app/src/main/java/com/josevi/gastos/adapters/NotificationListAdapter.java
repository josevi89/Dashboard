package com.josevi.gastos.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.josevi.gastos.R;
import com.josevi.gastos.activities.NewNotificationActivity;
import com.josevi.gastos.activities.NotificationsActivity;
import com.josevi.gastos.models.Notification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.josevi.gastos.utils.Constantes.NOTIFICATION_EDIT;
import static com.josevi.gastos.utils.Constantes.prettyDayDateFormat;
import static com.josevi.gastos.utils.Constantes.timeDateFormat;

public class NotificationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Notification> notifications;
    Activity activity;
    List<String> notificationsToDelete;

    public NotificationListAdapter(List<Notification> notifications, Activity activity) {
        this.notifications = notifications;
        this.activity = activity;
        notificationsToDelete = new ArrayList<String>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_view_notification, null);
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final NotificationViewHolder notificationViewHolder = (NotificationViewHolder) holder;
        final Notification notification = notifications.get(position);

        notificationViewHolder.tag.setText(activity.getResources()
                .getStringArray(R.array.notification_tag_array)[notification.getTag().ordinal()]);
        notificationViewHolder.date.setText(prettyDayDateFormat.format(notification.getDate()));
        notificationViewHolder.time.setText(timeDateFormat.format(notification.getDate()));
        notificationViewHolder.title.setText(notification.getTitle());

        notificationViewHolder.infoView.setLayoutManager(new LinearLayoutManager(activity));
        notificationViewHolder.infoView.setAdapter(new NotificationInfoListAdapter(notification.getInfoList(), activity));

        notificationViewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, NewNotificationActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(NOTIFICATION_EDIT, notification);
                intent.putExtra(NOTIFICATION_EDIT, notification);
                activity.startActivity(intent);
            }
        });

        notificationViewHolder.itemContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (notificationsToDelete.contains(notification.getCode()))
                    unmarkEventToDelete(notificationViewHolder, notification.getCode());
                else
                    markEventToDelete(notificationViewHolder, notification.getCode());
                return false;
            }
        });

        notificationViewHolder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!notificationsToDelete.isEmpty()) {
                    if (notificationsToDelete.contains(notification.getCode()))
                        unmarkEventToDelete(notificationViewHolder, notification.getCode());
                    else
                        markEventToDelete(notificationViewHolder, notification.getCode());
                }
                else {
                    if (notificationViewHolder.infoView.getVisibility() == View.VISIBLE
                            || notificationViewHolder.noInfoCover.getVisibility() == View.VISIBLE) {
                        notificationViewHolder.infoView.setVisibility(View.GONE);
                        notificationViewHolder.noInfoCover.setVisibility(View.GONE);
                    }
                    else {
                        if (notification.getInfoList().isEmpty())
                            notificationViewHolder.noInfoCover.setVisibility(View.VISIBLE);
                        else
                            notificationViewHolder.infoView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void markEventToDelete(NotificationViewHolder holder, String code) {
        holder.itemContainer.setBackgroundColor(activity.getResources().getColor(R.color.black));
        holder.infoView.setVisibility(View.GONE);
        notificationsToDelete.add(code);
        holder.tag.setTextColor(activity.getResources().getColor(R.color.white));
        holder.title.setTextColor(activity.getResources().getColor(R.color.white));
        holder.date.setTextColor(activity.getResources().getColor(R.color.white));
        holder.time.setTextColor(activity.getResources().getColor(R.color.white));
        holder.editBtn.setEnabled(false);
        ((NotificationsActivity)activity).setDeleteButtonVisibility(true);
    }

    public void unmarkEventToDelete(NotificationViewHolder holder, String code) {
        holder.itemContainer.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
        holder.infoView.setVisibility(View.GONE);
        notificationsToDelete.remove(code);
        holder.tag.setTextColor(activity.getResources().getColor(R.color.black));
        holder.title.setTextColor(activity.getResources().getColor(R.color.blue_app));
        holder.date.setTextColor(activity.getResources().getColor(R.color.black));
        holder.time.setTextColor(activity.getResources().getColor(R.color.black));
        holder.editBtn.setEnabled(true);
        if (notificationsToDelete.isEmpty())
            ((NotificationsActivity)activity).setDeleteButtonVisibility(false);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemContainer;
        TextView tag, title, date, time, noInfoCover;
        ImageView editBtn;
        RecyclerView infoView;

        public NotificationViewHolder(View v) {
            super(v);
            this.itemContainer = v.findViewById(R.id.item_view_notification_info);
            this.tag = v.findViewById(R.id.notification_tag);
            this.title = v.findViewById(R.id.notification_title);
            this.date = v.findViewById(R.id.notification_date);
            this.time = v.findViewById(R.id.notification_time);
            this.noInfoCover = v.findViewById(R.id.notification_no_info_cover);
            this.editBtn = v.findViewById(R.id.notification_edit_btn);
            this.infoView = v.findViewById(R.id.notification_info_view);
        }
    }

}
