package com.example.stacksats.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

public class Constants {

    public static final String BASE_URL = "https://api.coingecko.com/api/v3/coins/bitcoin/history";
    public static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm";
    public static final String SIMPLE_DATE_PATTERN = "dd-MM-yyyy";
    public static final String START_DATE = "04-11-2022 00:00";
    public static final String MARKET_DATA = "market_data";
    public static final String CURRENT_PRICE = "current_price";
    public static final String URI_DATE = "?date=";
    public static final String URI_LOCALIZATION = "&localization=false";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_TIME_PATTERN);
    public static final Integer SATOSHI_PER_BITCOIN = 100000000;
    public static final Integer INVESTMENT_AMOUNT = 8;
    public static final LocalDate FIRST_DATE = LocalDate.parse(START_DATE, FORMATTER);
    public static final List<Currency> CURRENCY_LIST = Arrays.asList(
            Currency.getInstance("ARS"), Currency.getInstance("CAD"), Currency.getInstance("CZK"), Currency.getInstance("EUR"),
            Currency.getInstance("NOK"), Currency.getInstance("USD"));
}
