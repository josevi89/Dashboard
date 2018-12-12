package com.josevi.gastos.repositories;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.util.Pair;
import android.util.Log;

import com.josevi.gastos.db.DBContract;
import com.josevi.gastos.models.Shipping;
import com.josevi.gastos.models.ShippingQty;
import com.josevi.gastos.models.enums.Store;
import com.josevi.gastos.utils.Utils;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static com.josevi.gastos.utils.Constantes.dateFormat;
import static com.josevi.gastos.utils.Constantes.dayDateFormat;
import static com.josevi.gastos.utils.Constantes.shortDateFormat;

public class ShippingRepository {

    Map<Integer, Map<Integer, Map<Integer, List<Shipping>>>> shippingsMap;

    private ContentResolver contentResolver;
    private Activity activity;

    public ShippingRepository(Activity activity) {
        this.activity = activity;
        this.contentResolver = activity.getContentResolver();
    }

    public ShippingRepository() {
        Map<Integer, Map<Integer, Map<Integer, List<Shipping>>>> shippingsMap =
                new HashMap<Integer, Map<Integer, Map<Integer,List<Shipping>>>>();

        Map<String, ShippingQty> newShippingMap201810272114 = new HashMap<String, ShippingQty>();
        newShippingMap201810272114.put("MDR2", new ShippingQty(2, 0.5d));
        newShippingMap201810272114.put("MFR0", new ShippingQty(1, 2d));
        newShippingMap201810272114.put("MBF0", new ShippingQty(1, 1.2d));
        Shipping newShipping201810272114 = new Shipping(newShippingMap201810272114, Store.MERCADONA, 0.0);
        try {
            newShipping201810272114.setDate(shortDateFormat.parse("201810272114"));
        } catch (ParseException pe) {}
        addToMap("27/10/2018", newShipping201810272114, shippingsMap);

        Map<String, ShippingQty> newShippingMap201808252114 = new HashMap<String, ShippingQty>();
        newShippingMap201808252114.put("MDR2", new ShippingQty(2, 0.5d));
        newShippingMap201808252114.put("MFR0", new ShippingQty(3, 2d));
        newShippingMap201808252114.put("MBF0", new ShippingQty(1, 1.2d));
        Shipping newShipping201808252114 = new Shipping(newShippingMap201808252114, Store.MERCADONA, 0.0);
        try {
            newShipping201808252114.setDate(shortDateFormat.parse("201808252114"));
        } catch (ParseException pe) {}
        addToMap("25/08/2018", newShipping201808252114, shippingsMap);

        this.shippingsMap = Collections.unmodifiableMap(shippingsMap);
    }

