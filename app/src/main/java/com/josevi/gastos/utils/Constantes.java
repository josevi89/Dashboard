package com.josevi.gastos.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constantes {

    //DateFormat
    public static SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static SimpleDateFormat dayDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat timeDateFormat = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat prettyDayDateFormat = new SimpleDateFormat("dd MMM yyyy");
    public static SimpleDateFormat monthYearFormatter = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

    //CoreActivity
    public static final int CORE_CARD_NOTIFICATIONS_NUMBER = 0;
    public static final int CORE_CARD_GASTOS_NUMBER = 1;

    //ShippingsActivity
    public static String SHIPPING_FRAGMENT_SHIPPING = "SHIPPING_FRAGMENT_SHIPPING";
    public static String SHIPPING_FRAGMENT_TAG = "SHIPPING_FRAGMENT_TAG";
    public static String SHIPPING_EDIT = "SHIPPING_EDIT";

    //NotificationsActivity
    public static String NOTIFICATION_EDIT = "NOTIFICATION_EDIT";

}
