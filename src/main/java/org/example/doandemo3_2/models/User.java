package org.example.doandemo3_2.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "NGUOIDUNG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private UUID idNguoiDung;

    @Column(nullable = false, length = 50, columnDefinition = "NVARCHAR(MAX)")
    private String tenNguoiDung;

    @Column(nullable = false)
    private String matKhau;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrangThaiTaiKhoan trangThaiTaiKhoan = TrangThaiTaiKhoan.CHUA_XAC_THUC;

    @Column
    private String verificationToken;

    @Column
    private String avatarUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime ngayTao;

    // Tạo UUID tự động trước khi lưu vào cơ sở dữ liệu
    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
        if (idNguoiDung == null) {
            idNguoiDung = UUID.randomUUID();
            tenNguoiDung = email.split("@")[0];
        }
    }


    public enum TrangThaiTaiKhoan {
        CHUA_XAC_THUC, DA_XAC_THUC, KHOA_TAI_KHOAN
    }
}
