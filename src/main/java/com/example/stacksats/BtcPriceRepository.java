package com.example.stacksats;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BtcPriceRepository extends ListCrudRepository<BtcPrice, Long> {
}
