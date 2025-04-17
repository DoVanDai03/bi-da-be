package com.fpt_be.fpt_be.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "ten_san_pham")
    private String tenSanPham;
    
    @Column(name = "ma_san_pham", unique = true)
    private String maSanPham;
    
    @Column(name = "xuat_xu")
    private String xuatXu;
    
    @Column(name = "bao_hanh")
    private String baoHanh;
    
    @ManyToOne
    @JoinColumn(name = "id_danh_muc")
    private Category danhMuc;
    
    @ManyToOne
    @JoinColumn(name = "id_thuong_hieu")
    private Brand thuongHieu;
    
    @ManyToOne
    @JoinColumn(name = "id_giam_gia")
    private Discount giamGia;
    
    @ManyToOne
    @JoinColumn(name = "id_nha_cung_cap")
    private Supplier nhaCungCap;
    
    @Column(name = "gia_san_pham")
    private Double giaSanPham;
    
    @Column(name = "so_luong_ton_kho")
    private Integer soLuongTonKho;
    
    @Column(name = "kich_co")
    private String kichCo;
    
    @Column(name = "mau_sac")
    private String mauSac;
    
    @Column(name = "chat_lieu")
    private String chatLieu;
    
    @Column(name = "mo_ta")
    private String moTa;
    
    @Column(name = "hinh_anh")
    private String hinhAnh;
    
    @Column(name = "trang_thai")
    private Boolean trangThai;
    
    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;
    
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
        ngayCapNhat = LocalDateTime.now();
        if (trangThai == null) {
            trangThai = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }
} 