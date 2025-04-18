package com.fpt_be.fpt_be.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpt_be.fpt_be.Entity.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    Position findFirstByTenChucVu(String tenChucVu);
} 