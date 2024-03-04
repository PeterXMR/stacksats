package com.example.stacksats.utils;

import java.time.format.DateTimeFormatter;

public class Constants {

    public static final String base_url = "https://api.coingecko.com/api/v3/coins/bitcoin/history";
    public static final String date_time_pattern = "dd-MM-yyyy HH:mm";
    public static final String simple_date_pattern = "dd-MM-yyyy";
    public static final String start_date = "04-11-2022 00:00";
    public static final String market_data = "market_data";
    public static final String current_price = "current_price";
    public static final String uri_date = "?date=";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.date_time_pattern);
    public static final Integer satoshi_per_bitcoin = 100000000;
    public static final Integer investment_amount = 8;

}
