package com.josevi.gastos.cards;

public class CoreCard {

    private int layoutResource;
    private int cardType;

    public CoreCard(int layoutResource, int cardType) {
        this.layoutResource = layoutResource;
        this.cardType = cardType;
    }

    public int getLayoutResource() {
        return layoutResource;
    }

    public void setLayoutResource(int layoutResource) {
        this.layoutResource = layoutResource;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }
}
