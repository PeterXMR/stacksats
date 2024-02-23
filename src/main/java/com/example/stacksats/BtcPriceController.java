package com.example.stacksats;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
    public ResponseEntity<List<BtcPriceDto>> getAndSaveHistoricRecords() throws InterruptedException {
        List<BtcPriceDto> btcPriceDtoList = btcPriceService.getHistoricRecords();
        List<BtcPrice> btcPriceList = new ArrayList<>();
        btcPriceList = btcPriceDtoList
                .stream()
                .map(btcPriceDto -> modelMapper.map(btcPriceDto, BtcPrice.class))
                .toList();
        btcPriceService.saveAll(btcPriceList);
        return ResponseEntity.ok().body(btcPriceDtoList);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRecordById(@PathVariable Long id) {
        btcPriceService.deleteRecordById(id);
        return ResponseEntity.ok().body("Record with id + " + id + " deleted successfully");
    }
}
