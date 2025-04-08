package com.fpt_be.fpt_be.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDto {
    private Long id;
    private String maCode;
    private Double phamTramGiamGia;
    private Double thanhTien;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
} 