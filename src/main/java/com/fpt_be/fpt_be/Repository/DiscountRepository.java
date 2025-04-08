package com.fpt_be.fpt_be.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpt_be.fpt_be.Entity.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
} 