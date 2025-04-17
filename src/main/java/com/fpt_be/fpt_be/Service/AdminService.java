package com.fpt_be.fpt_be.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fpt_be.fpt_be.Repository.AdminRepository;
import com.fpt_be.fpt_be.Entity.Admin;
import com.fpt_be.fpt_be.Dto.AdminDto;

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
            if (!"Hoạt động".equals(admin.getTinhTrang())) {
                throw new RuntimeException("Tài khoản đã bị vô hiệu hóa");
            }
            return admin;
        } else {
            throw new RuntimeException("Mật khẩu không chính xác");
        }
    }

    // Get all admins
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quản trị viên với id: " + id));
    }

    public Admin createAdmin(AdminDto adminDto) {
        if (adminRepository.existsByEmail(adminDto.getEmail())) {
            throw new RuntimeException("Email đã tồn tại trong hệ thống");
        }

        Admin admin = new Admin();
        admin.setEmail(adminDto.getEmail());
        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        admin.setHoVaTen(adminDto.getHoVaTen());
        admin.setSdt(adminDto.getSdt());
        admin.setChucVu(adminDto.getChucVu());
        admin.setQuyenHan(adminDto.getQuyenHan());
        admin.setTinhTrang(adminDto.getTinhTrang() != null ? adminDto.getTinhTrang() : "Hoạt động");
        
        return adminRepository.save(admin);
    }

    public Admin updateAdmin(Long id, AdminDto adminDto) {
        Admin existingAdmin = getAdminById(id);
        
        // Check if email is being changed and if new email already exists
        if (!existingAdmin.getEmail().equals(adminDto.getEmail()) && 
            adminRepository.existsByEmail(adminDto.getEmail())) {
            throw new RuntimeException("Email mới đã tồn tại trong hệ thống");
        }

        existingAdmin.setEmail(adminDto.getEmail());
        if (adminDto.getPassword() != null && !adminDto.getPassword().isEmpty()) {
            existingAdmin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        }
        existingAdmin.setHoVaTen(adminDto.getHoVaTen());
        existingAdmin.setSdt(adminDto.getSdt());
        existingAdmin.setChucVu(adminDto.getChucVu());
        existingAdmin.setQuyenHan(adminDto.getQuyenHan());
        if (adminDto.getTinhTrang() != null) {
            existingAdmin.setTinhTrang(adminDto.getTinhTrang());
        }

        return adminRepository.save(existingAdmin);
    }

    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy quản trị viên với id: " + id);
        }
        adminRepository.deleteById(id);
    }
} 