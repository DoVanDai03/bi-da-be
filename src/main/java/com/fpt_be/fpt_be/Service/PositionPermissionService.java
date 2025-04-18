package com.fpt_be.fpt_be.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fpt_be.fpt_be.Repository.PositionPermissionRepository;
import com.fpt_be.fpt_be.Repository.PositionRepository;
import com.fpt_be.fpt_be.Repository.PermissionRepository;
import com.fpt_be.fpt_be.Entity.PositionPermission;
import com.fpt_be.fpt_be.Entity.Position;
import com.fpt_be.fpt_be.Entity.Permission;
import com.fpt_be.fpt_be.Dto.PositionPermissionDto;

@Service
public class PositionPermissionService {
    
    @Autowired
    private PositionPermissionRepository positionPermissionRepository;
    
    @Autowired
    private PositionRepository positionRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;

    public List<PositionPermission> getPermissionsByPosition(Long positionId) {
        return positionPermissionRepository.findByPositionId(positionId);
    }

    @Transactional
    public void assignPermissionsToPosition(Long positionId, Set<Long> permissionIds) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chức vụ với id: " + positionId));

        // Xóa các quyền cũ
        positionPermissionRepository.deleteByPositionId(positionId);

        // Thêm các quyền mới
        Set<PositionPermission> newPermissions = permissionIds.stream()
                .map(permissionId -> {
                    Permission permission = permissionRepository.findById(permissionId)
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền với id: " + permissionId));
                    
                    PositionPermission positionPermission = new PositionPermission();
                    positionPermission.setPosition(position);
                    positionPermission.setPermission(permission);
                    positionPermission.setTinhTrang(true);
                    return positionPermission;
                })
                .collect(Collectors.toSet());

        positionPermissionRepository.saveAll(newPermissions);
    }

    public boolean hasPermission(Long positionId, String maQuyen) {
        List<PositionPermission> permissions = positionPermissionRepository.findByPositionId(positionId);
        return permissions.stream()
                .anyMatch(pp -> pp.getPermission().getMaQuyen().equals(maQuyen) && 
                               pp.getTinhTrang() && 
                               pp.getPermission().getTinhTrang());
    }

    public void removePermissionFromPosition(Long positionId, Long permissionId) {
        List<PositionPermission> permissions = positionPermissionRepository.findByPositionId(positionId);
        permissions.stream()
                .filter(pp -> pp.getPermission().getId().equals(permissionId))
                .findFirst()
                .ifPresent(pp -> positionPermissionRepository.delete(pp));
    }
} 