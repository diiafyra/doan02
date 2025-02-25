package org.example.doandemo3_2.controller;

import lombok.RequiredArgsConstructor;
import org.example.doandemo3_2.dto.CommentReponse;
import org.example.doandemo3_2.dto.CommentRequest;
import org.example.doandemo3_2.dto.CommentReponse;
import org.example.doandemo3_2.models.BinhLuan;
import org.example.doandemo3_2.models.User;
import org.example.doandemo3_2.service.BaiVietService;
import org.example.doandemo3_2.service.BinhLuanService;
import org.example.doandemo3_2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.io.IOException;

@Controller
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private BaiVietService baiVietService;
    @Autowired
    private BinhLuanService binhLuanService;


    @PostMapping("/upload-avatar/{userId}")
    public String uploadAvatar(@PathVariable String userId, @RequestParam("avatar") MultipartFile avatar) throws IOException {
        return userService.uploadAvatar(userId, avatar);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> addComment(@RequestBody CommentRequest commentRequest, @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            CommentReponse binhLuan = binhLuanService.addComment(commentRequest, token);
            return ResponseEntity.ok(binhLuan);
        } catch (IllegalArgumentException e) {
            CommentReponse errorResponse = new CommentReponse("Failed","Đăng bình luận thất bại" + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }



}
