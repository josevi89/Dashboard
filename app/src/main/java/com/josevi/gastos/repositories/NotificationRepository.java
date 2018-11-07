package com.josevi.gastos.repositories;

import android.app.Activity;
import android.content.ContentResolver;

import com.josevi.gastos.models.Notification;
import com.josevi.gastos.models.enums.NotificationTag;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.josevi.gastos.utils.Constantes.dayDateFormat;
import static com.josevi.gastos.utils.Constantes.shortDateFormat;

public class NotificationRepository {

    Map<Integer, Map<Integer, Map<Integer, List<Notification>>>> notificationsMap;

    private ContentResolver contentResolver;
    private Activity activity;

    public NotificationRepository(Activity activity) {
        this.activity = activity;
        this.contentResolver = activity.getContentResolver();
    }

    public NotificationRepository() {

        Map<Integer, Map<Integer, Map<Integer, List<Notification>>>> notificationsMap =
                new HashMap<Integer, Map<Integer, Map<Integer,List<Notification>>>>();

        Date date201811012114 = new Date();
        try {
            date201811012114 = shortDateFormat.parse("201811011114");
        } catch (ParseException pe) {}
        Notification newNotification201811012114 = new Notification(date201811012114, "Noviembre", NotificationTag.EVENT);
        addToMap("01/11/2018", newNotification201811012114, notificationsMap);

        Date date201812012114 = new Date();
        try {
            date201812012114 = shortDateFormat.parse("201812011114");
        } catch (ParseException pe) {}
        Notification newNotification201812012114 = new Notification(date201812012114, "Diciembre", NotificationTag.EVENT);
        addToMap("01/12/2018", newNotification201812012114, notificationsMap);

        this.notificationsMap = Collections.unmodifiableMap(notificationsMap);
    }

    public void addToMap(String dayFormatted, Notification shipping,
                         Map<Integer, Map<Integer, Map<Integer, List<Notification>>>> shippingsMap) {
        String[] daySplitted = dayFormatted.split("/");
        Integer year = Integer.parseInt(daySplitted[2]),
                month = Integer.parseInt(daySplitted[1]),
                day = Integer.parseInt(daySplitted[0]);
        try {
            shippingsMap.get(year).get(month).get(day).add(shipping);
        }
        catch (Exception e) {
            addToMapSafe(year, month, day, shipping, shippingsMap);
        }
    }

    public void addToMapSafe(Integer year, Integer month, Integer day, Notification shipping,
                             Map<Integer, Map<Integer, Map<Integer, List<Notification>>>> shippingsMap) {
        if (!shippingsMap.containsKey(year))
            shippingsMap.put(year, new HashMap<Integer, Map<Integer, List<Notification>>>());
        if (!shippingsMap.get(year).containsKey(month))
            shippingsMap.get(year).put(month, new HashMap<Integer, List<Notification>>());
        if (!shippingsMap.get(year).get(month).containsKey(day))
            shippingsMap.get(year).get(month).put(day, new ArrayList<Notification>());
        shippingsMap.get(year).get(month).get(day).add(shipping);
    }

    public List<Notification> getNotificationsNextWeek() {
        Calendar aux = Calendar.getInstance();
        List<Notification> notificationsToReturn = new ArrayList<>();
        for (int d = 0; d < 7; d++) {
            notificationsToReturn.addAll(getNotificationsInDay(aux));
            aux.add(Calendar.DAY_OF_YEAR, +1);
        }
        return notificationsToReturn;
    }

    public List<Notification> getNotificationsInDay(Calendar date) {
        try {
            Integer year = date.get(Calendar.YEAR),
                    month = date.get(Calendar.MONTH) + 1,
                    day = date.get(Calendar.DAY_OF_MONTH);
            List<Notification> notificationsToReturn = notificationsMap.get(year).get(month).get(day);
            Collections.sort(notificationsToReturn);
            return notificationsToReturn;
        }
        catch (Exception e) {
            return new ArrayList<Notification>();
        }
    }

