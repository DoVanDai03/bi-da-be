package com.fpt_be.fpt_be.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpt_be.fpt_be.Entity.Product;
import com.fpt_be.fpt_be.Service.ProductService;

@RestController
@RequestMapping("/api/user")
public class HomeProductController {

    @Autowired
    private ProductService productService;

    /**
     * API lấy sản phẩm cho trang chủ (home-page)
     */
    @GetMapping("/san-pham/home-page")
    public ResponseEntity<?> getProductsForHomePage() {
        try {
            List<Product> products = productService.getAllProducts();
            
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách sản phẩm trang chủ thành công",
                            "data", products
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
    
    
    /**
     * API lấy thông tin chi tiết sản phẩm theo ID
     */
    @GetMapping("/san-pham/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy thông tin sản phẩm thành công",
                            "data", product));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
    
    /**
     * API lấy danh sách sản phẩm mới nhất
     */
    @GetMapping("/moi-nhat")
    public ResponseEntity<?> getNewestProducts(@RequestParam(defaultValue = "8") int limit) {
        try {
            Pageable pageable = PageRequest.of(0, limit);
            List<Product> newestProducts = productService.getNewestProducts(pageable);
            
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách sản phẩm mới nhất thành công",
                            "data", newestProducts
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
    
    /**
     * API lấy sản phẩm theo danh mục
     */
    @GetMapping("/danh-muc/{categoryId}")
    public ResponseEntity<?> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "8") int limit) {
        try {
            Pageable pageable = PageRequest.of(0, limit);
            List<Product> products = productService.getProductsByCategory(categoryId, pageable);
            
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
    
    /**
     * API tìm kiếm sản phẩm theo từ khóa
     */
    @GetMapping("/tim-kiem")
    public ResponseEntity<?> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<Product> products = productService.searchProductsByKeyword(keyword, pageable);
            long totalItems = productService.countSearchResults(keyword);
            
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Tìm kiếm sản phẩm thành công",
                            "data", products,
                            "totalItems", totalItems,
                            "currentPage", page,
                            "totalPages", (int) Math.ceil((double) totalItems / size)
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    /**
     * API cập nhật số lượng hàng tồn kho sau khi đặt hàng
     */
    @PutMapping("/san-pham/{id}/cap-nhat-ton-kho")
    public ResponseEntity<?> updateProductInventory(
            @PathVariable Long id,
            @RequestParam int soLuongDat) {
        try {
            if (soLuongDat <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("status", false, "message", "Số lượng đặt phải lớn hơn 0"));
            }

            Product updatedProduct = productService.updateProductInventory(id, soLuongDat);
            
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Cập nhật số lượng tồn kho thành công",
                            "data", updatedProduct
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
} 