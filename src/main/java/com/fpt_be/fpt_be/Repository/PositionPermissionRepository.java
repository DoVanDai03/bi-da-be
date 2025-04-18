package com.fpt_be.fpt_be.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.fpt_be.fpt_be.Entity.PositionPermission;
import com.fpt_be.fpt_be.Entity.Position;

@Repository
public interface PositionPermissionRepository extends JpaRepository<PositionPermission, Long> {
    List<PositionPermission> findByPosition(Position position);
    List<PositionPermission> findByPosition_Id(Long positionId);
    void deleteByPosition_Id(Long positionId);
} 