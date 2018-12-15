package com.josevi.gastos.cards;

import java.util.Objects;

public class Legend {

    private int color;
    private String label;

    public Legend(int color, String label) {
        this.color = color;
        this.label = label;
    }

    public int getColor() {
        return color;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Legend legend = (Legend) o;
        return color == legend.color &&
                Objects.equals(label, legend.label);
    }

    @Override
    public int hashCode() {

        return Objects.hash(color, label);
    }
}


