package com.fpt_be.fpt_be.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "position_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PositionPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chuc_vu")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private Position position;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chuc_nang")
    @EqualsAndHashCode.Exclude
    private Permission permission;
    
    @Column(name = "tinh_trang")
    private Boolean tinhTrang;
    
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
