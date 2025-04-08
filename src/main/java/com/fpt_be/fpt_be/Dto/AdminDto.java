package com.fpt_be.fpt_be.Dto;

import java.time.LocalDateTime;

public class AdminDto {
    private Long id;
    private String email;
    private String password;
    private String hoVaTen;
    private String sdt;
    private String chucVu;
    private String quyenHan;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    // Default constructor
    public AdminDto() {
    }

    public AdminDto(Long id, String email, String password, String hoVaTen, String sdt, String chucVu, 
                  String quyenHan, LocalDateTime ngayTao, LocalDateTime ngayCapNhat) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.hoVaTen = hoVaTen;
        this.sdt = sdt;
        this.chucVu = chucVu;
        this.quyenHan = quyenHan;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHoVaTen() {
        return hoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        this.hoVaTen = hoVaTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getQuyenHan() {
        return quyenHan;
    }

    public void setQuyenHan(String quyenHan) {
        this.quyenHan = quyenHan;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public LocalDateTime getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(LocalDateTime ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }
} 