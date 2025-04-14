package com.fpt_be.fpt_be.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpt_be.fpt_be.Dto.CartDto;
import com.fpt_be.fpt_be.Entity.Cart;
import com.fpt_be.fpt_be.Service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<?> getAllCarts() {
        try {
            List<Cart> carts = cartService.getAllCarts();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách giỏ hàng thành công",
                            "data", carts));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
    
    @GetMapping("/khach-hang/{idKhachHang}")
    public ResponseEntity<?> getCartsByUserId(@PathVariable Long idKhachHang) {
        try {
            List<Cart> carts = cartService.getCartsByUserId(idKhachHang);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách giỏ hàng của khách hàng thành công",
                            "data", carts));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
    
    @GetMapping("/don-hang/{idDonHang}")
    public ResponseEntity<?> getCartsByOrderId(@PathVariable Long idDonHang) {
        try {
            List<Cart> carts = cartService.getCartsByOrderId(idDonHang);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách giỏ hàng theo đơn hàng thành công",
                            "data", carts));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCartById(@PathVariable Long id) {
        try {
            Cart cart = cartService.getCartById(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy thông tin giỏ hàng thành công",
                            "data", cart));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createCart(@RequestBody CartDto cartDto) {
        try {
            Cart createdCart = cartService.createCart(cartDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "status", true,
                            "message", "Thêm giỏ hàng thành công",
                            "data", createdCart));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
    
    @PostMapping("/assign-order")
    public ResponseEntity<?> assignOrderToCart(
            @RequestParam Long idKhachHang, 
            @RequestParam Long idDonHang) {
        try {
            cartService.assignOrderToCart(idKhachHang, idDonHang);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Gán đơn hàng cho giỏ hàng thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCart(@PathVariable Long id, @RequestBody CartDto cartDto) {
        try {
            Cart updatedCart = cartService.updateCart(id, cartDto);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Cập nhật giỏ hàng thành công",
                            "data", updatedCart));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable Long id) {
        try {
            cartService.deleteCart(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Xóa giỏ hàng thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
}
