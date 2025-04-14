package com.fpt_be.fpt_be.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpt_be.fpt_be.Entity.Review;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    @Query("SELECT r FROM Review r " +
           "JOIN FETCH r.user u " +
           "JOIN FETCH r.product p " +
           "WHERE r.id = :id")
    Review findByIdWithDetails(@Param("id") Long id);
    
    @Query("SELECT r FROM Review r " +
           "JOIN FETCH r.user u " +
           "JOIN FETCH r.product p")
    List<Review> findAllWithDetails();
    
    @Query("SELECT r FROM Review r " +
           "JOIN FETCH r.user u " +
           "JOIN FETCH r.product p " +
           "WHERE r.product.id = :productId")
    List<Review> findByProductId(@Param("productId") Long productId);
    
    @Query("SELECT r FROM Review r " +
           "JOIN FETCH r.user u " +
           "JOIN FETCH r.product p " +
           "WHERE r.user.id = :userId")
    List<Review> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT r FROM Review r " +
           "JOIN FETCH r.user u " +
           "JOIN FETCH r.product p " +
           "WHERE r.trangThai = true " +
           "ORDER BY r.ngayDanhGia DESC")
    List<Review> findByTrangThaiTrueOrderByNgayDanhGiaDesc();
    

} 