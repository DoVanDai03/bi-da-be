package com.fpt_be.fpt_be.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

import com.fpt_be.fpt_be.Entity.Admin;
import com.fpt_be.fpt_be.Entity.Position;
import com.fpt_be.fpt_be.Entity.Permission;
import com.fpt_be.fpt_be.Repository.AdminRepository;
import com.fpt_be.fpt_be.Repository.PositionRepository;
import com.fpt_be.fpt_be.Repository.PermissionRepository;
import com.fpt_be.fpt_be.Service.PositionPermissionService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PositionRepository positionRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;
    
    @Autowired
    private PositionPermissionService positionPermissionService;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    
    @Override
    public void run(String... args) throws Exception {
        // Tạo quyền mặc định nếu chưa có
        createDefaultPermissions();
        
        // Tạo chức vụ Super Admin nếu chưa có
        Position superAdminPosition = createSuperAdminPosition();
        
        // Kiểm tra xem đã có tài khoản admin mặc định hay chưa
        if (!adminRepository.existsByEmail("admin@fpt.com")) {
            // Tạo tài khoản admin mặc định
            Admin admin = new Admin();
            admin.setEmail("admin@fpt.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setHoVaTen("Quản trị viên");
            admin.setSdt("0123456789");
            admin.setPosition(superAdminPosition);
            admin.setTinhTrang(1);
            adminRepository.save(admin);
            System.out.println("Đã tạo tài khoản admin mặc định: admin@fpt.com / Admin@123");
        }
    }
    
    private void createDefaultPermissions() {
        // Tạo các quyền mặc định nếu chưa có
        createPermissionIfNotExists("MANAGE_ADMINS", "Quản lý admin", "Quyền quản lý tài khoản admin");
        createPermissionIfNotExists("MANAGE_USERS", "Quản lý người dùng", "Quyền quản lý người dùng");
        createPermissionIfNotExists("MANAGE_PRODUCTS", "Quản lý sản phẩm", "Quyền quản lý sản phẩm");
        createPermissionIfNotExists("MANAGE_ORDERS", "Quản lý đơn hàng", "Quyền quản lý đơn hàng");
        createPermissionIfNotExists("MANAGE_CATEGORIES", "Quản lý danh mục", "Quyền quản lý danh mục");
        createPermissionIfNotExists("VIEW_REPORTS", "Xem báo cáo", "Quyền xem báo cáo thống kê");
        createPermissionIfNotExists("MANAGE_SETTINGS", "Quản lý cài đặt", "Quyền quản lý cài đặt hệ thống");
    }
    
    private void createPermissionIfNotExists(String maQuyen, String tenQuyen, String moTa) {
        if (!permissionRepository.existsByMaQuyen(maQuyen)) {
            Permission permission = new Permission();
            permission.setMaQuyen(maQuyen);
            permission.setTenQuyen(tenQuyen);
            permission.setMoTa(moTa);
            permission.setTinhTrang(true);
            permissionRepository.save(permission);
        }
    }
    
    private Position createSuperAdminPosition() {
        // Kiểm tra xem đã có chức vụ Super Admin chưa
        Position superAdminPosition = positionRepository.findFirstByTenChucVu("Super Admin");
        if (superAdminPosition == null) {
            superAdminPosition = new Position();
            superAdminPosition.setTenChucVu("Super Admin");
            superAdminPosition.setTinhTrang(true);
            superAdminPosition = positionRepository.save(superAdminPosition);
            
            // Lấy tất cả các quyền và gán cho Super Admin
            List<Permission> allPermissions = permissionRepository.findAll();
            Set<Long> permissionIds = allPermissions.stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());
            
            // Gán tất cả quyền cho Super Admin
            positionPermissionService.assignPermissionsToPosition(superAdminPosition.getId(), permissionIds);
        }
        return superAdminPosition;
    }
} 