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
        if (positionId == null) {
            throw new RuntimeException("ID chức vụ không được để trống");
        }
        
        // Kiểm tra xem chức vụ có tồn tại không
        if (!positionRepository.existsById(positionId)) {
            throw new RuntimeException("Không tìm thấy chức vụ với id: " + positionId);
        }
        
        List<PositionPermission> permissions = positionPermissionRepository.findByPosition_Id(positionId);
        if (permissions == null) {
            throw new RuntimeException("Lỗi khi lấy danh sách quyền");
        }
        
        return permissions;
    }

    @Transactional
    public void assignPermissionsToPosition(Long positionId, Set<Long> permissionIds) {
        try {
            // Validate input
            if (positionId == null || permissionIds == null) {
                throw new RuntimeException("ID chức vụ và danh sách quyền không được để trống");
            }

            Position position = positionRepository.findById(positionId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy chức vụ với id: " + positionId));

            // Xóa các quyền cũ
            List<PositionPermission> oldPermissions = positionPermissionRepository.findByPosition_Id(positionId);
            if (!oldPermissions.isEmpty()) {
                positionPermissionRepository.deleteAll(oldPermissions);
                positionPermissionRepository.flush(); // Đảm bảo xóa được lưu vào DB
            }

            // Thêm các quyền mới
            List<PositionPermission> newPermissions = permissionIds.stream()
                    .map(permissionId -> {
                        Permission permission = permissionRepository.findById(permissionId)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền với id: " + permissionId));
                        
                        PositionPermission positionPermission = new PositionPermission();
                        positionPermission.setPosition(position);
                        positionPermission.setPermission(permission);
                        positionPermission.setTinhTrang(true);
                        return positionPermission;
                    })
                    .collect(Collectors.toList());

            // Lưu các quyền mới và đảm bảo được lưu vào DB
            positionPermissionRepository.saveAll(newPermissions);
            positionPermissionRepository.flush();
            
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi phân quyền: " + e.getMessage());
        }
    }

    public boolean hasPermission(Long positionId, String maQuyen) {
        List<PositionPermission> permissions = positionPermissionRepository.findByPosition_Id(positionId);
        return permissions.stream()
                .anyMatch(pp -> pp.getPermission().getMaQuyen().equals(maQuyen) && 
                               pp.getTinhTrang() && 
                               pp.getPermission().getTinhTrang());
    }

    public void removePermissionFromPosition(Long positionId, Long permissionId) {
        List<PositionPermission> permissions = positionPermissionRepository.findByPosition_Id(positionId);
        permissions.stream()
                .filter(pp -> pp.getPermission().getId().equals(permissionId))
                .findFirst()
                .ifPresent(pp -> positionPermissionRepository.delete(pp));
    }
} 