package com.fpt_be.fpt_be.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpt_be.fpt_be.Entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
} 