    public List<Notification> getNotificationsInMonth(Calendar date) {
        Integer year = date.get(Calendar.YEAR),
                month = date.get(Calendar.MONTH) + 1;
        List<Notification> notificationsToReturn = new ArrayList<Notification>();
        for (int d = 1; d < 31; d++)
            try {
                notificationsToReturn.addAll(notificationsMap.get(year).get(month).get(d));
            }
            catch (Exception e) {}
        Collections.sort(notificationsToReturn);
        return notificationsToReturn;
    }

    public List<CalendarDay> getNotificationDatesInMonth(Calendar date) {
        Integer year = date.get(Calendar.YEAR),
                month = date.get(Calendar.MONTH) + 1;
        List<CalendarDay> datesToReturn = new ArrayList<CalendarDay>();
        for (int d = 1; d < 31; d++) {
            try {
                if (!notificationsMap.get(year).get(month).get(d).isEmpty()) {
                    Calendar day = Calendar.getInstance();
                    day.set(year, month - 1, d, 0, 0, 0);
                    datesToReturn.add(new CalendarDay(day));
                }
            } catch (Exception e) {}
        }
        return datesToReturn;
    }

//    public List<List<List<Notification>>> getNotificationsInThreeMonths2(Calendar date) {
//        List<List<List<Notification>>> monthNotifications = new ArrayList<List<List<Notification>>>();
//        for (int m = 0; m < 3; m++) {
//            monthNotifications.add(new ArrayList<List<Notification>>());
//            for (int d = 0; d < 30; d++) {
//                monthNotifications.get(m).add(new ArrayList<Notification>());
//                if (m == 0 && d == 27)
//                    monthNotifications.get(m).get(d).addAll(getAllNotifications());
//            }
//        }
//        return monthNotifications;
//    }

//    public List<Notification> getAllNotifications() {
//        List<Notification> NotificationsToSend = new ArrayList<Notification>();
//
//        Uri NotificationsUri = DBContract.NotificationsEntry.CONTENT_URI;
//        Cursor cur = contentResolver.query(NotificationsUri,
//                null, //Columnas a devolver
//                null,       //Condición de la query
//                null,       //Argumentos variables de la query
//                null);
//        if(cur.moveToFirst()){
//            List<String> ids = new ArrayList<String>();
//            do
//            {
//                String id = cur.getString(cur.getColumnIndex(DBContract.NotificationsEntry.COLUMN_ID));
//                if (id != null && !ids.contains(id)) {
//                    Notification shipping = new Notification(cur);
//                    ids.add(shipping.getId());
//                    NotificationsToSend.add(shipping);
//                }
//            } while (cur.moveToNext());
//        }
//        cur.close();
//        return NotificationsToSend;
//    }

//    public void addNotificationToDb(Notification newNotification) {
//        if (newNotification.getStore() != null && newNotification.getDate() != null &&
//                newNotification.getNotification() != null && newNotification.getNotification().isEmpty()) {
//            Vector<ContentValues> cVVector = new Vector<ContentValues>(1);
//            ContentValues values = new ContentValues();
//            values.put(DBContract.NotificationsEntry.COLUMN_ID, newNotification.getId());
//            values.put(DBContract.NotificationsEntry.COLUMN_DATE, dateFormat.format(newNotification.getDate()));
//            values.put(DBContract.NotificationsEntry.COLUMN_STORE, newNotification.getStore().ordinal());
//            values.put(DBContract.NotificationsEntry.COLUMN_SHIPPING, Utils.formatNotification(newNotification.getNotification()));
//            values.put(DBContract.NotificationsEntry.COLUMN_OTHERS, newNotification.getOthers());
//
//            cVVector.add(values);
//            int inserted = 0;
//            // add to database
//            if (cVVector.size() > 0) {
//                ContentValues[] cvArray = new ContentValues[cVVector.size()];
//                cVVector.toArray(cvArray);
//                inserted = activity.getContentResolver().bulkInsert(DBContract.NotificationsEntry.CONTENT_URI, cvArray);
//            }
//            Log.d("NotificationData", cVVector.toString());
//            Log.d("NotificationRepository", "NotificationTask Complete. " + inserted + " Inserted");
//        }
//    }

    public void save(Notification newNotification) {}
    public void update(Notification newNotification) {}
    
}
