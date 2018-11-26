package com.josevi.gastos.models;

import java.io.Serializable;

public class ShippingQty implements Serializable {

    public int qty;
    public double prize;

    public ShippingQty(int qty, double prize) {
        this.qty = qty;
        this.prize = prize;
    }
}
