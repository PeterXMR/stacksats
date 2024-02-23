package com.example.stacksats;

import com.example.stacksats.utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "records")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BtcPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.simple_date_pattern)
    @Column(nullable = false)
    public Date date;
    @Column(nullable = false)
    public Double price_ars;
    @Column(nullable = false)
    public Double price_cad;
    @Column(nullable = false)
    public Double price_czk;
    @Column(nullable = false)
    public Double price_eur;
    @Column(nullable = false)
    public Double price_nok;
    @Column(nullable = false)
    public Double price_usd;

    @Override
    public String toString() {
        return String.format(
                "BtcPrice[id=%x, date='%s', price_ars='%s', price_cad='%s', price_czk='%s' , price_eur='%s' , price_nok='%s' , price_usd='%s']",
                id, date, price_ars, price_cad, price_czk, price_eur, price_nok, price_usd);
    }
}
