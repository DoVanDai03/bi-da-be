package com.fpt_be.fpt_be.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

import com.fpt_be.fpt_be.Entity.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    // Find by discount code
    Optional<Discount> findByMaCode(String maCode);
    
    // Find valid discount by code and date
    Optional<Discount> findByMaCodeAndNgayBatDauLessThanEqualAndNgayKetThucGreaterThanEqual(
            String maCode, LocalDateTime currentDate, LocalDateTime currentDate2);
} 