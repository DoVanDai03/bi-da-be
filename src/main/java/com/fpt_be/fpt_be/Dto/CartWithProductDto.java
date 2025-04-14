package com.fpt_be.fpt_be.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartWithProductDto {
    // Cart fields
    private Long id;
    private Long idKhachHang;
    private Long idSanPham;
    private Long idDonHang;
    private Integer soLuong;
    private Double thanhTien;
    private String trangThai;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
    
    // Product fields
    private String tenSanPham;
    private Double giaSanPham;
    private String kichCo;
    private String mauSac;
    private String chatLieu;
    private String hinhAnh;
    private Integer soLuongTonKho;
    private Long idDanhMuc;
    private String tenDanhMuc;
    private Long idThuongHieu;
    private String tenThuongHieu;
    private Long idGiamGia;
    private Integer phanTramGiamGia;
    private String maCode;
    private String maGiamGia;

    public String getMaGiamGia() {
        return maGiamGia;
    }

    public void setMaGiamGia(String maGiamGia) {
        this.maGiamGia = maGiamGia;
    }
} 