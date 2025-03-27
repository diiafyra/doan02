package org.example.doandemo3_2.controller;

import org.example.doandemo3_2.dto.CommentReponse;
import org.example.doandemo3_2.dto.CommentRequest;
import org.example.doandemo3_2.dto.UpdateProfileRequest;
import org.example.doandemo3_2.models.User;
import org.example.doandemo3_2.security.JwtTokenProvider;
import org.example.doandemo3_2.service.BinhLuanService;
import org.example.doandemo3_2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BinhLuanService binhLuanService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * API thêm bình luận vào bài viết
     */
    @PostMapping("/comment")
    public ResponseEntity<?> addComment(@RequestBody CommentRequest commentRequest,
                                        @RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ");
        }
        token = token.substring(7);

        try {
            CommentReponse binhLuan = binhLuanService.addComment(commentRequest, token);
            return ResponseEntity.ok(binhLuan);
        } catch (IllegalArgumentException e) {
            CommentReponse errorResponse = new CommentReponse("Failed", "Đăng bình luận thất bại: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Hiển thị trang profile
     */
    @GetMapping("/profile")
    public String getUserProfile(@RequestHeader("Authorization") String token, Model model) {
        if (token == null || !token.startsWith("Bearer ")) {
            return "redirect:/api/public/login?error=no_token";
        }

        token = token.substring(7);

        if (!jwtTokenProvider.validateToken(token)) {
            return "redirect:/api/public/login?error=invalid_token";
        }

        Optional<User> userOptional = jwtTokenProvider.getUserFromToken(token);
        if (userOptional.isEmpty()) {
            return "redirect:/api/public/login?error=user_not_found";
        }

        User user = userOptional.get();
        model.addAttribute("user", user);
        return "profile";
    }

    /**
     * Xác minh mật khẩu hiện tại
     */
    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestHeader("Authorization") String token,
                                            @RequestBody Map<String, String> request) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ");
        }

        token = token.substring(7);

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ hoặc đã hết hạn!");
        }

        Optional<User> userOptional = jwtTokenProvider.getUserFromToken(token);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng!");
        }

        User user = userOptional.get();
        String password = request.get("password");

        Map<String, Object> response = new HashMap<>();
        if (passwordEncoder.matches(password, user.getMatKhau())) {
            response.put("success", true);
        } else {
            response.put("success", false);
            response.put("message", "Mật khẩu không đúng!");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Cập nhật thông tin hồ sơ người dùng
     */
    @PostMapping("/profile/update")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody UpdateProfileRequest request) {
        try {
            // Kiểm tra token có hợp lệ không
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập!");
            }

            String token = authHeader.substring(7);
            if (!jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ hoặc đã hết hạn!");
            }

            Optional<User> userOptional = jwtTokenProvider.getUserFromToken(token);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng!");
            }

            User user = userOptional.get();
            UUID userIdFromToken = user.getIdNguoiDung();

            // Đảm bảo chỉ người dùng chính chủ mới được update
            if (!userIdFromToken.equals(UUID.fromString(request.getIdNguoiDung()))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền sửa thông tin người khác!");
            }

            // Cập nhật thông tin
            user.setTenNguoiDung(request.getTenNguoiDung());
            user.setAvatarUrl(request.getAvatarUrl());

            // Cập nhật mật khẩu nếu có
            if (request.getMatKhau() != null && !request.getMatKhau().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(request.getMatKhau());
                user.setMatKhau(encodedPassword);
            }

            userService.updateUserInfo(user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cập nhật thành công!");

            return ResponseEntity.ok(response);


        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "ID không hợp lệ!");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Cập nhật thất bại: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}