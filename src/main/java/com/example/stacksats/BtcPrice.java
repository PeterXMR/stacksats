package com.example.stacksats;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.stacksats.utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "btc_price")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BtcPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.SIMPLE_DATE_PATTERN)
    private LocalDate date;
    private BigDecimal priceArs;
    private BigDecimal priceCad;
    private BigDecimal priceCzk;
    private BigDecimal priceEur;
    private BigDecimal priceNok;
    private BigDecimal priceUsd;
}
