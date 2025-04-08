package com.fpt_be.fpt_be.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpt_be.fpt_be.Entity.Category;
import com.fpt_be.fpt_be.Entity.Product;
import com.fpt_be.fpt_be.Service.CategoryService;
import com.fpt_be.fpt_be.Service.ProductService;

@RestController
@RequestMapping("/api/user/danh-muc")
public class HomeCategoryController {

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductService productService;

    /**
     * API lấy danh sách danh mục cho trang chủ
     */
    @GetMapping("/home-page")
    public ResponseEntity<?> getCategoriesForHomePage() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách danh mục thành công",
                            "data", categories));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }


    @GetMapping("/{id}/san-pham")
    public ResponseEntity<?> getProductsByCategory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "8") int limit) {
        try {
            Pageable pageable = PageRequest.of(0, limit);
            List<Product> products = productService.getProductsByCategory(id, pageable);
            
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách sản phẩm theo danh mục thành công",
                            "data", products
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
} 