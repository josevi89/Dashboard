package com.josevi.gastos.models.enums;

import android.content.Context;

import com.josevi.gastos.R;

public enum Store {
    MERCADONA, ESTANCO, ROLL;

    public static Store[] vals() {
        Store[] stores = new Store[values().length - 1];
        for (int g = 0; g < stores.length; g++)
            stores[g] = values()[g];
        return stores;
    }

    public String title() {
        String[] title = {"Mercadona", "Estanco", "Roll"};
        return title[ordinal()];
    }

    public int color(Context context) {
        int[] colors = {
                context.getResources().getColor(R.color.green_mercadona),
                context.getResources().getColor(R.color.brown_estanco),
                context.getResources().getColor(R.color.purple_roll)};
        return colors[ordinal()];
    }
}
