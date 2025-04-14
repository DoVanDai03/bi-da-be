package com.fpt_be.fpt_be.Controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpt_be.fpt_be.Dto.CartWithProductDto;
import com.fpt_be.fpt_be.Service.CartService;
import com.fpt_be.fpt_be.Service.OrderService;
import com.fpt_be.fpt_be.Entity.Order;

@RestController
@RequestMapping("/api/admin/chi-tiet-don-hang")
public class ChiTietDonHangController {
    
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getAllDonHang() {
        try {
            List<Map<String, Object>> orders = orderService.getAllOrdersWithDetails();
            
            if (orders.isEmpty()) {
                return ResponseEntity.ok()
                        .body(Map.of(
                                "status", true,
                                "message", "Chưa có đơn hàng nào",
                                "data", new ArrayList<>()));
            }
            
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách đơn hàng thành công",
                            "data", orders));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/khach-hang/{idKhachHang}")
    public ResponseEntity<?> getDonHangByUserId(@PathVariable Long idKhachHang) {
        try {
            List<CartWithProductDto> carts = cartService.getCartsWithProductInfo(idKhachHang);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách đơn hàng của khách hàng thành công",
                            "data", carts));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/don-hang/{idDonHang}")
    public ResponseEntity<?> getDonHangById(@PathVariable Long idDonHang) {
        try {
            Order order = orderService.getOrderById(idDonHang);
            List<CartWithProductDto> orderItems = cartService.getCartsWithProductInfoByOrderId(idDonHang);
            
            Map<String, Object> orderDetail = new HashMap<>();
            orderDetail.put("id", order.getId());
            orderDetail.put("idKhachHang", order.getIdKhachHang());
            orderDetail.put("tenNguoiNhan", order.getTenNguoiNhan());
            orderDetail.put("sdtNguoiNhan", order.getSdtNguoiNhan());
            orderDetail.put("diaChiGiao", order.getDiaChiGiao());
            orderDetail.put("tongTien", order.getTongTien());
            orderDetail.put("trangThai", order.getTrangThai());
            orderDetail.put("phuongThucThanhToan", order.getPhuongThucThanhToan());
            orderDetail.put("maGiamGia", order.getMaGiamGia());
            orderDetail.put("ngayDat", order.getNgayDat());
            orderDetail.put("ngayCapNhat", order.getNgayCapNhat());
            orderDetail.put("chiTietDonHang", orderItems);
            
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy chi tiết đơn hàng thành công",
                            "data", orderDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/cap-nhat-trang-thai/{idDonHang}")
    public ResponseEntity<?> updateTrangThaiDonHang(
            @PathVariable Long idDonHang,
            @RequestBody Map<String, String> requestBody) {
        try {
            String trangThaiMoi = requestBody.get("trangThai");
            if (trangThaiMoi == null || trangThaiMoi.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("status", false, "message", "Trạng thái không được để trống"));
            }

            Order updatedOrder = orderService.updateOrderStatus(idDonHang, trangThaiMoi);
            
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Cập nhật trạng thái đơn hàng thành công",
                            "data", Map.of(
                                "id", updatedOrder.getId(),
                                "trangThai", updatedOrder.getTrangThai(),
                                "ngayCapNhat", updatedOrder.getNgayCapNhat()
                            )));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    // @PutMapping("/cap-nhat-dia-chi/{idDonHang}")
    // public ResponseEntity<?> updateDiaChiGiaoHang(
    //         @PathVariable Long idDonHang,
    //         @RequestParam String diaChiMoi) {
    //     try {
    //         cartService.updateDiaChiGiaoHang(idDonHang, diaChiMoi);
    //         return ResponseEntity.ok()
    //                 .body(Map.of(
    //                         "status", true,
    //                         "message", "Cập nhật địa chỉ giao hàng thành công"));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest()
    //                 .body(Map.of("status", false, "message", e.getMessage()));
    //     }
    // }

    // @PutMapping("/cap-nhat-phuong-thuc-thanh-toan/{idDonHang}")
    // public ResponseEntity<?> updatePhuongThucThanhToan(
    //         @PathVariable Long idDonHang,
    //         @RequestParam String phuongThucMoi) {
    //     try {
    //         cartService.updatePhuongThucThanhToan(idDonHang, phuongThucMoi);
    //         return ResponseEntity.ok()
    //                 .body(Map.of(
    //                         "status", true,
    //                         "message", "Cập nhật phương thức thanh toán thành công"));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest()
    //                 .body(Map.of("status", false, "message", e.getMessage()));
    //     }
    // }

    // @DeleteMapping("/{idDonHang}")
    // public ResponseEntity<?> deleteDonHang(@PathVariable Long idDonHang) {
    //     try {
    //         cartService.deleteDonHang(idDonHang);
    //         return ResponseEntity.ok()
    //                 .body(Map.of(
    //                         "status", true,
    //                         "message", "Xóa đơn hàng thành công"));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest()
    //                 .body(Map.of("status", false, "message", e.getMessage()));
    //     }
    // }
}
