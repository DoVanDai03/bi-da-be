package com.fpt_be.fpt_be.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpt_be.fpt_be.Dto.CartWithProductDto;
import com.fpt_be.fpt_be.Entity.Cart;
import com.fpt_be.fpt_be.Service.CartService;

@RestController
@RequestMapping("/api/user/gio-hang")
public class HomeCartController {
    
    @Autowired
    private CartService cartService;

    @GetMapping("/khach-hang/{idKhachHang}")
    public ResponseEntity<?> getCartsByUserId(@PathVariable Long idKhachHang) {
        try {
            List<CartWithProductDto> carts = cartService.getCartsWithProductInfo(idKhachHang);
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

    @PostMapping("/them-san-pham")
    public ResponseEntity<?> addProductToCart(
            @RequestParam Long idKhachHang, 
            @RequestParam Long idSanPham,
            @RequestParam Integer soLuong,
            @RequestParam Double thanhTien) {
        try {
            Cart cart = cartService.addProductToCart(idKhachHang, idSanPham, soLuong, thanhTien);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Thêm sản phẩm vào giỏ hàng thành công",
                            "data", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
    
    @PutMapping("/cap-nhat-san-pham")
    public ResponseEntity<?> updateProductInCart(
            @RequestParam Long idKhachHang, 
            @RequestParam Long idSanPham,
            @RequestParam Integer soLuong,
            @RequestParam Double thanhTien) {
        try {
            Cart cart = cartService.updateProductQuantity(idKhachHang, idSanPham, soLuong, thanhTien);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Cập nhật số lượng sản phẩm thành công",
                            "data", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
    
    @DeleteMapping("/xoa-san-pham")
    public ResponseEntity<?> removeProductFromCart(
            @RequestParam Long idKhachHang, 
            @RequestParam Long idSanPham) {
        try {
            cartService.removeProductFromCart(idKhachHang, idSanPham);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Xóa sản phẩm khỏi giỏ hàng thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/ap-dung-ma-giam-gia")
    public ResponseEntity<?> applyDiscountCode(
            @RequestParam Long idKhachHang,
            @RequestParam String maGiamGia,
            @RequestParam(required = false) Long idSanPham) {
        try {
            Map<String, Object> result = cartService.applyDiscountCode(idKhachHang, maGiamGia, idSanPham);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
}
