package com.fpt_be.fpt_be.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long idKhachHang;
    private Long idDonHang;
    private LocalDateTime ngayThanhToan;
    private Double soTien;
    private String phuongThucThanhToan;
    private String trangThai;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
    
    // VNPay specific fields
    private String vnp_BankCode;
    private String vnp_BankTranNo;
    private String vnp_CardType;
    private String vnp_OrderInfo;
    private String vnp_TransactionNo;
    private String vnp_TransactionStatus;
    private String vnp_SecureHash;
    private String vnp_ResponseCode;
    private String vnp_PayDate;
    private String vnp_TmnCode;
    private String vnp_TransactionType;
    private String vnp_TxnRef;
    private String vnp_Amount;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
        ngayCapNhat = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }
}