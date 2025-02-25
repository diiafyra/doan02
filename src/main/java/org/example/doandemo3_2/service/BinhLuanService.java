package org.example.doandemo3_2.service;

import org.example.doandemo3_2.dto.CommentReponse;
import org.example.doandemo3_2.dto.CommentRequest;
import org.example.doandemo3_2.models.BaiViet;
import org.example.doandemo3_2.models.BinhLuan;
import org.example.doandemo3_2.models.User;
import org.example.doandemo3_2.repository.BaiVietRepository;
import org.example.doandemo3_2.repository.BinhLuanRepository;
import org.example.doandemo3_2.repository.UserRepository;
import org.example.doandemo3_2.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service

public class BinhLuanService {

    private final BinhLuanRepository binhLuanRepository;
    private final JwtTokenProvider jwtService;
    @Autowired
    private BaiVietRepository baiVietRepository;

    public BinhLuanService(BinhLuanRepository binhLuanRepository, JwtTokenProvider jwtService) {
        this.binhLuanRepository = binhLuanRepository;
        this.jwtService = jwtService;
    }

    // Thêm bình luận
    public CommentReponse addComment(CommentRequest commentRequest, String token) {
        if (token == null || token.isEmpty() || !jwtService.validateToken(token)) {
            return new CommentReponse("Failed","Token không hợp lệ.  " + token);
        }

        User user = jwtService.getUserFromToken(token).get();
        BaiViet baiViet = baiVietRepository.findBaiVietByIdBaiViet(commentRequest.getBaiVietId()).get();

        if (user == null) {
            return new CommentReponse("Failed", "Không tìm thấy người dùng từ token.");
        } else if (baiViet == null) {
            return new CommentReponse("Failed", "Lỗi sai bài viết");
        }
        BinhLuan binhLuan = new BinhLuan();
        binhLuan.setNoiDung(commentRequest.getContent());
        binhLuan.setNguoiDung(user);
        binhLuan.setBaiViet(baiViet);
        binhLuanRepository.save(binhLuan);
        return new CommentReponse(null, "Đăng bình luận thành công.");

    }

    public List<BinhLuan> getCommentsByBaiVietId(Integer baiVietId) {
        return binhLuanRepository.findBinhLuansByBaiViet_IdBaiViet(baiVietId);
    }
}
