package com.fpt_be.fpt_be.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import com.fpt_be.fpt_be.Entity.PositionPermission;
import com.fpt_be.fpt_be.Entity.Position;

@Repository
public interface PositionPermissionRepository extends JpaRepository<PositionPermission, Long> {
    List<PositionPermission> findByPosition(Position position);
    List<PositionPermission> findByPosition_Id(Long positionId);
    void deleteByPosition_Id(Long positionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PositionPermission pp WHERE pp.permission.id = :permissionId")
    void deleteByPermissionId(@Param("permissionId") Long permissionId);
} 