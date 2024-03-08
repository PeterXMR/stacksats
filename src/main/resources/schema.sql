DROP TABLE IF EXISTS btc_price;

CREATE TABLE btc_price
(
    id  SERIAL PRIMARY KEY,
    date  date NOT NULL,
    price_ars DECIMAL(15, 2) NOT NULL,
    price_cad DECIMAL(15, 2) NOT NULL,
    price_czk DECIMAL(15, 2) NOT NULL,
    price_eur DECIMAL(15, 2) NOT NULL,
    price_nok DECIMAL(15, 2) NOT NULL,
    price_usd DECIMAL(15, 2) NOT NULL
);
