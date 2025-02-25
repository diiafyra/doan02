package org.example.doandemo3_2.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "BINH_LUAN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BinhLuan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBinhLuan;

    @Column(nullable = false, length = 1000, columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;

    @ManyToOne
    @JoinColumn(name = "id_nguoi_dung")
    private User nguoiDung;

    @ManyToOne
    @JoinColumn(name = "id_bai_viet")
    private BaiViet baiViet;

    @Column(nullable = false)
    private LocalDateTime ngayBinhLuan;

    @PrePersist
    protected void onCreate() {
        ngayBinhLuan = LocalDateTime.now();
    }
}
