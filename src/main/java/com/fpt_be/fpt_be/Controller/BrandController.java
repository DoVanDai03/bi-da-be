package com.fpt_be.fpt_be.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpt_be.fpt_be.Dto.BrandDto;
import com.fpt_be.fpt_be.Entity.Brand;
import com.fpt_be.fpt_be.Service.BrandService;

@RestController
@RequestMapping("/api/admin/thuong-hieu")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    public ResponseEntity<?> getAllBrands() {
        try {
            List<Brand> brands = brandService.getAllBrands();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách thương hiệu thành công",
                            "data", brands));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBrandById(@PathVariable Long id) {
        try {
            Brand brand = brandService.getBrandById(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy thông tin thương hiệu thành công",
                            "data", brand));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createBrand(@RequestBody BrandDto brandDto) {
        try {
            Brand createdBrand = brandService.createBrand(brandDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "status", true,
                            "message", "Thêm thương hiệu thành công",
                            "data", createdBrand));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable Long id, @RequestBody BrandDto brandDto) {
        try {
            Brand updatedBrand = brandService.updateBrand(id, brandDto);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Cập nhật thương hiệu thành công",
                            "data", updatedBrand));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable Long id) {
        try {
            brandService.deleteBrand(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Xóa thương hiệu thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<?> toggleBrandStatus(@PathVariable Long id) {
        try {
            Brand updatedBrand = brandService.toggleBrandStatus(id);
            String message = updatedBrand.getTinhTrang() == 1 ? 
                "Kích hoạt thương hiệu thành công" : 
                "Tạm dừng thương hiệu thành công";
            
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", message,
                            "data", updatedBrand));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
} 