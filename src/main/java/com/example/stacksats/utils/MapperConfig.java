package com.example.stacksats.utils;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MapperConfig {

    @Bean
    public static ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
