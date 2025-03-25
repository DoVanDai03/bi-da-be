package com.fpt_be.fpt_be.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fpt_be.fpt_be.Repository.UserRepository;
import com.fpt_be.fpt_be.Request.DangKyRequest;
import com.fpt_be.fpt_be.Entity.User;

@Service
public class UserService {
    @Autowired  
    private UserRepository userRepository;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public String dangKy(DangKyRequest dangKyRequest) {
        // Check if email already exists
        if (userRepository.existsByEmail(dangKyRequest.getEmail())) {
            throw new RuntimeException("Email đã tồn tại trong hệ thống");
        } else {
            User newUser = new User();
            newUser.setEmail(dangKyRequest.getEmail());
            String encodedPassword = passwordEncoder.encode(dangKyRequest.getPassword());
            newUser.setPassword(encodedPassword);
            userRepository.save(newUser);
            return "Đăng ký thành công";
        }
    }
    public boolean dangNhap(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
             return false;
        }
        if (passwordEncoder.matches(password, user.getPassword())) {
            return true;
        } else {
            throw new RuntimeException("Mật khẩu không chính xác");
        }
    }
}