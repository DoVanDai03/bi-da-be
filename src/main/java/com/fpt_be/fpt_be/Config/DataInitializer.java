package com.fpt_be.fpt_be.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.HashSet;

import com.fpt_be.fpt_be.Entity.Admin;
import com.fpt_be.fpt_be.Entity.Position;
import com.fpt_be.fpt_be.Entity.Permission;
import com.fpt_be.fpt_be.Entity.PositionPermission;
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
        // Danh sách các quyền mặc định
        List<String[]> defaultPermissions = Arrays.asList(
            // Quản lý tài khoản Admin
            new String[]{"ADMIN_VIEW", "Xem danh sách admin"},
            new String[]{"ADMIN_CREATE", "Thêm mới admin"},
            new String[]{"ADMIN_UPDATE", "Cập nhật admin"},
            new String[]{"ADMIN_DELETE", "Xóa admin"},
            new String[]{"ADMIN_DETAIL", "Xem chi tiết admin"},

            
            
            // Quản lý sản phẩm
            new String[]{"PRODUCT_VIEW", "Xem danh sách sản phẩm"},
            new String[]{"PRODUCT_CREATE", "Thêm mới sản phẩm"},
            new String[]{"PRODUCT_UPDATE", "Cập nhật sản phẩm"},
            new String[]{"PRODUCT_DELETE", "Xóa sản phẩm"},
            
            // Quản lý danh mục
            new String[]{"CATEGORY_VIEW", "Xem danh sách danh mục"},
            new String[]{"CATEGORY_CREATE", "Thêm mới danh mục"},
            new String[]{"CATEGORY_UPDATE", "Cập nhật danh mục"},
            new String[]{"CATEGORY_DELETE", "Xóa danh mục"},
            
            // Quản lý đơn hàng
            new String[]{"ORDER_VIEW", "Xem danh sách đơn hàng"},
            new String[]{"ORDER_UPDATE_STATUS", "Cập nhật trạng thái đơn hàng"},
            
            // Quản lý mã giảm giá
            new String[]{"DISCOUNT_VIEW", "Xem danh sách mã giảm giá"},
            new String[]{"DISCOUNT_CREATE", "Thêm mới mã giảm giá"},
            new String[]{"DISCOUNT_UPDATE", "Cập nhật mã giảm giá"},
            new String[]{"DISCOUNT_DELETE", "Xóa mã giảm giá"},
            
            // Quản lý quyền hạn
            new String[]{"PERMISSION_VIEW", "Xem danh sách quyền"},
            new String[]{"PERMISSION_CREATE", "Thêm mới quyền"},
            new String[]{"PERMISSION_UPDATE", "Cập nhật quyền"},
            new String[]{"PERMISSION_DELETE", "Xóa quyền"},
            new String[]{"PERMISSION_ASSIGN", "Phân quyền cho chức vụ"},
            
            // Quản lý chức vụ
            new String[]{"CHUC_VU_VIEW", "Xem danh sách chức vụ"},
            new String[]{"CHUC_VU_CREATE", "Thêm mới chức vụ"},
            new String[]{"CHUC_VU_UPDATE", "Cập nhật chức vụ"},
            new String[]{"CHUC_VU_DELETE", "Xóa chức vụ"},
            
            // Quản lý nhà cung cấp
            new String[]{"SUPPLIER_VIEW", "Xem danh sách nhà cung cấp"},
            new String[]{"SUPPLIER_CREATE", "Thêm mới nhà cung cấp"},
            new String[]{"SUPPLIER_UPDATE", "Cập nhật nhà cung cấp"},
            new String[]{"SUPPLIER_DELETE", "Xóa nhà cung cấp"},
            
            // Quản lý thương hiệu
            new String[]{"BRAND_VIEW", "Xem danh sách thương hiệu"},
            new String[]{"BRAND_CREATE", "Thêm mới thương hiệu"},
            new String[]{"BRAND_UPDATE", "Cập nhật thương hiệu"},
            new String[]{"BRAND_DELETE", "Xóa thương hiệu"},
            
            // Quản lý đánh giá
            new String[]{"REVIEW_VIEW", "Xem danh sách đánh giá"},
            new String[]{"REVIEW_CREATE", "Thêm mới đánh giá"},
            new String[]{"REVIEW_UPDATE", "Cập nhật đánh giá"},
            new String[]{"REVIEW_DELETE", "Xóa đánh giá"},
            
            // Thống kê
            new String[]{"STATISTICS_VIEW", "Xem thống kê"},

            // Quản lý khách hàng
            new String[]{"CUSTOMER_VIEW", "Xem danh sách khách hàng"},
            new String[]{"CUSTOMER_CREATE", "Thêm mới khách hàng"},
            new String[]{"CUSTOMER_UPDATE", "Cập nhật khách hàng"},
            new String[]{"CUSTOMER_DELETE", "Xóa khách hàng"},

            //Quyền truy cập
            new String[]{"ACCESS_VIEW", "Xem danh sách quyền truy cập"},
            new String[]{"ACCESS_CREATE", "Thêm mới quyền truy cập"},
            new String[]{"ACCESS_UPDATE", "Cập nhật quyền truy cập"},
            new String[]{"ACCESS_DELETE", "Xóa quyền truy cập"}
            
            
        );
        
        // Chỉ tạo các quyền chưa tồn tại
        for (String[] permissionInfo : defaultPermissions) {
            String maQuyen = permissionInfo[0];
            String tenQuyen = permissionInfo[1];
            
            if (!permissionRepository.existsByMaQuyen(maQuyen)) {
                Permission permission = new Permission();
                permission.setMaQuyen(maQuyen);
                permission.setTenQuyen(tenQuyen);
                permissionRepository.save(permission);
                System.out.println("Đã tạo quyền mới: " + maQuyen + " - " + tenQuyen);
            }
        }
    }
    
    private Position createSuperAdminPosition() {
        // Kiểm tra xem đã có chức vụ Super Admin chưa
        Position superAdminPosition = positionRepository.findFirstByTenChucVu("Super Admin");
        if (superAdminPosition == null) {
            superAdminPosition = new Position();
            superAdminPosition.setId(1L);
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
            System.out.println("Đã tạo chức vụ Super Admin và gán tất cả quyền");
        } else {
            // Nếu Super Admin đã tồn tại, chỉ gán thêm các quyền còn thiếu
            List<Permission> allPermissions = permissionRepository.findAll();
            Set<Long> allPermissionIds = allPermissions.stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());
            
            // Lấy danh sách quyền hiện tại của Super Admin
            List<PositionPermission> currentPermissions = positionPermissionService.getPermissionsByPosition(superAdminPosition.getId());
            Set<Long> currentPermissionIds = currentPermissions.stream()
                .map(pp -> pp.getPermission().getId())
                .collect(Collectors.toSet());
            
            // Tìm các quyền còn thiếu
            Set<Long> missingPermissionIds = new HashSet<>(allPermissionIds);
            missingPermissionIds.removeAll(currentPermissionIds);
            
            // Chỉ gán thêm các quyền còn thiếu
            if (!missingPermissionIds.isEmpty()) {
                positionPermissionService.assignPermissionsToPosition(superAdminPosition.getId(), missingPermissionIds);
                System.out.println("Đã gán thêm " + missingPermissionIds.size() + " quyền mới cho Super Admin");
            }
        }
        return superAdminPosition;
    }
}