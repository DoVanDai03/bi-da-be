package com.fpt_be.fpt_be.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionPermissionDto {
    private Long id;
    private Long idChucVu;
    private Long idChucNang;
    private Boolean tinhTrang;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
} 