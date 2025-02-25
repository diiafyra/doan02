package org.example.doandemo3_2.repository;

import org.example.doandemo3_2.models.BinhLuan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BinhLuanRepository extends JpaRepository<BinhLuan, Integer> {

    List<BinhLuan> findBinhLuansByBaiViet_IdBaiViet(int id);

}
