package com.vadimaid.aeon.repository;

import com.vadimaid.aeon.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findByCode(String code);
}