    public void addToMap(String dayFormatted, Shipping shipping,
                         Map<Integer, Map<Integer, Map<Integer, List<Shipping>>>> shippingsMap) {
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

    public void addToMapSafe(Integer year, Integer month, Integer day, Shipping shipping,
                             Map<Integer, Map<Integer, Map<Integer, List<Shipping>>>> shippingsMap) {
        if (!shippingsMap.containsKey(year))
            shippingsMap.put(year, new HashMap<Integer, Map<Integer, List<Shipping>>>());
        if (!shippingsMap.get(year).containsKey(month))
            shippingsMap.get(year).put(month, new HashMap<Integer, List<Shipping>>());
        if (!shippingsMap.get(year).get(month).containsKey(day))
            shippingsMap.get(year).get(month).put(day, new ArrayList<Shipping>());
        shippingsMap.get(year).get(month).get(day).add(shipping);
    }

    public List<Shipping> getShippingsInMonth(Calendar date) {
        String[] dateSplitted = dayDateFormat.format(date.getTime()).split("/");
        Integer month = Integer.parseInt(dateSplitted[1]),
                year = Integer.parseInt(dateSplitted[2]);
        List<Shipping> shippingsToReturn = new ArrayList<Shipping>();
        for (int d = 1; d <= 31; d++)
            try {
                shippingsToReturn.addAll(shippingsMap.get(year).get(month).get(d));
            }
            catch (Exception e){}
        return shippingsToReturn;
    }

    public List<Shipping> getShippingsInDay(Calendar date) {
        String[] dateSplitted = dayDateFormat.format(date.getTime()).split("/");
        Integer day = Integer.parseInt(dateSplitted[0]),
                month = Integer.parseInt(dateSplitted[1]),
                year = Integer.parseInt(dateSplitted[2]);
        try {
            return shippingsMap.get(year).get(month).get(day);
        }
        catch (Exception e){}
        return new ArrayList<Shipping>();
    }

    public List<CalendarDay> getShippingDatesInMonth(Calendar date) {
        Integer year = date.get(Calendar.YEAR),
                month = date.get(Calendar.MONTH) + 1;
        List<CalendarDay> datesToReturn = new ArrayList<CalendarDay>();
        for (int d = 1; d < 31; d++) {
            try {
                if (!shippingsMap.get(year).get(month).get(d).isEmpty()) {
                    Calendar day = Calendar.getInstance();
                    day.set(year, month - 1, d, 0, 0, 0);
                    datesToReturn.add(new CalendarDay(day));
                }
            } catch (Exception e) {}
        }
        return datesToReturn;
    }

    public List<List<Shipping>> getShippingsInMonthGroupped(Calendar date) {
        String[] dateSplitted = dayDateFormat.format(date.getTime()).split("/");
        Integer month = Integer.parseInt(dateSplitted[1]),
                year = Integer.parseInt(dateSplitted[2]);
        if (shippingsMap.containsKey(year) && shippingsMap.get(year).containsKey(month)) {
            int monthDays = 31;
            switch (month) {
                case 2:
                    monthDays = 28;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    monthDays = 30;
            }
            List<List<Shipping>> shippings = new ArrayList<List<Shipping>>();
            for (int d = 0; d < monthDays; d++)
                if (shippingsMap.get(year).get(month).containsKey(new Integer(d)))
                    shippings.add(shippingsMap.get(year).get(month).get(new Integer(d)));
                else
                    shippings.add(new ArrayList<Shipping>());
            return shippings;
        }
        else
            return new ArrayList<List<Shipping>>();
    }

    public Map<Store, List<Shipping>> getShippingsInMonthMappedByStore(Calendar date) {
        String[] dateSplitted = dayDateFormat.format(date.getTime()).split("/");
        Integer month = Integer.parseInt(dateSplitted[1]),
                year = Integer.parseInt(dateSplitted[2]);
        Map<Store, List<Shipping>> shippingsMapped = new HashMap<Store, List<Shipping>>();
        if (shippingsMap.containsKey(year) && shippingsMap.get(year).containsKey(month)) {
            int monthDays = 31;
            switch (month) {
                case 2:
                    monthDays = 28;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    monthDays = 30;
            }
            for (Store store: Store.values())
                shippingsMapped.put(store, new ArrayList<Shipping>());
            for (int d = 0; d < monthDays; d++)
                if (shippingsMap.get(year).get(month).containsKey(new Integer(d)))
                    for (Shipping shipping: shippingsMap.get(year).get(month).get(d))
                        shippingsMapped.get(shipping.getStore()).add(shipping);
        }
        return shippingsMapped;
    }

    public List<List<List<Shipping>>> getShippingsInThreeMonths(Calendar date) {
        List<List<List<Shipping>>> monthShippings = new ArrayList<List<List<Shipping>>>();
        monthShippings.add(getShippingsInMonthGroupped(date));
        date.add(Calendar.MONTH, -1);
        monthShippings.add(getShippingsInMonthGroupped(date));
        date.add(Calendar.MONTH, -1);
        monthShippings.add(getShippingsInMonthGroupped(date));
        return monthShippings;
    }

    public List<List<List<Shipping>>> getShippingsInThreeMonths2(Calendar date) {
        List<List<List<Shipping>>> monthShippings = new ArrayList<List<List<Shipping>>>();
        for (int m = 0; m < 3; m++) {
            monthShippings.add(new ArrayList<List<Shipping>>());
            for (int d = 0; d < 30; d++) {
                monthShippings.get(m).add(new ArrayList<Shipping>());
                if (m == 0 && d == 27)
                    monthShippings.get(m).get(d).addAll(getAllShippings());
            }
        }
        return monthShippings;
    }

    public List<Shipping> getAllShippings() {
        List<Shipping> ShippingsToSend = new ArrayList<Shipping>();

        Uri ShippingsUri = DBContract.ShippingsEntry.CONTENT_URI;
        Cursor cur = contentResolver.query(ShippingsUri,
                null, //Columnas a devolver
                null,       //Condición de la query
                null,       //Argumentos variables de la query
                null);
        if(cur.moveToFirst()){
            List<String> ids = new ArrayList<String>();
            do
            {
                String id = cur.getString(cur.getColumnIndex(DBContract.ShippingsEntry.COLUMN_ID));
                if (id != null && !ids.contains(id)) {
                    Shipping shipping = new Shipping(cur);
                    ids.add(shipping.getId());
                    ShippingsToSend.add(shipping);
                }
            } while (cur.moveToNext());
        }
        cur.close();
        return ShippingsToSend;
    }

    public void addShippingToDb(Shipping newShipping) {
        if (newShipping.getStore() != null && newShipping.getDate() != null &&
                newShipping.isEmpty()) {
            Vector<ContentValues> cVVector = new Vector<ContentValues>(1);
            ContentValues values = new ContentValues();
            values.put(DBContract.ShippingsEntry.COLUMN_ID, newShipping.getId());
            values.put(DBContract.ShippingsEntry.COLUMN_DATE, dateFormat.format(newShipping.getDate()));
            values.put(DBContract.ShippingsEntry.COLUMN_STORE, newShipping.getStore().ordinal());
            values.put(DBContract.ShippingsEntry.COLUMN_SHIPPING, newShipping.getShippingFormated());
            values.put(DBContract.ShippingsEntry.COLUMN_OTHERS, newShipping.getOthers());

            cVVector.add(values);
            int inserted = 0;
            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = activity.getContentResolver().bulkInsert(DBContract.ShippingsEntry.CONTENT_URI, cvArray);
            }
            Log.d("ShippingData", cVVector.toString());
            Log.d("ShippingRepository", "ShippingTask Complete. " + inserted + " Inserted");
        }
    }
}
