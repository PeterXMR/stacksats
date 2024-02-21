package com.example.stacksats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BtcPriceDto {
        public int id;
        public Date date;
        public Double price_ars;
        public Double price_cad;
        public Double price_czk;
        public Double price_eur;
        public Double price_nok;
        public Double price_usd;
}
