package com.fpt_be.fpt_be.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "positions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(name = "ten_chuc_vu")
    private String tenChucVu;
    
    @Column(name = "tinh_trang")
    private Boolean tinhTrang;
    
    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<Admin> admins;
    
    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<PositionPermission> positionPermissions;
    
    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;
    
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
        ngayCapNhat = LocalDateTime.now();
        if (tinhTrang == null) {
            tinhTrang = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }
}
