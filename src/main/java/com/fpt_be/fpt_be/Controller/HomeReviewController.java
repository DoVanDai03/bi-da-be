package com.fpt_be.fpt_be.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpt_be.fpt_be.Entity.Review;
import com.fpt_be.fpt_be.Service.ReviewService;
import com.fpt_be.fpt_be.Service.OrderService;
import com.fpt_be.fpt_be.Dto.ReviewDto;

@RestController
@RequestMapping("/api/user/danh-gia")
public class HomeReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private OrderService orderService;

    /**
     * API lấy danh sách đánh giá cho trang chủ
     */
    @GetMapping("/home-page")
    public ResponseEntity<?> getReviewsForHomePage() {
        try {
            List<Review> reviews = reviewService.getReviewsForHomePage();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách đánh giá thành công",
                            "data", reviews));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    /**
     * API lấy danh sách đánh giá cho một sản phẩm cụ thể
     */
    @GetMapping("/san-pham/{idSanPham}")
    public ResponseEntity<?> getReviewsByProduct(@PathVariable Long idSanPham) {
        try {
            List<Review> reviews = reviewService.getReviewsByProductId(idSanPham);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Lấy danh sách đánh giá theo sản phẩm thành công",
                            "data", reviews));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    /**
     * API tạo đánh giá mới cho sản phẩm (chỉ khi đã mua và nhận hàng thành công)
     */
    @PostMapping("/san-pham/{idSanPham}")
    public ResponseEntity<?> createReview(
            @PathVariable Long idSanPham,
            @RequestBody ReviewDto reviewDto) {
        try {
            // Kiểm tra quyền đánh giá
            Map<String, Object> reviewCheck = orderService.canCustomerReviewProduct(
                reviewDto.getIdKhachHang(), 
                idSanPham
            );
            
            if (!(boolean)reviewCheck.get("canReview")) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "status", false,
                                "message", reviewCheck.get("message")));
            }

            reviewDto.setIdSanPham(idSanPham);
            Review review = reviewService.createReview(reviewDto);
            
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Tạo đánh giá thành công",
                            "data", review));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    /**
     * API cập nhật đánh giá (chỉ người tạo đánh giá mới được phép cập nhật)
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewDto reviewDto) {
        try {
            // Kiểm tra xem đánh giá có tồn tại không và có thuộc về khách hàng này không
            Review existingReview = reviewService.getReviewById(reviewId);
            if (existingReview == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "status", false,
                                "message", "Không tìm thấy đánh giá"));
            }

            // Kiểm tra quyền cập nhật (chỉ người tạo mới được cập nhật)
            if (!existingReview.getUser().getId().equals(reviewDto.getIdKhachHang())) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "status", false,
                                "message", "Bạn không có quyền cập nhật đánh giá này"));
            }

            // Cập nhật đánh giá
            Review updatedReview = reviewService.updateReview(reviewId, reviewDto);
            
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Cập nhật đánh giá thành công",
                            "data", updatedReview));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    /**
     * API xoá đánh giá (chỉ người tạo đánh giá mới được phép xoá)
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId,
            @RequestParam Long idKhachHang) {
        try {
            // Kiểm tra xem đánh giá có tồn tại không và có thuộc về khách hàng này không
            Review existingReview = reviewService.getReviewById(reviewId);
            if (existingReview == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "status", false,
                                "message", "Không tìm thấy đánh giá"));
            }

            // Kiểm tra quyền xoá (chỉ người tạo mới được xoá)
            if (!existingReview.getUser().getId().equals(idKhachHang)) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "status", false,
                                "message", "Bạn không có quyền xoá đánh giá này"));
            }

            // Xoá đánh giá
            reviewService.deleteReview(reviewId);
            
            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", true,
                            "message", "Xoá đánh giá thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
}