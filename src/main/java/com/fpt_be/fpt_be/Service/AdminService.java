package com.fpt_be.fpt_be.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fpt_be.fpt_be.Repository.AdminRepository;
import com.fpt_be.fpt_be.Entity.Admin;

@Service
public class AdminService {
    @Autowired  
    private AdminRepository adminRepository;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    
    public Admin dangNhapAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            throw new RuntimeException("Email không tồn tại");
        }
        if (passwordEncoder.matches(password, admin.getPassword())) {
            return admin;
        } else {
            throw new RuntimeException("Mật khẩu không chính xác");
        }
    }

    // Get all admins
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

} 