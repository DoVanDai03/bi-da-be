package com.fpt_be.fpt_be.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpt_be.fpt_be.Dto.AdminDto;
import com.fpt_be.fpt_be.Entity.Admin;
import com.fpt_be.fpt_be.Entity.Permission;
import com.fpt_be.fpt_be.Entity.PositionPermission;
import com.fpt_be.fpt_be.Security.JwtTokenProvider;
import com.fpt_be.fpt_be.Service.AdminService;
import com.fpt_be.fpt_be.Service.PositionPermissionService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    private PositionPermissionService positionPermissionService;
    
    @PostMapping("/dang-nhap")
    public ResponseEntity<?> dangNhapAdmin(@RequestBody AdminDto request) {
        try {
            if (request.getEmail() == null || request.getPassword() == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", "Email và mật khẩu không được để trống"));
            }
            
            Admin admin = adminService.dangNhapAdmin(request.getEmail(), request.getPassword());
            String token = tokenProvider.generateAdminToken(admin.getId(), admin.getEmail());
            
            List<PositionPermission> permissions = positionPermissionService.getPermissionsByPosition(admin.getPosition().getId());
            List<String> quyenHan = permissions.stream()
                .map(pp -> pp.getPermission().getMaQuyen())
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "status", true, 
                "message", "Đăng nhập thành công!", 
                "token", token,
                "admin", Map.of(
                    "id", admin.getId(),
                    "email", admin.getEmail(),
                    "hoVaTen", admin.getHoVaTen(),
                    "chucVu", admin.getPosition().getTenChucVu(),
                    "quyenHan", quyenHan
                )
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", false, "message", "Có lỗi xảy ra trong quá trình đăng nhập"));
        }
    }
    
    @GetMapping("/kiem-tra-token")
    public ResponseEntity<?> kiemTraToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.ok(Map.of("status", false, "message", "Token không hợp lệ"));
            }

            String token = authHeader.substring(7);
            if (tokenProvider.validateToken(token)) {
                if (!tokenProvider.isAdminToken(token)) {
                    return ResponseEntity.ok(Map.of("status", false, "message", "Token không có quyền admin"));
                }
                
                Long adminId = tokenProvider.getUserIdFromToken(token);
                Admin admin = adminService.getAdminById(adminId);
                
                List<PositionPermission> permissions = positionPermissionService.getPermissionsByPosition(admin.getPosition().getId());
                List<String> quyenHan = permissions.stream()
                    .map(pp -> pp.getPermission().getMaQuyen())
                    .collect(Collectors.toList());
                
                return ResponseEntity.ok(Map.of(
                    "status", true, 
                    "message", "Token hợp lệ", 
                    "admin", Map.of(
                        "id", admin.getId(),
                        "email", admin.getEmail(),
                        "hoVaTen", admin.getHoVaTen(),
                        "chucVu", admin.getPosition().getTenChucVu(),
                        "quyenHan", quyenHan
                    )
                ));
            } else {
                return ResponseEntity.ok(Map.of("status", false, "message", "Token không hợp lệ hoặc đã hết hạn"));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("status", false, "message", "Token không hợp lệ hoặc đã hết hạn"));
        }
    }
    
    // Management of admins - these should only be accessible to super admin
    @GetMapping
    public ResponseEntity<?> getAllAdmins() {
        try {
            List<AdminDto> admins = adminService.getAllAdmins();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách quản trị viên thành công",
                            "data", admins));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable Long id) {
        try {
            Admin admin = adminService.getAdminById(id);
            AdminDto adminDto = adminService.convertToDto(admin);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy thông tin quản trị viên thành công",
                            "data", adminDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/them-moi")
    public ResponseEntity<?> createAdmin(@RequestBody AdminDto adminDto) {
        try {
            Admin createdAdmin = adminService.createAdmin(adminDto);
            AdminDto createdAdminDto = adminService.convertToDto(createdAdmin);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "status", true,
                            "message", "Thêm quản trị viên thành công",
                            "data", createdAdminDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long id, @RequestBody AdminDto adminDto) {
        try {
            Admin updatedAdmin = adminService.updateAdmin(id, adminDto);
            AdminDto updatedAdminDto = adminService.convertToDto(updatedAdmin);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Cập nhật thông tin quản trị viên thành công",
                            "data", updatedAdminDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Xóa quản trị viên thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
} 