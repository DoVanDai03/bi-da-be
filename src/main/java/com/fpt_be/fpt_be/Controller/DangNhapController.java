package com.fpt_be.fpt_be.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpt_be.fpt_be.Dto.UserDto;
import com.fpt_be.fpt_be.Service.UserService;

@RestController
@RequestMapping("/api")
public class DangNhapController {
    @Autowired
    private UserService userService;

    @PostMapping("/dang-nhap")
    public ResponseEntity<?> dangNhap(@RequestBody UserDto request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", "Email và mật khẩu không được trống"));
        }

        try {
            boolean result = userService.dangNhap(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(Map.of("status", result, "message", result ? "Đăng nhập thành công" : "Đăng nhập thất bại"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", false, "message", "Có lỗi xảy ra: " + e.getMessage()));
        }
    }
}