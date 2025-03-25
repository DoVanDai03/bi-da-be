package com.fpt_be.fpt_be.Dto;

public class UserDto {
    private Long id;
    private String email;
    private String password;
    private String hoVaTen;
    private String sdt;
    private String diaChi;

    public UserDto(Long id, String email, String password, String hoVaTen, String sdt, String diaChi) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.hoVaTen = hoVaTen;
        this.sdt = sdt;
        this.diaChi = diaChi;
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
}
