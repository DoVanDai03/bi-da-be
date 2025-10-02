package com.fpt_be.fpt_be.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpt_be.fpt_be.Request.ForgotPasswordRequest;
import com.fpt_be.fpt_be.Request.ResetPasswordRequest;
import com.fpt_be.fpt_be.Service.QuenMatKhauService;
import com.fpt_be.fpt_be.Entity.User;

@RestController
@RequestMapping("/api/auth")
public class QuenMatKhauController {

    @Autowired
    private QuenMatKhauService quenMatKhauService;

    /**
     * API gửi email reset mật khẩu
     * POST /api/auth/forgot-password
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            String result = quenMatKhauService.sendResetPasswordEmail(request.getEmail());
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "status", false,
                            "message", e.getMessage()));
        }
    }

    /**
     * API kiểm tra token reset password có hợp lệ không
     * GET /api/auth/validate-reset-token?token=xxx
     */
    @GetMapping("/validate-reset-token")
    public ResponseEntity<?> validateResetToken(@RequestParam String token) {
        try {
            boolean isValid = quenMatKhauService.validateResetToken(token);
            if (isValid) {
                return ResponseEntity.ok()
                        .body(Map.of(
                                "status", true,
                                "message", "Token hợp lệ",
                                "valid", true));
            } else {
                return ResponseEntity.ok()
                        .body(Map.of(
                                "status", false,
                                "message", "Token không hợp lệ hoặc đã hết hạn",
                                "valid", false));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "status", false,
                            "message", e.getMessage(),
                            "valid", false));
        }
    }

    /**
     * API lấy thông tin user từ token reset password
     * GET /api/auth/user-info?token=xxx
     */
    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfoByToken(@RequestParam String token) {
        try {
            User user = quenMatKhauService.getUserByResetToken(token);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy thông tin user thành công",
                            "data", Map.of(
                                    "id", user.getId(),
                                    "email", user.getEmail(),
                                    "hoVaTen", user.getHoVaTen()
                            )));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "status", false,
                            "message", e.getMessage()));
        }
    }

    /**
     * API reset mật khẩu mới
     * POST /api/auth/reset-password
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            String result = quenMatKhauService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "status", false,
                            "message", e.getMessage()));
        }
    }
}
