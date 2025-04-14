package com.fpt_be.fpt_be.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietDonHangDto {
    private Long id;
    private Long idKhachHang;
    private Long idDonHang;
    private List<CartWithProductDto> danhSachSanPham;
    private Double tongTien;
    private String trangThaiDonHang;
    private Long idThongKe;
    private String diaChiGiaoHang;
    private String phuongThucThanhToan;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
} 