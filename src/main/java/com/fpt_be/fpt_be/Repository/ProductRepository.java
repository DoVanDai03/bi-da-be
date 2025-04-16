package com.fpt_be.fpt_be.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpt_be.fpt_be.Entity.Product;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
       
    @Query("SELECT p FROM Product p " +
           "LEFT JOIN FETCH p.danhMuc " +
           "LEFT JOIN FETCH p.thuongHieu " +
           "LEFT JOIN FETCH p.giamGia " +
           "LEFT JOIN FETCH p.nhaCungCap " +
           "WHERE p.id = :id")
    Product findByIdWithJoins(@Param("id") Long id);
    
    @Query("SELECT p FROM Product p " +
           "LEFT JOIN FETCH p.danhMuc " +
           "LEFT JOIN FETCH p.thuongHieu " +
           "LEFT JOIN FETCH p.giamGia " +
           "LEFT JOIN FETCH p.nhaCungCap")
    List<Product> findAllWithJoins();
    
    /**
     * Tìm sản phẩm mới nhất dựa trên ngày tạo
     */
    @Query("SELECT p FROM Product p " +
           "LEFT JOIN FETCH p.danhMuc " +
           "LEFT JOIN FETCH p.thuongHieu " +
           "LEFT JOIN FETCH p.giamGia " +
           "LEFT JOIN FETCH p.nhaCungCap " +
           "WHERE p.trangThai = true " +
           "ORDER BY p.ngayTao DESC")
    List<Product> findNewestProducts(Pageable pageable);
    
    /**
     * Tìm sản phẩm theo danh mục với phân trang
     */
    @Query("SELECT p FROM Product p " +
           "LEFT JOIN FETCH p.danhMuc " +
           "LEFT JOIN FETCH p.thuongHieu " +
           "LEFT JOIN FETCH p.giamGia " +
           "LEFT JOIN FETCH p.nhaCungCap " +
           "WHERE p.trangThai = true " +
           "AND p.danhMuc.id = :categoryId")
    List<Product> findProductsByCategory(@Param("categoryId") Long categoryId, Pageable pageable);
    
    /**
     * Tìm kiếm sản phẩm theo từ khóa (tên hoặc mô tả)
     */
    @Query("SELECT p FROM Product p " +
           "LEFT JOIN FETCH p.danhMuc " +
           "LEFT JOIN FETCH p.thuongHieu " +
           "LEFT JOIN FETCH p.giamGia " +
           "LEFT JOIN FETCH p.nhaCungCap " +
           "WHERE p.trangThai = true " +
           "AND (LOWER(p.tenSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(p.moTa) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Product> searchProductsByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Đếm kết quả tìm kiếm theo từ khóa
     */
    @Query("SELECT COUNT(p) FROM Product p " +
           "WHERE p.trangThai = true " +
           "AND (LOWER(p.tenSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(p.moTa) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    long countSearchResults(@Param("keyword") String keyword);
    
    /**
     * Đếm tổng số sản phẩm đang hoạt động
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.trangThai = true")
    long countActiveProducts();
} 