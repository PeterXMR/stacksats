package com.example.stacksats;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BtcPriceDtoConverter {

    @Autowired
    public ModelMapper modelMapper;

    public BtcPrice convertToEntity(BtcPriceDto dto) {
        return modelMapper.map(dto, BtcPrice.class);
    }
}
