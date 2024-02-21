package com.example.stacksats;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BtcPriceRepository extends CrudRepository<BtcPrice, Long> {
}
