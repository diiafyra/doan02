package org.example.doandemo3_2.repository;

import org.example.doandemo3_2.models.BaiViet;
import org.example.doandemo3_2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable; import java.util.List;
import java.util.Optional;

@Repository
public interface BaiVietRepository extends JpaRepository<BaiViet, Integer> {
    Optional<BaiViet> findBaiVietByIdBaiViet(Integer idBaiViet);
    Optional<List<BaiViet>> findBaiVietsByDanhMuc_IdDanhMuc(Integer idDanhMuc);
}
