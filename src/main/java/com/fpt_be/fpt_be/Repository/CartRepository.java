package com.fpt_be.fpt_be.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpt_be.fpt_be.Entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // Find carts by customer ID
    java.util.List<Cart> findByIdKhachHang(Long idKhachHang);
    
    // Find cart by customer ID and product ID
    java.util.Optional<Cart> findByIdKhachHangAndIdSanPham(Long idKhachHang, Long idSanPham);
    
    // Find carts by order ID
    java.util.List<Cart> findByIdDonHang(Long idDonHang);

    java.util.Optional<Cart> findByIdDonHangAndIdSanPham(Long idDonHang, Long idSanPham);

    java.util.Optional<Cart> findByIdKhachHangAndIdSanPhamAndTrangThai(Long idKhachHang, Long idSanPham, String trangThai);

    java.util.List<Cart> findByIdKhachHangAndTrangThai(Long idKhachHang, String trangThai);
}
