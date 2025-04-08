package com.fpt_be.fpt_be.Request;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DangKyRequest {
    private String email;
    private String password;
    private String hoVaTen;
    private String sdt;
    private String diaChi;
    private LocalDateTime ngaySinh;
    private String gioiTinh;
}
