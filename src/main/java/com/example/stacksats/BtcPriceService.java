package com.example.stacksats;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.stacksats.utils.Constants;
import com.example.stacksats.utils.CurrencyEnum;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;;

@Slf4j
@Service
public class BtcPriceService {

    private final ObjectMapper objectMapper;
    private final RestClient restClient;
    private final BtcPriceRepository btcPriceRepository;

    public BtcPriceService(ObjectMapper objectMapper, BtcPriceRepository btcPriceRepository) {
        this.objectMapper = objectMapper;
        this.btcPriceRepository = btcPriceRepository;
        restClient = RestClient.builder()
                .baseUrl(Constants.base_url)
                .build();
    }

    public Iterable<BtcPrice> findAll() {
        return btcPriceRepository.findAll();
    }

    public List<BtcPriceDto> getHistoricRecords() throws InterruptedException {

        List<BtcPriceDto> btcPriceDtoList = new ArrayList<>();
        LocalDateTime first_date = getFirstDate();
        LocalDateTime now = LocalDateTime.now();
        int i = 0;
        while (first_date.isBefore(now)) {
            i++;
            BtcPriceDto dto;
            dto = getHistoricRecord(Constants.formatter.format(first_date).substring(0, 10));
            btcPriceDtoList.add(dto);
            first_date = first_date.plusMonths(1);
            if (i > 2) {
                TimeUnit.MINUTES.sleep(1);
                TimeUnit.SECONDS.sleep(5);
                i = 0;
            }
        }

        return btcPriceDtoList;
    }

    private BtcPriceDto getHistoricRecord(String record_for_date) {
        String data = getHistoricRecordsForDate(record_for_date);
        Date date = formatDate(record_for_date);
        return parseJsonData(data, date);
    }

    private Date formatDate(String record_for_date) {
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.simple_date_pattern, Locale.getDefault());
        Date date = new Date();
        try {
            date = formatter.parse(record_for_date);
        } catch (ParseException e) {
            log.error("Error occurred, cannot parse date {}", e.getMessage());
        }
        return date;
    }

    private BtcPriceDto parseJsonData(String data, Date date) {
        JsonNode json;
        BtcPriceDto dto;
        try {
            assert data != null;
            InputStream inputStream = new ByteArrayInputStream(data.getBytes());
            json = objectMapper.readValue(inputStream, JsonNode.class);
            dto = parseJson(json, date);

        } catch (IOException e) {
            log.error("Error occurred, cannot read JSON data {}", e.getMessage());
            throw new RuntimeException("Error occurred, cannot read JSON data");
        }
        return dto;
    }

    private String getHistoricRecordsForDate(String date) {
        return restClient.get()
                .uri(Constants.uri_date + date + "&localization=false")
                .retrieve()
                .body(String.class);
    }

    private BtcPriceDto parseJson(JsonNode json, Date date) {
        List<String> currencyNames = Arrays.stream(CurrencyEnum.values())
                .map(currency -> currency.label)
                .collect(Collectors.toUnmodifiableList());

        BtcPriceDto dto = new BtcPriceDto();
        dto.date = date;
        Optional.ofNullable(json)
                .map(j -> j.get(Constants.market_data))
                .map(j -> j.get(Constants.current_price).properties())
                .orElseThrow(() -> new IllegalArgumentException("Invalid JSON data"))
                .stream()
                .filter(currency -> currencyNames.contains(currency.getKey()))
                .forEach(currency -> {
                    switch (currency.getKey()) {
                        case "ars" -> dto.price_ars = formatTwoDecimals(currency.getValue().doubleValue());
                        case "cad" -> dto.price_cad = formatTwoDecimals(currency.getValue().doubleValue());
                        case "czk" -> dto.price_czk = formatTwoDecimals(currency.getValue().doubleValue());
                        case "eur" -> dto.price_eur = formatTwoDecimals(currency.getValue().doubleValue());
                        case "nok" -> dto.price_nok = formatTwoDecimals(currency.getValue().doubleValue());
                        case "usd" -> dto.price_usd = formatTwoDecimals(currency.getValue().doubleValue());
                    }
                });
        return dto;
    }

    private Double formatTwoDecimals(double value) {
        return Double.parseDouble(String.format("%.2f", value));
    }

    @Transactional
    public void saveAll(List<BtcPrice> btcPriceList) {
        btcPriceRepository.saveAll(btcPriceList);
    }

    public void deleteRecordById(Long id) {
        btcPriceRepository.deleteById(id);
    }

    public LocalDateTime getFirstDate() {
        return LocalDateTime.parse(Constants.start_date, Constants.formatter);
    }

    public HashMap<Date, Double> datesMap() {
        HashMap<Date, Double> map = new HashMap<>();
        for (BtcPrice btcPrice : findAll()) {
            map.put(btcPrice.date, btcPrice.getPrice_usd());
        }
        return map;
    }

    public Collection<Number> convertToActualList(HashMap<Date, Double> datesMap) {
        Collection<Number> actualList = new ArrayList<>();
        int bitcoin_total = 0;
        for (Map.Entry<Date, Double> entry : datesMap.entrySet()) {
            int stackedSats = Constants.investment_amount * Constants.satoshi_per_bitcoin / entry.getValue().intValue();
            bitcoin_total += stackedSats;
            actualList.add(bitcoin_total);
        }
        return actualList;
    }
}
