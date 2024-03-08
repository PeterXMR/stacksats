# Stacksats

This app is rewrite of https://github.com/PeterXMR/twitterFeeStacker

## Requirements

[Java 21](https://www.oracle.com/java/technologies/downloads/)

[Docker](https://www.docker.com/) running locally for PostgresDB container


## Running Stacksats

Call: http://localhost:8080/historic-records to load data. Request processing could take few minutes,
as its call free API which are limited to requests per minute. It will return JSON and save data to DB.
After that reload page http://localhost:8080/ where you could see chart filled with data.

<img width="1318" alt="Screenshot 2024-03-05" src="https://github.com/PeterXMR/stacksats/assets/42377209/b2e1888c-91d2-4736-a393-0c239b254c11">
