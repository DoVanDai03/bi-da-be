package com.fpt_be.fpt_be.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpt_be.fpt_be.Dto.AdminDto;
import com.fpt_be.fpt_be.Entity.Admin;
import com.fpt_be.fpt_be.Security.JwtTokenProvider;
import com.fpt_be.fpt_be.Service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @PostMapping("/dang-nhap")
    public ResponseEntity<?> dangNhapAdmin(@RequestBody AdminDto request) {
        try {
            if (request.getEmail() == null || request.getPassword() == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", "Email và mật khẩu không được để trống"));
            }
            
            Admin admin = adminService.dangNhapAdmin(request.getEmail(), request.getPassword());
            String token = tokenProvider.generateAdminToken(admin.getId(), admin.getEmail());
            
            return ResponseEntity.ok(Map.of(
                "status", true, 
                "message", "Đăng nhập thành công!", 
                "token", token,
                "admin", Map.of(
                    "id", admin.getId(),
                    "email", admin.getEmail(),
                    "hoVaTen", admin.getHoVaTen(),
                    "chucVu", admin.getChucVu(),
                    "quyenHan", admin.getQuyenHan()
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
                return ResponseEntity.ok(Map.of(
                    "status", true, 
                    "message", "Token hợp lệ", 
                    "admin", Map.of(
                        "id", admin.getId(),
                        "email", admin.getEmail(),
                        "hoVaTen", admin.getHoVaTen(),
                        "chucVu", admin.getChucVu(),
                        "quyenHan", admin.getQuyenHan()
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
            List<Admin> admins = adminService.getAllAdmins();
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
    
} 