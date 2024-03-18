package com.example.stacksats;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.stacksats.utils.Constants;
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
                .baseUrl(Constants.BASE_URL)
                .build();
    }

    public Iterable<BtcPrice> findAll() {
        return btcPriceRepository.findAll();
    }

    public List<BtcPriceDto> getHistoricRecords() throws InterruptedException {

        List<BtcPriceDto> btcPriceDtoList = new ArrayList<>();
        LocalDate startDate = Constants.FIRST_DATE;
        LocalDate now = LocalDate.now();
        AtomicInteger i = new AtomicInteger(0);
        AtomicLong monthCount = new AtomicLong(0);
        AtomicBoolean dataLoaded = new AtomicBoolean(false);
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        Runnable getHistoricBtcPrice = () -> {
            for (i.set(0); i.get() < 4; i.incrementAndGet()) {
                if (startDate.plusMonths(monthCount.get()).isBefore(now)) {
                    BtcPriceDto dto = getHistoricBtcPrice(startDate.plusMonths(monthCount.get()));
                    btcPriceDtoList.add(dto);
                    monthCount.incrementAndGet();
                    i.incrementAndGet();
                } else {
                    dataLoaded.set(true);
                    executorService.shutdown();
                    break;
                }
            }
        };

        if (!dataLoaded.get()) {
            executorService.scheduleAtFixedRate(getHistoricBtcPrice, 0, Constants.PERIOD_IN_SECONDS, TimeUnit.SECONDS);
        }

        return btcPriceDtoList;
    }

    private BtcPriceDto getHistoricBtcPrice(LocalDate localDate) {
        String response = getHistoricBtcPriceForDate(localDate);
        return parseJsonData(response, localDate);
    }

    private BtcPriceDto parseJsonData(String response, LocalDate date) {
        JsonNode json;
        BtcPriceDto btcPriceDto;
        try {
            assert response != null;
            InputStream inputStream = new ByteArrayInputStream(response.getBytes());
            json = objectMapper.readValue(inputStream, JsonNode.class);
            btcPriceDto = parseJson(json, date);

        } catch (IOException e) {
            log.error("Error occurred, cannot read JSON data {}", e.getMessage());
            throw new RuntimeException("Error occurred, cannot read JSON data");
        }
        return btcPriceDto;
    }

    private String getHistoricBtcPriceForDate(LocalDate date) {
        String dateUrl = Constants.FORMATTER.format(date.atStartOfDay()).substring(0, 10);
        return restClient.get()
                .uri(Constants.URI_DATE + dateUrl + Constants.URI_LOCALIZATION)
                .retrieve()
                .body(String.class);
    }

    private BtcPriceDto parseJson(JsonNode json, LocalDate date) {
        List<String> currencyNames = Constants.CURRENCY_LIST.stream()
                .map(currency -> currency.getCurrencyCode().toLowerCase())
                .collect(Collectors.toUnmodifiableList());

        BtcPriceDto btcPriceDto = new BtcPriceDto();
        btcPriceDto.date = date;
        Optional.ofNullable(json)
                .map(j -> j.get(Constants.MARKET_DATA))
                .map(j -> j.get(Constants.CURRENT_PRICE).properties())
                .orElseThrow(() -> new IllegalArgumentException("Invalid JSON data"))
                .stream()
                .filter(currency -> currencyNames.contains(currency.getKey()))
                .forEach(currency -> {
                    switch (currency.getKey()) {
                        case "ars" -> btcPriceDto.priceArs = currency.getValue().decimalValue();
                        case "cad" -> btcPriceDto.priceCad = currency.getValue().decimalValue();
                        case "czk" -> btcPriceDto.priceCzk = currency.getValue().decimalValue();
                        case "eur" -> btcPriceDto.priceEur = currency.getValue().decimalValue();
                        case "nok" -> btcPriceDto.priceNok = currency.getValue().decimalValue();
                        case "usd" -> btcPriceDto.priceUsd = currency.getValue().decimalValue();
                    }
                });
        return btcPriceDto;
    }

    @Transactional
    public void saveAll(List<BtcPrice> btcPriceList) {
        btcPriceRepository.saveAll(btcPriceList);
    }

    public void deleteRecordById(Long id) {
        btcPriceRepository.deleteById(id);
    }

    public HashMap<LocalDate, BigDecimal> priceForDateMap() {
        HashMap<LocalDate, BigDecimal> priceForDateMap = new HashMap<>();
        for (BtcPrice btcPrice : findAll()) {
            priceForDateMap.put(btcPrice.getDate(), btcPrice.getPriceUsd());
        }
        return priceForDateMap;
    }

    public Collection<Number> convertToSatsList(HashMap<LocalDate, BigDecimal> priceForDateMap) {
        Collection<Number> satsList = new ArrayList<>();
        int totalSats = 0;
        for (Map.Entry<LocalDate, BigDecimal> entry : priceForDateMap.entrySet()) {
            int stackedSats = Constants.INVESTMENT_AMOUNT * Constants.SATOSHI_PER_BITCOIN / entry.getValue().intValue();
            totalSats += stackedSats;
            satsList.add(totalSats);
        }
        return satsList;
    }
}
