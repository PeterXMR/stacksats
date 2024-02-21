package com.example.stacksats;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BtcPriceController {

    private final BtcPriceService btcPriceService;

    private final ModelMapper modelMapper;

    public BtcPriceController(BtcPriceService btcPriceService, ModelMapper modelMapper) {
        this.btcPriceService = btcPriceService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/records")
    public Iterable<BtcPrice> findAll() {
        return btcPriceService.findAll();
    }


    @GetMapping("/historic-records")
    public List<BtcPriceDto> getAndSaveHistoricRecords() throws InterruptedException {
        List<BtcPriceDto> btcPriceDtoList = btcPriceService.getAndSaveHistoricRecords();
        List<BtcPrice> btcPriceList = btcPriceDtoList
                .stream()
                .map(btcPriceDto -> modelMapper.map(btcPriceDto, BtcPrice.class))
                .toList();
        btcPriceService.saveAll(btcPriceList);
        return btcPriceDtoList;
    }
}

