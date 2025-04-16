package com.fpt_be.fpt_be.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpt_be.fpt_be.Dto.ReviewDto;
import com.fpt_be.fpt_be.Entity.Review;
import com.fpt_be.fpt_be.Entity.User;
import com.fpt_be.fpt_be.Entity.Product;
import com.fpt_be.fpt_be.Repository.ReviewRepository;
import com.fpt_be.fpt_be.Repository.UserRepository;
import com.fpt_be.fpt_be.Repository.ProductRepository;

@Service
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAllWithDetails();
    }

    public Review createReview(ReviewDto reviewDto) {
        Review review = new Review();
        
        // Set user
        User user = userRepository.findById(reviewDto.getIdKhachHang())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với id: " + reviewDto.getIdKhachHang()));
        review.setUser(user);
        
        // Set product
        Product product = productRepository.findById(reviewDto.getIdSanPham())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id: " + reviewDto.getIdSanPham()));
        review.setProduct(product);
        
        review.setDanhGia(reviewDto.getDanhGia());
        review.setHinhAnh(reviewDto.getHinhAnh());
        review.setNgayDanhGia(reviewDto.getNgayDanhGia());
        review.setTrangThai(reviewDto.getTrangThai());
        
        return reviewRepository.save(review);
    }

    public Review updateReview(Long id, ReviewDto reviewDto) {
        Optional<Review> existingReview = reviewRepository.findById(id);
        if (existingReview.isPresent()) {
            Review review = existingReview.get();
            
            // Update user if changed
            if (reviewDto.getIdKhachHang() != null && !reviewDto.getIdKhachHang().equals(review.getUser().getId())) {
                User user = userRepository.findById(reviewDto.getIdKhachHang())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với id: " + reviewDto.getIdKhachHang()));
                review.setUser(user);
            }
            
            // Update product if changed
            if (reviewDto.getIdSanPham() != null && !reviewDto.getIdSanPham().equals(review.getProduct().getId())) {
                Product product = productRepository.findById(reviewDto.getIdSanPham())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id: " + reviewDto.getIdSanPham()));
                review.setProduct(product);
            }
            
            review.setDanhGia(reviewDto.getDanhGia());
            review.setHinhAnh(reviewDto.getHinhAnh());
            review.setNgayDanhGia(reviewDto.getNgayDanhGia());
            review.setTrangThai(reviewDto.getTrangThai());
            
            return reviewRepository.save(review);
        }
        throw new RuntimeException("Không tìm thấy đánh giá với id: " + id);
    }

    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy đánh giá với id: " + id);
        }
        reviewRepository.deleteById(id);
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findByIdWithDetails(id);
    }
    
    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }
    
    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }
    public List<Review> getReviewsForHomePage() {
        return reviewRepository.findByTrangThaiTrueOrderByNgayDanhGiaDesc();
    }

    public Map<String, Object> getReviewsByProduct(Long idSanPham, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviewPage = reviewRepository.findByProductIdOrderByNgayDanhGiaDesc(idSanPham, pageable);
        
        return Map.of(
            "reviews", reviewPage.getContent(),
            "currentPage", page,
            "totalItems", reviewPage.getTotalElements(),
            "totalPages", reviewPage.getTotalPages()
        );
    }
} 