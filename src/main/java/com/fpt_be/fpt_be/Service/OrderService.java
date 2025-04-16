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

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;


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
            order.setTongTien(orderDto.getTongTien());
            
            // Save order
            Order savedOrder = orderRepository.save(order);
            
            if (savedOrder == null || savedOrder.getId() == null) {
                throw new RuntimeException("Không thể tạo đơn hàng");
            }

            // // Create order items
            // for (com.fpt_be.fpt_be.Dto.OrderItemDto itemDto : orderDto.getChiTietDonHang()) {
            //     OrderItem orderItem = new OrderItem();
            //     orderItem.setOrder(savedOrder);
            //     orderItem.setProductId(itemDto.getProductId());
            //     orderItem.setQuantity(itemDto.getQuantity());
            //     orderItem.setPrice(itemDto.getPrice());
            //     orderItem.setDiscount(itemDto.getDiscount());
            //     orderItem.setTotalPrice(itemDto.getTotalPrice());
            //     orderItemRepository.save(orderItem);
            // }
            
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
            
            // Lấy chi tiết sản phẩm trong đơn hàng
            List<OrderItem> orderItems = order.getChiTietDonHang();
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
}
