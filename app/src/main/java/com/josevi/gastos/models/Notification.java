package com.josevi.gastos.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.josevi.gastos.models.enums.NotificationTag;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.josevi.gastos.utils.Constantes.shortDateFormat;

public class Notification implements Comparable<Notification>, Parcelable{

    Date date;
    String title;
    NotificationTag tag;
    List<Pair<String, String>> infoList;

    public Notification(Date date, String title, NotificationTag tag) {
        this.date = date;
        this.title = title;
        this.tag = tag;
    }

    protected Notification(Parcel in) {
        try {
            date = shortDateFormat.parse(in.readString());
        }
        catch (ParseException pe) {
            date = new Date();
        }
        title = in.readString();
        tag = NotificationTag.values()[in.readInt()];
        infoList = new ArrayList<Pair<String, String>>();
        int N = in.readInt();
        if (N > 0)
            for (int n = 0; n < N; n++)
                infoList.add(new Pair(in.readString(), in.readString()));
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCode() {
        String code = title;
        while (code.length() < 3)
            code += "_";
        code = code.substring(0, 3).toUpperCase();
        code += shortDateFormat.format(date);
        return code;
    }

    public Calendar getCalendar() {
        if (this.date != null) {
            Calendar date = Calendar.getInstance();
            date.setTime(this.date);
            return date;
        }
        else
            return null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public NotificationTag getTag() {
        return tag;
    }

    public void setTag(NotificationTag tag) {
        this.tag = tag;
    }

    public List<Pair<String, String>> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<Pair<String, String>> infoList) {
        this.infoList = infoList;
    }

    @Override
    public int compareTo(@NonNull Notification other) {
        if (this.getCalendar() != null && other.getCalendar() != null) {
            if (this.getCalendar().before(other.getCalendar()))
                return 1;
            else if (this.getCalendar().after(other.getCalendar()))
                return -1;
            else
                return 0;
        }
        else if (this.getCalendar() != null)
            return 1;
        else if (other.getCalendar() != null)
            return -1;
        else
            return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shortDateFormat.format(date));
        parcel.writeString(title);
        parcel.writeInt(tag.ordinal());
        if (infoList == null || infoList.isEmpty())
            parcel.writeInt(0);
        else {
            parcel.writeInt(infoList.size());
            for (Pair<String, String> info: infoList) {
                parcel.writeString(info.first);
                parcel.writeString(info.second);
            }
        }
    }
}
