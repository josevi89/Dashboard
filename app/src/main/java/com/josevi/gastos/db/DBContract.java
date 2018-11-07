package com.josevi.gastos.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DBContract {
    public static final String CONTENT_AUTHORITY = "com.josevi";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SHIPINGS = "shippings";

    public static final class ShippingsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SHIPINGS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SHIPINGS;

        public static final String TABLE_NAME = "shippings";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_SHIPPING = "shipping";
        public static final String COLUMN_STORE = "store";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_OTHERS = "others";

        public static Uri buildShippingsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
