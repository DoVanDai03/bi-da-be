package com.fpt_be.fpt_be.Controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpt_be.fpt_be.Dto.OrderDto;
import com.fpt_be.fpt_be.Dto.CartWithProductDto;
import com.fpt_be.fpt_be.Service.CartService;
import com.fpt_be.fpt_be.Service.OrderService;
import com.fpt_be.fpt_be.Entity.Order;
import com.fpt_be.fpt_be.Entity.Cart;
import com.fpt_be.fpt_be.Repository.CartRepository;

@RestController
@RequestMapping("/api/user/chi-tiet-don-hang")
public class HomeChiTietDonHangController {
    
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/khach-hang/{idKhachHang}")
    public ResponseEntity<?> getDonHangByUserId(
            @PathVariable Long idKhachHang,
            @RequestHeader("Authorization") String token) {
        try {
            // Kiểm tra token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(401)
                        .body(Map.of("status", false, "message", "Unauthorized - Token không hợp lệ"));
            }

            // Lấy tất cả đơn hàng của khách hàng
            List<Order> orders = orderService.getOrdersByCustomerId(idKhachHang);
            
            if (orders.isEmpty()) {
                return ResponseEntity.ok()
                        .body(Map.of(
                                "status", true,
                                "message", "Khách hàng chưa có đơn hàng nào",
                                "data", new ArrayList<>()));
            }

            // Lấy chi tiết từng đơn hàng
            List<Map<String, Object>> orderDetails = new ArrayList<>();
            for (Order order : orders) {
                // Lấy chi tiết sản phẩm trong đơn hàng
                List<CartWithProductDto> orderItems = cartService.getCartsWithProductInfoByOrderId(order.getId());
                
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
                
                // Thêm chi tiết sản phẩm vào đơn hàng
                List<Map<String, Object>> productDetails = new ArrayList<>();
                for (CartWithProductDto item : orderItems) {
                    Map<String, Object> productDetail = new HashMap<>();
                    productDetail.put("id", item.getId());
                    productDetail.put("idSanPham", item.getIdSanPham());
                    productDetail.put("tenSanPham", item.getTenSanPham());
                    productDetail.put("giaSanPham", item.getGiaSanPham());
                    productDetail.put("soLuong", item.getSoLuong());
                    productDetail.put("thanhTien", item.getThanhTien());
                    productDetail.put("hinhAnh", item.getHinhAnh());
                    productDetail.put("kichCo", item.getKichCo());
                    productDetail.put("mauSac", item.getMauSac());
                    productDetail.put("chatLieu", item.getChatLieu());
                    productDetails.add(productDetail);
                }
                orderDetail.put("chiTietDonHang", productDetails);
                
                orderDetails.add(orderDetail);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", true);
            response.put("message", "Lấy danh sách đơn hàng thành công");
            response.put("data", orderDetails);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/don-hang/{idDonHang}")
    public ResponseEntity<?> getDonHangById(@PathVariable Long idDonHang) {
        try {
            List<CartWithProductDto> carts = cartService.getCartsWithProductInfoByOrderId(idDonHang);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy chi tiết đơn hàng thành công",
                            "data", carts));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/tao-don-hang")
    public ResponseEntity<?> createDonHang(@RequestBody OrderDto orderDto) {
        try {
            // Validate required fields
            if (orderDto.getIdKhachHang() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("status", false, "message", "ID khách hàng không được để trống"));
            }
            
            if (orderDto.getDiaChiGiao() == null || orderDto.getDiaChiGiao().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("status", false, "message", "Địa chỉ giao hàng không được để trống"));
            }
            
            if (orderDto.getSdtNguoiNhan() == null || orderDto.getSdtNguoiNhan().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("status", false, "message", "Số điện thoại người nhận không được để trống"));
            }
            
            if (orderDto.getTenNguoiNhan() == null || orderDto.getTenNguoiNhan().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("status", false, "message", "Tên người nhận không được để trống"));
            }

