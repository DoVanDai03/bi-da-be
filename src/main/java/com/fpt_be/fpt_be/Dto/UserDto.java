package com.fpt_be.fpt_be.Dto;

import java.time.LocalDateTime;

public class UserDto {
    private Long id;
    private String email;
    private String password;
    private String hoVaTen;
    private String sdt;
    private String diaChi;
    private LocalDateTime ngaySinh;
    private String gioiTinh;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
    private Integer isBlock;

    // Default constructor
    public UserDto() {
    }

    public UserDto(Long id, String email, String password, String hoVaTen, String sdt, String diaChi, 
                  LocalDateTime ngaySinh, String gioiTinh, LocalDateTime ngayTao, LocalDateTime ngayCapNhat) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.hoVaTen = hoVaTen;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
        this.isBlock = 1; // Default value is 1 (active)
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

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public LocalDateTime getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDateTime ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
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

    public Integer getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(Integer isBlock) {
        this.isBlock = isBlock;
    }
}
