package com.fpt_be.fpt_be.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fpt_be.fpt_be.Dto.UserDto;
import com.fpt_be.fpt_be.Entity.User;
import com.fpt_be.fpt_be.Security.JwtTokenProvider;
import com.fpt_be.fpt_be.Service.UserService;

@RestController
@RequestMapping("/api")
public class DangNhapController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenProvider tokenProvider;


    @PostMapping("/dang-nhap")
    public ResponseEntity<?> dangNhap(@RequestBody UserDto request) {
        try {
            if (request.getEmail() == null || request.getPassword() == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", "Email và mật khẩu không được để trống"));
            }
            
            User user = userService.dangNhap(request.getEmail(), request.getPassword());
            String token = tokenProvider.generateUserToken(user.getId(), user.getEmail());
            
            return ResponseEntity.ok(Map.of(
                "status", true, 
                "message", "Đăng nhập thành công!", 
                "token", token,
                "user", Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "hoVaTen", user.getHoVaTen()
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

    @GetMapping("/user/kiem-tra-token")
    public ResponseEntity<?> kiemTraUserToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.ok(Map.of("status", false, "message", "Token không hợp lệ"));
            }

            String token = authHeader.substring(7);
            if (tokenProvider.validateToken(token)) {
                if (!tokenProvider.isUserToken(token)) {
                    return ResponseEntity.ok(Map.of("status", false, "message", "Token không phải của khách hàng"));
                }
                
                Long userId = tokenProvider.getUserIdFromToken(token);
                User user = userService.getUserById(userId);
                return ResponseEntity.ok(Map.of(
                    "status", true, 
                    "message", "Token hợp lệ", 
                    "user", Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "hoVaTen", user.getHoVaTen(),
                        "isBlocked", user.getIsBlock()
                    )
                ));
            } else {
                return ResponseEntity.ok(Map.of("status", false, "message", "Token không hợp lệ hoặc đã hết hạn"));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("status", false, "message", "Token không hợp lệ hoặc đã hết hạn"));
        }
    }

    
}