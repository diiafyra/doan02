package org.example.doandemo3_2.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "TACGIA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TacGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTacGia;

    @Column(nullable = false, length = 255, columnDefinition = "NVARCHAR(MAX)")
    private String tenTacGia;

    @Column(nullable = true, length = 255)
    private String email;

    @Column(nullable = true)
    private LocalDateTime ngaySinh;

    @Column(nullable = true, columnDefinition = "NVARCHAR(MAX)")
    private String diaChi;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private User.TrangThaiTaiKhoan trangThaiTaiKhoan = User.TrangThaiTaiKhoan.CHUA_XAC_THUC;

}
