package com.fpt_be.fpt_be.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fpt_be.fpt_be.Entity.Payment;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByIdDonHang(Long idDonHang);
    List<Payment> findByTrangThai(String trangThai);
    List<Payment> findByIdKhachHang(Long idKhachHang);
    List<Payment> findByIdKhachHangAndTrangThai(Long idKhachHang, String trangThai);
} 