package com.fpt_be.fpt_be.Service.Impl;

import com.fpt_be.fpt_be.Request.DoiMatKhauRequest;
import com.fpt_be.fpt_be.Request.QuenMatKhauRequest;
import com.fpt_be.fpt_be.Service.QuenMatKhauService;
import com.fpt_be.fpt_be.Entity.User;
import com.fpt_be.fpt_be.Repository.UserRepository;
import com.fpt_be.fpt_be.Validator.QuenMatKhauValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuenMatKhauServiceImpl implements QuenMatKhauService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> xacNhanMa(QuenMatKhauRequest request) {
        // Validate request
        List<String> errors = QuenMatKhauValidator.validateQuenMatKhau(request);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", false,
                "errors", errors
            ));
        }

        // Đã xử lý bằng Jcrip
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> doiMatKhau(DoiMatKhauRequest request) {
        try {
            // Validate request
            List<String> errors = QuenMatKhauValidator.validateDoiMatKhau(request);
            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", false,
                    "errors", errors
                ));
            }

            // Tìm user theo email
            User user = userRepository.findByEmail(request.getEmail());
            if (user == null) {
                throw new RuntimeException("Không tìm thấy tài khoản với email: " + request.getEmail());
            }

            // Cập nhật mật khẩu mới
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);

            return ResponseEntity.ok().body(Map.of(
                "status", true,
                "message", "Đổi mật khẩu thành công"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", false,
                "message", e.getMessage()
            ));
        }
    }
} 