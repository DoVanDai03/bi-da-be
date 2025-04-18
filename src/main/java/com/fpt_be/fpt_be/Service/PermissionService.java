package com.fpt_be.fpt_be.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.fpt_be.fpt_be.Repository.PermissionRepository;
import com.fpt_be.fpt_be.Entity.Permission;
import com.fpt_be.fpt_be.Dto.PermissionDto;

@Service
public class PermissionService {
    
    @Autowired
    private PermissionRepository permissionRepository;

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền với id: " + id));
    }

    public Permission createPermission(PermissionDto permissionDto) {
        if (permissionRepository.existsByMaQuyen(permissionDto.getMaQuyen())) {
            throw new RuntimeException("Mã quyền đã tồn tại");
        }

        Permission permission = new Permission();
        permission.setTenQuyen(permissionDto.getTenQuyen());
        permission.setMaQuyen(permissionDto.getMaQuyen());
        permission.setMoTa(permissionDto.getMoTa());
        permission.setTinhTrang(true);

        return permissionRepository.save(permission);
    }

    public Permission updatePermission(Long id, PermissionDto permissionDto) {
        Permission permission = getPermissionById(id);
        
        if (!permission.getMaQuyen().equals(permissionDto.getMaQuyen()) && 
            permissionRepository.existsByMaQuyen(permissionDto.getMaQuyen())) {
            throw new RuntimeException("Mã quyền đã tồn tại");
        }

        permission.setTenQuyen(permissionDto.getTenQuyen());
        permission.setMaQuyen(permissionDto.getMaQuyen());
        permission.setMoTa(permissionDto.getMoTa());
        if (permissionDto.getTinhTrang() != null) {
            permission.setTinhTrang(permissionDto.getTinhTrang());
        }

        return permissionRepository.save(permission);
    }

    public void deletePermission(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy quyền với id: " + id);
        }
        permissionRepository.deleteById(id);
    }
} 