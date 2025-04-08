package com.fpt_be.fpt_be.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.fpt_be.fpt_be.Entity.Admin;
import com.fpt_be.fpt_be.Repository.AdminRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    
    @Override
    public void run(String... args) throws Exception {
        // Kiểm tra xem đã có tài khoản admin mặc định hay chưa
        if (!adminRepository.existsByEmail("admin@fpt.com")) {
            // Tạo tài khoản admin mặc định
            Admin admin = new Admin();
            admin.setEmail("admin@fpt.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setHoVaTen("Quản trị viên");
            admin.setSdt("0123456789");
            admin.setChucVu("Quản trị viên");
            admin.setQuyenHan("FULL_ACCESS");
            
            adminRepository.save(admin);
            System.out.println("Đã tạo tài khoản admin mặc định: admin@fpt.com / Admin@123");
        }
    }
} 