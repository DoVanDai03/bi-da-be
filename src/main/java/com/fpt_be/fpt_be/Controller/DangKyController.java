package com.fpt_be.fpt_be.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpt_be.fpt_be.Dto.UserDto;
import com.fpt_be.fpt_be.Request.DangKyRequest;
import com.fpt_be.fpt_be.Service.UserService;
import com.fpt_be.fpt_be.Validator.UserValidator;

@RestController
@RequestMapping("/api")
public class DangKyController {
    @Autowired
    private UserService userService;

    @Value("${app.frontend.login-url:http://localhost:5173/dang-nhap}")
    private String frontendLoginUrl;

    @PostMapping("/dang-ky")
    public ResponseEntity<?> dangKy(@RequestBody UserDto request) {
        try {
            // Chuyển đổi từ UserDto sang DangKyRequest
            DangKyRequest dangKyRequest = new DangKyRequest();
            dangKyRequest.setEmail(request.getEmail());
            dangKyRequest.setPassword(request.getPassword());
            dangKyRequest.setHoVaTen(request.getHoVaTen());
            dangKyRequest.setSdt(request.getSdt());
            
            // Validate dữ liệu
            List<String> validationErrors = UserValidator.validateDangKy(dangKyRequest);
            if (!validationErrors.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                            "status", false, 
                            "message", "Dữ liệu không hợp lệ",
                            "errors", validationErrors));
            }
            
            // Thực hiện đăng ký
            String result = userService.dangKy(dangKyRequest);
            return ResponseEntity.ok(Map.of("status", true, "message", result));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", false, "message", "Có lỗi xảy ra: " + e.getMessage()));
        }
    }

    @GetMapping("/xac-minh")
    public Object verifyAccount(@RequestParam("token") String token,
                                @RequestParam(value = "redirect", required = false, defaultValue = "true") boolean redirect) {
        try {
            String result = userService.verifyAccount(token);
            if (redirect) {
                String target = frontendLoginUrl + "?verified=1&message=" + java.net.URLEncoder.encode(result, java.nio.charset.StandardCharsets.UTF_8);
                return new RedirectView(target);
            }
            return ResponseEntity.ok(Map.of("status", true, "message", result));
        } catch (RuntimeException e) {
            if (redirect) {
                String target = frontendLoginUrl + "?verified=0&message=" + java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8);
                return new RedirectView(target);
            }
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        } catch (Exception e) {
            if (redirect) {
                String target = frontendLoginUrl + "?verified=0&message=" + java.net.URLEncoder.encode("Có lỗi xảy ra", java.nio.charset.StandardCharsets.UTF_8);
                return new RedirectView(target);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", false, "message", "Có lỗi xảy ra: " + e.getMessage()));
        }
    }
}