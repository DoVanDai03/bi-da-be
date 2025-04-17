package com.fpt_be.fpt_be.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String tenSanPham;
    private String maSanPham;
    private String xuatXu;
    private String baoHanh;
    private Long idDanhMuc;
    private Long idThuongHieu;
    private Long idGiamGia;
    private Long idNhaCungCap;
    private Double giaSanPham;
    private Integer soLuongTonKho;
    private String kichCo;
    private String mauSac;
    private String chatLieu;
    private String moTa;
    private String hinhAnh;
    private Boolean trangThai;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
} 