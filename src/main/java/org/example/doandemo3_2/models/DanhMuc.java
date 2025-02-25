package org.example.doandemo3_2.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DANHMUC")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DanhMuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDanhMuc;

    @Column(nullable = false, length = 100, columnDefinition = "NVARCHAR(MAX)")
    private String tenDanhMuc;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String moTa;
}
