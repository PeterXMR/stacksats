package com.example.stacksats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BtcPriceRepository extends JpaRepository<BtcPrice, Long> {
}
