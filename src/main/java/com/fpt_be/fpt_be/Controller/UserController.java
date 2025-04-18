package com.fpt_be.fpt_be.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpt_be.fpt_be.Dto.UserDto;
import com.fpt_be.fpt_be.Entity.User;
import com.fpt_be.fpt_be.Service.UserService;
import com.fpt_be.fpt_be.Security.JwtTokenProvider;

@RestController
@RequestMapping("/api/admin/khach-hang")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenProvider tokenProvider;

    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.ok(Map.of("status", false, "message", "Token không hợp lệ"));
            }

            String token = authHeader.substring(7);
            if (!tokenProvider.validateToken(token) || !tokenProvider.isAdminToken(token)) {
                return ResponseEntity.ok(Map.of("status", false, "message", "Token không hợp lệ hoặc không có quyền admin"));
            }

            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách người dùng thành công",
                            "data", users));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.ok(Map.of("status", false, "message", "Token không hợp lệ"));
            }

            String token = authHeader.substring(7);
            if (!tokenProvider.validateToken(token) || !tokenProvider.isAdminToken(token)) {
                return ResponseEntity.ok(Map.of("status", false, "message", "Token không hợp lệ hoặc không có quyền admin"));
            }

            User user = userService.getUserById(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy thông tin người dùng thành công",
                            "data", user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDto userDto, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.ok(Map.of("status", false, "message", "Token không hợp lệ"));
            }

            String token = authHeader.substring(7);
            if (!tokenProvider.validateToken(token) || !tokenProvider.isAdminToken(token)) {
                return ResponseEntity.ok(Map.of("status", false, "message", "Token không hợp lệ hoặc không có quyền admin"));
            }

            User updatedUser = userService.updateUser(id, userDto);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Cập nhật thông tin người dùng thành công",
                            "data", updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.ok(Map.of("status", false, "message", "Token không hợp lệ"));
            }

            String token = authHeader.substring(7);
            if (!tokenProvider.validateToken(token) || !tokenProvider.isAdminToken(token)) {
                return ResponseEntity.ok(Map.of("status", false, "message", "Token không hợp lệ hoặc không có quyền admin"));
            }

            userService.deleteUser(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Xóa người dùng thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
} 