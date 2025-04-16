package com.fpt_be.fpt_be.Service.Impl;

import com.fpt_be.fpt_be.Request.DoiMatKhauRequest;
import com.fpt_be.fpt_be.Request.QuenMatKhauRequest;
import com.fpt_be.fpt_be.Service.QuenMatKhauService;
import com.fpt_be.fpt_be.Entity.User;
import com.fpt_be.fpt_be.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class QuenMatKhauServiceImpl implements QuenMatKhauService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> xacNhanMa(QuenMatKhauRequest request) {
        // Đã xử lý bằng Jcrip
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> doiMatKhau(DoiMatKhauRequest request) {
        try {
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