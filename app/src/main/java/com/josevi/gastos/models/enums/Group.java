package com.josevi.gastos.models.enums;

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
        return Group.values();
    }

}
