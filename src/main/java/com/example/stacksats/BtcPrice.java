package com.example.stacksats;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "records")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BtcPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
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
