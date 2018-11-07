package com.josevi.gastos.models;

import com.josevi.gastos.models.enums.Group;
import com.josevi.gastos.models.enums.Store;

public class Product {

    String name;
    String code;
    double prize;
    Store store;
    Group group;

    public Product(String name, String code, double prize, Store store, Group group) {
        this.name = name;
        this.code = code;
        this.prize = prize;
        this.store = store;
        this.group = group;
    }

    public Product(String name, String code, double prize) {
        this.name = name;
        this.code = code;
        this.prize = prize;
        switch (code.substring(0, 1)) {
            case "M":
                store = Store.MERCADONA;
                switch (code.substring(1, 3)) {
                    case "ME":
                        group = Group.MEAT;
                        break;
                    case "FI":
                        group = Group.FISH;
                        break;
                    case "BR":
                        group = Group.BREAD;
                        break;
                    case "PR":
                        group = Group.PREPARED;
                        break;
                    case "DR":
                        group = Group.DRINKS;
                        break;
                    case "FR":
                        group = Group.FROZEN;
                        break;
                    case "BF":
                        group = Group.BREAKFAST;
                        break;
                    case "AN":
                        group = Group.ANIMALS;
                        break;
                    case "VE":
                        group = Group.VEGETABLES;
                        break;
                    case "OT":
                        group = Group.OTHER;
                        break;
                    default:
                        group = null;
                }
                break;
            case "E":
                store = Store.ESTANCO;
                break;
            default:
                store = null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPrize() {
        return prize;
    }

    public void setPrize(double prize) {
        this.prize = prize;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
