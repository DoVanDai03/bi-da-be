package com.fpt_be.fpt_be.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fpt_be.fpt_be.Repository.AdminRepository;
import com.fpt_be.fpt_be.Repository.PositionRepository;
import com.fpt_be.fpt_be.Entity.Admin;
import com.fpt_be.fpt_be.Entity.Position;
import com.fpt_be.fpt_be.Dto.AdminDto;

@Service
public class AdminService {
    @Autowired  
    private AdminRepository adminRepository;
    
    @Autowired
    private PositionRepository positionRepository;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    
    public Admin dangNhapAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            throw new RuntimeException("Email không tồn tại");
        }
        if (passwordEncoder.matches(password, admin.getPassword())) {
            if (admin.getTinhTrang() != 1) {
                throw new RuntimeException("Tài khoản đã bị vô hiệu hóa");
            }
            return admin;
        } else {
            throw new RuntimeException("Mật khẩu không chính xác");
        }
    }

    public AdminDto convertToDto(Admin admin) {
        AdminDto dto = new AdminDto();
        dto.setId(admin.getId());
        dto.setEmail(admin.getEmail());
        dto.setPassword(admin.getPassword());
        dto.setHoVaTen(admin.getHoVaTen());
        dto.setSdt(admin.getSdt());
        dto.setTinhTrang(admin.getTinhTrang());
        dto.setNgayTao(admin.getNgayTao());
        dto.setNgayCapNhat(admin.getNgayCapNhat());
        
        if (admin.getPosition() != null) {
            dto.setChucVu(admin.getPosition().getTenChucVu());
            dto.setChucVuId(admin.getPosition().getId());
        }
        
        return dto;
    }

    // Get all admins
    public List<AdminDto> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quản trị viên với id: " + id));
    }

    private Position findPosition(AdminDto adminDto) {
        Position position = null;
        
        // Try to find position by ID first
        if (adminDto.getChucVuId() != null) {
            position = positionRepository.findById(adminDto.getChucVuId())
                    .orElse(null);
        }
        
        // If not found by ID and name is provided, try to find by name
        if (position == null && adminDto.getChucVu() != null && !adminDto.getChucVu().isEmpty()) {
            position = positionRepository.findFirstByTenChucVu(adminDto.getChucVu());
        }
        
        if (position == null) {
            throw new RuntimeException("Không tìm thấy chức vụ");
        }
        
        return position;
    }

    public Admin createAdmin(AdminDto adminDto) {
        if (adminRepository.existsByEmail(adminDto.getEmail())) {
            throw new RuntimeException("Email đã tồn tại trong hệ thống");
        }

        Position position = findPosition(adminDto);

        Admin admin = new Admin();
        admin.setEmail(adminDto.getEmail());
        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        admin.setHoVaTen(adminDto.getHoVaTen());
        admin.setSdt(adminDto.getSdt());
        admin.setPosition(position);
        admin.setTinhTrang(adminDto.getTinhTrang() != null ? adminDto.getTinhTrang() : 1);
        
        return adminRepository.save(admin);
    }

    public Admin updateAdmin(Long id, AdminDto adminDto) {
        Admin existingAdmin = getAdminById(id);
        
        // Check if email is being changed and if new email already exists
        if (!existingAdmin.getEmail().equals(adminDto.getEmail()) && 
            adminRepository.existsByEmail(adminDto.getEmail())) {
            throw new RuntimeException("Email mới đã tồn tại trong hệ thống");
        }

        Position position = findPosition(adminDto);

        existingAdmin.setEmail(adminDto.getEmail());
        if (adminDto.getPassword() != null && !adminDto.getPassword().isEmpty()) {
            existingAdmin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        }
        existingAdmin.setHoVaTen(adminDto.getHoVaTen());
        existingAdmin.setSdt(adminDto.getSdt());
        existingAdmin.setPosition(position);
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