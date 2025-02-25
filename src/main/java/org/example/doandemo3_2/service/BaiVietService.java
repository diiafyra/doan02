package org.example.doandemo3_2.service;

import org.example.doandemo3_2.models.BaiViet;
import org.example.doandemo3_2.repository.BaiVietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BaiVietService {

    @Autowired
    private BaiVietRepository baiVietRepository;

    public Optional<BaiViet> getBaiVietById(Integer idBaiViet) {
        return baiVietRepository.findBaiVietByIdBaiViet(idBaiViet);
    }

    public Optional<List<BaiViet>> getBaiVietsByDanhMuc(Integer idDanhMuc) {
        return baiVietRepository.findBaiVietsByDanhMuc_IdDanhMuc(idDanhMuc);
    }

    public List<BaiViet> getAllBaiViets(Pageable pageable) {
        return baiVietRepository.findAll(pageable).getContent(); // getContent() để lấy danh sách
    }
}
