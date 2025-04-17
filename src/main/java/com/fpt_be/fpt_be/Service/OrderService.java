package com.fpt_be.fpt_be.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fpt_be.fpt_be.Dto.OrderDto;
import com.fpt_be.fpt_be.Entity.Order;
import com.fpt_be.fpt_be.Entity.OrderItem;
import com.fpt_be.fpt_be.Repository.OrderRepository;
import com.fpt_be.fpt_be.Repository.OrderItemRepository;
import com.fpt_be.fpt_be.Dto.CartWithProductDto;
import com.fpt_be.fpt_be.Service.CartService;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartService cartService;

    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(OrderDto orderDto) {
        try {
            // Validate input
            if (orderDto == null) {
                throw new IllegalArgumentException("Thông tin đơn hàng không được để trống");
            }

            if (orderDto.getChiTietDonHang() == null || orderDto.getChiTietDonHang().isEmpty()) {
                throw new IllegalArgumentException("Chi tiết đơn hàng không được để trống");
            }

            // Calculate total product price before discount
            double totalProductPrice = 0.0;
            for (com.fpt_be.fpt_be.Dto.OrderItemDto itemDto : orderDto.getChiTietDonHang()) {
                totalProductPrice += itemDto.getGiaSanPham() * itemDto.getSoLuong();
            }

            // Calculate discount percentage from discount code
            double discountPercentage = 0.0;
            if (orderDto.getMaGiamGia() != null) {
                switch (orderDto.getMaGiamGia()) {
                    case "WELCOME10":
                        discountPercentage = 10.0;
                        break;
                    case "SUMMER25":
                        discountPercentage = 25.0;
                        break;
                    case "FREESHIP50":
                        discountPercentage = 50.0;
                        break;
                    case "VIP30":
                        discountPercentage = 30.0;
                        break;
                    case "FLASH15": 
                        discountPercentage = 15.0;
                        break;
                }
            }

            // Calculate discount on product price
            double discountAmount = (totalProductPrice * discountPercentage) / 100;
            double totalAfterDiscount = totalProductPrice - discountAmount;

            // Add shipping fee after discount
            double shippingFee = 300000.0;
            double finalTotal = totalAfterDiscount + shippingFee;

            // Create order
            Order order = new Order();
            order.setIdKhachHang(orderDto.getIdKhachHang());
            order.setDiaChiGiao(orderDto.getDiaChiGiao());
            order.setSdtNguoiNhan(orderDto.getSdtNguoiNhan());
            order.setTenNguoiNhan(orderDto.getTenNguoiNhan());
            order.setPhuongThucThanhToan(orderDto.getPhuongThucThanhToan());
            order.setMaGiamGia(orderDto.getMaGiamGia());
            order.setNgayDat(LocalDateTime.now());
            order.setTrangThai("pending");
            order.setTongTien(finalTotal);
            
            // Save order
            Order savedOrder = orderRepository.save(order);
            
            if (savedOrder == null || savedOrder.getId() == null) {
                throw new RuntimeException("Không thể tạo đơn hàng");
            }

            // Create order items
            for (com.fpt_be.fpt_be.Dto.OrderItemDto itemDto : orderDto.getChiTietDonHang()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setProductId(itemDto.getProductId());
                orderItem.setQuantity(itemDto.getSoLuong());
                orderItem.setPrice(itemDto.getGiaSanPham());
                orderItem.setDiscount(discountPercentage); // Store the order's discount percentage
                
                // For single item, calculate its proportion of the final total (including shipping)
                double itemRatio = (itemDto.getGiaSanPham() * itemDto.getSoLuong()) / totalProductPrice;
                orderItem.setTotalPrice(finalTotal * itemRatio);
                
                orderItemRepository.save(orderItem);
            }
            
            return savedOrder;
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi tạo đơn hàng: " + e.getMessage());
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


    public List<Order> getOrdersByCustomerId(Long idKhachHang) {
        if (idKhachHang == null) {
            throw new RuntimeException("ID khách hàng không được để trống");
        }
        List<Order> orders = orderRepository.findByIdKhachHang(idKhachHang);
        if (orders == null) {
            return new ArrayList<>();
        }
        return orders;
    }

    public List<Order> getPendingOrdersByCustomerId(Long idKhachHang) {
        return orderRepository.findByIdKhachHang(idKhachHang);
    }

    public Order updateOrder(Long id, OrderDto orderDto) {
        Optional<Order> existingOrder = orderRepository.findById(id);
        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();
            order.setDiaChiGiao(orderDto.getDiaChiGiao());
            order.setSdtNguoiNhan(orderDto.getSdtNguoiNhan());
            order.setTenNguoiNhan(orderDto.getTenNguoiNhan());
            order.setPhuongThucThanhToan(orderDto.getPhuongThucThanhToan());
            order.setMaGiamGia(orderDto.getMaGiamGia());
            order.setTongTien(orderDto.getTongTien());
            order.setNgayCapNhat(LocalDateTime.now());
            
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found with id: " + id);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public Order updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với id: " + orderId));
        
        // Kiểm tra trạng thái hợp lệ
        if (!isValidOrderStatus(newStatus)) {
            throw new RuntimeException("Trạng thái đơn hàng không hợp lệ: " + newStatus);
        }
        
        order.setTrangThai(newStatus);
        order.setNgayCapNhat(LocalDateTime.now());
        return orderRepository.save(order);
    }
    
    private boolean isValidOrderStatus(String status) {
        return status != null && (
            status.equals("pending") ||          // Chờ xử lý
            status.equals("processing") ||       // Đang xử lý
            status.equals("shipped") ||          // Đang giao hàng
            status.equals("delivered") ||        // Đã giao hàng
            status.equals("cancelled") ||        // Đã hủy
            status.equals("returned")   ||         // Đã hoàn trả
            status.equals("completed")
        );
    }

    public Order getOrderById(Long id) {
        if (id == null) {
            throw new RuntimeException("ID đơn hàng không được để trống");
        }
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với id: " + id));
    }

    public List<Map<String, Object>> getAllOrdersWithDetails() {
        List<Order> orders = orderRepository.findAll();
        List<Map<String, Object>> orderDetails = new ArrayList<>();
    
        for (Order order : orders) {
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
    
            // Lấy chi tiết sản phẩm trong đơn hàng từ repository riêng
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
            List<Map<String, Object>> productDetails = new ArrayList<>();
    
            for (OrderItem item : orderItems) {
                Map<String, Object> productDetail = new HashMap<>();
                productDetail.put("id", item.getId());
                productDetail.put("productId", item.getProductId());
                productDetail.put("quantity", item.getQuantity());
                productDetail.put("price", item.getPrice());
                productDetail.put("discount", item.getDiscount());
                productDetail.put("totalPrice", item.getTotalPrice());
                productDetails.add(productDetail);
            }
    
            orderDetail.put("chiTietDonHang", productDetails);
            orderDetails.add(orderDetail);
        }
    
        return orderDetails;
    }
    

    public Map<String, Object> canCustomerReviewProduct(Long customerId, Long productId) {
        try {
            // Bước 1: Kiểm tra xem khách hàng có đơn hàng nào không
            List<Order> customerOrders = orderRepository.findByIdKhachHang(customerId);
            if (customerOrders == null || customerOrders.isEmpty()) {
                return Map.of(
                    "canReview", false,
                    "message", "Bạn chưa có đơn hàng nào"
                );
            }

            // Bước 2: Kiểm tra xem trong đơn hàng có sản phẩm cần đánh giá không
            List<Order> ordersWithProduct = orderRepository.findOrdersByCustomerAndProduct(customerId, productId);
            if (ordersWithProduct == null || ordersWithProduct.isEmpty()) {
                return Map.of(
                    "canReview", false,
                    "message", "Bạn chưa mua sản phẩm này nên không thể đánh giá"
                );
            }

            // Bước 3: Kiểm tra xem có đơn hàng nào đã completed chứa sản phẩm này không
            List<Order> completedOrders = orderRepository.findCompletedOrdersByCustomerAndProduct(customerId, productId);
            if (completedOrders == null || completedOrders.isEmpty()) {
                return Map.of(
                    "canReview", false,
                    "message", "Bạn cần đợi đơn hàng được giao thành công mới có thể đánh giá"
                );
            }

            // Tất cả điều kiện đã thỏa mãn
            return Map.of(
                "canReview", true,
                "message", "Bạn có thể đánh giá sản phẩm này"
            );

        } catch (Exception e) {
            System.out.println("Lỗi khi kiểm tra quyền đánh giá: " + e.getMessage());
            return Map.of(
                "canReview", false,
                "message", "Có lỗi xảy ra khi kiểm tra quyền đánh giá"
            );
        }
    }

    public Map<String, Object> getOrderProductDetails(Long orderId) {
        try {
            // Get the order
            Order order = getOrderById(orderId);
            if (order == null) {
                throw new RuntimeException("Không tìm thấy đơn hàng với id: " + orderId);
            }

            // Get order items with product details
            List<CartWithProductDto> orderItems = cartService.getCartsWithProductInfoByOrderId(orderId);

            // Create response object
            Map<String, Object> response = new HashMap<>();
            response.put("id", order.getId());
            response.put("idKhachHang", order.getIdKhachHang());
            response.put("tenNguoiNhan", order.getTenNguoiNhan());
            response.put("sdtNguoiNhan", order.getSdtNguoiNhan());
            response.put("diaChiGiao", order.getDiaChiGiao());
            response.put("tongTien", order.getTongTien());
            response.put("trangThai", order.getTrangThai());
            response.put("phuongThucThanhToan", order.getPhuongThucThanhToan());
            response.put("maGiamGia", order.getMaGiamGia());
            response.put("ngayDat", order.getNgayDat());
            response.put("ngayCapNhat", order.getNgayCapNhat());

            // Add product details
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
                
                // Add category information if available
                if (item.getIdDanhMuc() != null) {
                    productDetail.put("idDanhMuc", item.getIdDanhMuc());
                    productDetail.put("tenDanhMuc", item.getTenDanhMuc());
                }
                
                // Add brand information if available
                if (item.getIdThuongHieu() != null) {
                    productDetail.put("idThuongHieu", item.getIdThuongHieu());
                    productDetail.put("tenThuongHieu", item.getTenThuongHieu());
                }
                
                // Add discount information if available
                if (item.getIdGiamGia() != null) {
                    productDetail.put("idGiamGia", item.getIdGiamGia());
                    productDetail.put("phanTramGiamGia", item.getPhanTramGiamGia());
                    productDetail.put("maGiamGia", item.getMaGiamGia());
                }
                
                productDetails.add(productDetail);
            }
            response.put("chiTietSanPham", productDetails);

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy thông tin chi tiết đơn hàng: " + e.getMessage());
        }
    }

    @Transactional
    public Order updatePaymentStatus(Long orderId) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với id: " + orderId));

            // Cập nhật trạng thái thanh toán
            order.setTrangThaiThanhToan("DA_THANH_TOAN");
            order.setNgayCapNhat(LocalDateTime.now());

            return orderRepository.save(order);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật trạng thái thanh toán: " + e.getMessage());
        }
    }
}
