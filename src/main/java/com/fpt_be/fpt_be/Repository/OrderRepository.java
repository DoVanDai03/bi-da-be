package com.fpt_be.fpt_be.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fpt_be.fpt_be.Entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByIdKhachHang(Long idKhachHang);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.trangThai = :status AND o.ngayTao BETWEEN :startDate AND :endDate")
    int countByStatusAndCreatedAtBetween(@Param("status") String status, 
                                        @Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.ngayTao BETWEEN :startDate AND :endDate")
    int countByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(o.tongTien), 0) FROM Order o WHERE o.ngayTao BETWEEN :startDate AND :endDate")
    double sumRevenueByDateRange(@Param("startDate") LocalDateTime startDate, 
                                @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(o.tongTien), 0) FROM Order o WHERE DATE(o.ngayTao) = :date")
    double sumRevenueByDate(@Param("date") LocalDate date);
}
