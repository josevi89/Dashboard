package com.josevi.gastos.models.enums;

import android.content.Context;

import com.josevi.gastos.R;

public enum Group {

    MEAT, FISH, MILK, BREAD, PREPARED, DRINKS, FROZEN, BREAKFAST, ANIMALS, VEGETABLES, OTHER,
    TOBACCO, PAPER, FILTERS, ROLL_A, ROLL_B;

    public static Group[] getGroupsFromStore(Store store) {
        Group[] mercadonaGroups = {MEAT, FISH, MILK, BREAD, PREPARED, DRINKS, FROZEN, BREAKFAST, ANIMALS, VEGETABLES, OTHER};
        Group[] estancoGroups = {TOBACCO, PAPER, FILTERS};
        switch (store) {
            case MERCADONA:
                return mercadonaGroups;
            case ESTANCO:
                return estancoGroups;
        }
        return Group.vals();
    }

    public static Group[] vals() {
        Group[] groups = new Group[values().length - 2];
        for (int g = 0; g < groups.length; g++)
            groups[g] = values()[g];
        return groups;
    }

    public String title() {
        String[] title = {"Carne", "Pescado", "Lácteos", "Panadería", "Preparados", "Bebidas",
                "Congelados", "Desayuno", "Animales", "Vegetales", "Otros", "Tabaco", "Papel",
                "Filtros", "Roll A", "Roll B"};
        return title[ordinal()];
    }

    public int color(Context context, boolean highContrast) {
        int[] colors = {
                context.getResources().getColor(R.color.green_meat),
                context.getResources().getColor(R.color.green_fish),
                context.getResources().getColor(R.color.green_milk),
                context.getResources().getColor(R.color.green_bread),
                context.getResources().getColor(R.color.green_prepared),
                context.getResources().getColor(R.color.green_drinks),
                context.getResources().getColor(R.color.green_frozen),
                context.getResources().getColor(R.color.green_breakfast),
                context.getResources().getColor(R.color.green_animals),
                context.getResources().getColor(R.color.green_vegetables),
                context.getResources().getColor(R.color.green_other),
                context.getResources().getColor(R.color.brown_tobacco),
                context.getResources().getColor(R.color.brown_paper),
                context.getResources().getColor(R.color.brown_filters),
                context.getResources().getColor(R.color.purle_roll_a),
                context.getResources().getColor(R.color.purle_roll_b)
        };
        int[] highContrastColors = {
                context.getResources().getColor(R.color.brown_meat),
                context.getResources().getColor(R.color.blue_fish),
                context.getResources().getColor(R.color.grey_milk),
                context.getResources().getColor(R.color.brown_bread),
                context.getResources().getColor(R.color.purple_prepared),
                context.getResources().getColor(R.color.orange_drinks),
                context.getResources().getColor(R.color.blue_frozen),
                context.getResources().getColor(R.color.red_breakfast),
                context.getResources().getColor(R.color.black_animals),
                context.getResources().getColor(R.color.green_vegetables_hc),
                context.getResources().getColor(R.color.yellow_other),
                context.getResources().getColor(R.color.blue_tobacco),
                context.getResources().getColor(R.color.black_paper),
                context.getResources().getColor(R.color.grey_filters),
                context.getResources().getColor(R.color.green_roll_a),
                context.getResources().getColor(R.color.brown_roll_b)
        };
        return highContrast ? highContrastColors[ordinal()] : colors[ordinal()];
    }

}
