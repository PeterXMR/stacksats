package com.example.stacksats.utils;

public enum CurrencyEnum {
    ARS("ars"),
    CAD("cad"),
    CZK("czk"),
    EUR("eur"),
    NOK("nok"),
    USD("usd");

    public final String label;

    private CurrencyEnum(String label) {
        this.label = label;
    }
}
