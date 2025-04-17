package com.fpt_be.fpt_be.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionDto {
    private Long id;
    private String tenChucVu;
    private Boolean tinhTrang;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
} 