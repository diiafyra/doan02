package org.example.doandemo3_2.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "BAIVIET")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaiViet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBaiViet;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String tieuDe;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;

    @ManyToOne
    @JoinColumn(name = "tac_gia")
    private TacGia tacGia;

    @Column(nullable = false, updatable = false)
    private LocalDateTime ngayDang = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "danh_muc")
    private DanhMuc danhMuc;
}

