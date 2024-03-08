package com.example.stacksats;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BtcPriceDto {
        public int id;
        public LocalDate date;
        public BigDecimal priceArs;
        public BigDecimal priceCad;
        public BigDecimal priceCzk;
        public BigDecimal priceEur;
        public BigDecimal priceNok;
        public BigDecimal priceUsd;
}
