package com.qeema.aps.card.domain;

public enum CardType {
    VISA("VISA"), MASTERCARD("MASTERCARD"), AMEX("AMEX"), MADA("MADA"), MEEZA("MEEZA");

    private String cardType;

    CardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardType() {
        return cardType;
    }
}
