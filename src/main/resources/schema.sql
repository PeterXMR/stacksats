DROP TABLE IF EXISTS records;

CREATE TABLE records
(
    id    varchar(255) NOT NULL PRIMARY KEY,
    date  date NOT NULL,
    price_ars DOUBLE PRECISION NOT NULL,
    price_cad DOUBLE PRECISION NOT NULL,
    price_czk DOUBLE PRECISION NOT NULL,
    price_eur DOUBLE PRECISION NOT NULL,
    price_nok DOUBLE PRECISION NOT NULL,
    price_usd DOUBLE PRECISION NOT NULL
);

INSERT INTO records
(id, date, price_ars, price_cad, price_czk, price_eur, price_nok, price_usd)
values (1, CURRENT_DATE, 100000, 48000, 980000, 40400, 11100, 43000);