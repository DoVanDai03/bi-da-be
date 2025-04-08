package com.fpt_be.fpt_be.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpt_be.fpt_be.Entity.Review;
import com.fpt_be.fpt_be.Service.ReviewService;

@RestController
@RequestMapping("/api/user/danh-gia")
public class HomeReviewController {

    @Autowired
    private ReviewService reviewService;

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

}