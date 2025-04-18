package com.fpt_be.fpt_be.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fpt_be.fpt_be.Service.PermissionService;
import com.fpt_be.fpt_be.Service.PositionPermissionService;
import com.fpt_be.fpt_be.Entity.Permission;
import com.fpt_be.fpt_be.Entity.PositionPermission;
import com.fpt_be.fpt_be.Dto.PermissionDto;

@RestController
@RequestMapping("/api/admin/quyen")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;
    
    @Autowired
    private PositionPermissionService positionPermissionService;

    @GetMapping
    public ResponseEntity<?> getAllPermissions() {
        try {
            List<Permission> permissions = permissionService.getAllPermissions();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách quyền thành công",
                            "data", permissions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPermissionById(@PathVariable Long id) {
        try {
            Permission permission = permissionService.getPermissionById(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy thông tin quyền thành công",
                            "data", permission));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPermission(@RequestBody PermissionDto permissionDto) {
        try {
            Permission createdPermission = permissionService.createPermission(permissionDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "status", true,
                            "message", "Thêm quyền thành công",
                            "data", createdPermission));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePermission(@PathVariable Long id, @RequestBody PermissionDto permissionDto) {
        try {
            Permission updatedPermission = permissionService.updatePermission(id, permissionDto);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Cập nhật quyền thành công",
                            "data", updatedPermission));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePermission(@PathVariable Long id) {
        try {
            permissionService.deletePermission(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Xóa quyền thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/chuc-vu/{positionId}")
    public ResponseEntity<?> getPermissionsByPosition(@PathVariable Long positionId) {
        try {
            if (positionId == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "status", false,
                                "message", "ID chức vụ không được để trống"));
            }

            List<PositionPermission> positionPermissions = positionPermissionService.getPermissionsByPosition(positionId);
            
            // Chuyển đổi dữ liệu để trả về
            List<Map<String, Object>> formattedPermissions = positionPermissions.stream()
                .map(pp -> {
                    Permission permission = pp.getPermission();
                    return Map.of(
                        "id", pp.getId(),
                        "permission", Map.of(
                            "id", permission.getId(),
                            "maQuyen", permission.getMaQuyen(),
                            "tenQuyen", permission.getTenQuyen(),
                            "tinhTrang", permission.getTinhTrang()
                        ),
                        "tinhTrang", pp.getTinhTrang()
                    );
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách quyền của chức vụ thành công",
                            "data", formattedPermissions));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "status", false,
                            "message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", false,
                            "message", "Lỗi hệ thống: " + e.getMessage()));
        }
    }

    @PostMapping("/chuc-vu/{positionId}/phan-quyen")
    public ResponseEntity<?> assignPermissionsToPosition(
            @PathVariable Long positionId,
            @RequestBody Set<Long> permissionIds) {
        try {
            // Validate input
            if (positionId == null || permissionIds == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "status", false,
                                "message", "ID chức vụ và danh sách quyền không được để trống"));
            }

            positionPermissionService.assignPermissionsToPosition(positionId, permissionIds);
            
            // Kiểm tra xem quyền đã được lưu thành công chưa
            List<PositionPermission> savedPermissions = positionPermissionService.getPermissionsByPosition(positionId);
            if (savedPermissions.size() != permissionIds.size()) {
                throw new RuntimeException("Có lỗi khi lưu quyền vào database");
            }

            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Phân quyền cho chức vụ thành công",
                            "data", savedPermissions));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "status", false,
                            "message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", false,
                            "message", "Lỗi hệ thống khi phân quyền: " + e.getMessage()));
        }
    }

    @DeleteMapping("/chuc-vu/{positionId}/quyen/{permissionId}")
    public ResponseEntity<?> removePermissionFromPosition(
            @PathVariable Long positionId,
            @PathVariable Long permissionId) {
        try {
            positionPermissionService.removePermissionFromPosition(positionId, permissionId);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Xóa quyền khỏi chức vụ thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
} 