            if (orderDto.getChiTietDonHang() == null || orderDto.getChiTietDonHang().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("status", false, "message", "Chi tiết đơn hàng không được để trống"));
            }
            
            // Tạo đơn hàng mới
            Order order = orderService.createOrder(orderDto);
            
            if (order == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("status", false, "message", "Không thể tạo đơn hàng"));
            }

            // Cập nhật trạng thái giỏ hàng thành "pending" thay vì "ordered"
            for (com.fpt_be.fpt_be.Dto.OrderItemDto item : orderDto.getChiTietDonHang()) {
                // Tìm giỏ hàng với trạng thái "active"
                Optional<Cart> cartItem = cartRepository.findByIdKhachHangAndIdSanPhamAndTrangThai(
                    orderDto.getIdKhachHang(), 
                    item.getProductId(), 
                    "active"
                );
                
                if (cartItem.isPresent()) {
                    Cart cart = cartItem.get();
                    cart.setTrangThai("shipping");
                    cart.setIdDonHang(order.getId());
                    cartRepository.save(cart);
                }
            }
            
            // Trả về thông tin đơn hàng đã tạo
            Map<String, Object> response = new HashMap<>();
            response.put("status", true);
            response.put("message", "Tạo đơn hàng thành công");
            response.put("data", Map.of(
                "id", order.getId(),
                "idKhachHang", order.getIdKhachHang(),
                "tenNguoiNhan", order.getTenNguoiNhan(),
                "sdtNguoiNhan", order.getSdtNguoiNhan(),
                "diaChiGiao", order.getDiaChiGiao(),
                "tongTien", order.getTongTien(),
                "trangThai", "pending",
                "ngayDat", order.getNgayDat()
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/khach-hang/{idKhachHang}/pending")
    public ResponseEntity<?> getPendingDonHangByUserId(@PathVariable Long idKhachHang) {
        try {
            List<Order> orders = orderService.getPendingOrdersByCustomerId(idKhachHang);
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

    @PutMapping("/don-hang/{idDonHang}/trang-thai")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long idDonHang,
            @RequestBody Map<String, String> requestBody,
            @RequestHeader("Authorization") String token) {
        try {
            // Kiểm tra token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(401)
                        .body(Map.of("status", false, "message", "Unauthorized - Token không hợp lệ"));
            }

            String trangThai = requestBody.get("trangThai");
            if (trangThai == null || trangThai.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("status", false, "message", "Trạng thái đơn hàng không được để trống"));
            }

            Order updatedOrder = orderService.updateOrderStatus(idDonHang, trangThai);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", true);
            response.put("message", "Cập nhật trạng thái đơn hàng thành công");
            response.put("data", Map.of(
                "id", updatedOrder.getId(),
                "idKhachHang", updatedOrder.getIdKhachHang(),
                "tenNguoiNhan", updatedOrder.getTenNguoiNhan(),
                "sdtNguoiNhan", updatedOrder.getSdtNguoiNhan(),
                "diaChiGiao", updatedOrder.getDiaChiGiao(),
                "tongTien", updatedOrder.getTongTien(),
                "trangThai", updatedOrder.getTrangThai(),
                "ngayCapNhat", updatedOrder.getNgayCapNhat()
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/ap-dung-ma-giam-gia")
    public ResponseEntity<?> applyDiscountCode(
            @RequestParam String maGiamGia,
            @RequestParam Double tongTien,
            @RequestHeader("Authorization") String token) {
        try {
            // Kiểm tra token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(401)
                        .body(Map.of("status", false, "message", "Unauthorized - Token không hợp lệ"));
            }

            // Áp dụng mã giảm giá
            Map<String, Object> result = cartService.applyDiscountCodeToOrder(null, maGiamGia, tongTien);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/san-pham/{idDonHang}")
    public ResponseEntity<?> getOrderProductDetails(@PathVariable Long idDonHang) {
        try {
            Map<String, Object> orderDetails = orderService.getOrderProductDetails(idDonHang);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy thông tin chi tiết sản phẩm trong đơn hàng thành công",
                            "data", orderDetails));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/don-hang/{idDonHang}/thanh-toan")
    public ResponseEntity<?> updatePaymentStatus(
            @PathVariable Long idDonHang,
            @RequestHeader("Authorization") String token) {
        try {
            // Kiểm tra token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(401)
                        .body(Map.of("status", false, "message", "Unauthorized - Token không hợp lệ"));
            }

            Order updatedOrder = orderService.updatePaymentStatus(idDonHang);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", true);
            response.put("message", "Cập nhật trạng thái thanh toán thành công");
            response.put("data", Map.of(
                "id", updatedOrder.getId(),
                "idKhachHang", updatedOrder.getIdKhachHang(),
                "trangThaiThanhToan", "DA_THANH_TOAN",
                "ngayCapNhat", updatedOrder.getNgayCapNhat()
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
}
