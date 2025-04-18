package com.fpt_be.fpt_be.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto {
    private Long id;
    private String tenQuyen;
    private String maQuyen;
    private String moTa;
    private Boolean tinhTrang;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
} 