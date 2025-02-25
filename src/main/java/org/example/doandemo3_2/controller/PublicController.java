package org.example.doandemo3_2.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.doandemo3_2.dto.UserRequest;
import org.example.doandemo3_2.dto.LoginResponse;
import org.example.doandemo3_2.dto.RegisterResponse;
import org.example.doandemo3_2.models.BaiViet;
import org.example.doandemo3_2.models.BinhLuan;
import org.example.doandemo3_2.models.User;
import org.example.doandemo3_2.service.BaiVietService;
import org.example.doandemo3_2.service.BinhLuanService;
import org.example.doandemo3_2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private BaiVietService baiVietService;

    @Autowired
    private UserService userService;

    @Autowired
    private BinhLuanService binhLuanService;



    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        System.out.println("show registration form");
        return "formDK";
    }

    @PostMapping("/auth/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody User user) {
        try {
            RegisterResponse registerResponse = userService.registerUser(user);
            return ResponseEntity.ok(registerResponse);
        } catch (Exception e) {
            RegisterResponse errorResponse = new RegisterResponse("Failed","Đăng ký thất bại: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("token") String token) {
        String mess = userService.verifyUser(token);
        return "redirect:/api/public/";
    }

    @GetMapping("/")
    public String homePage(Model model, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(0, 10); // Lấy 10 bài viết đầu tiên
        List<BaiViet> baiViets = baiVietService.getAllBaiViets(pageable);
        model.addAttribute("baiViets", baiViets);
        return "home";
    }

    @GetMapping("/login")
    public String showLogin() {
        System.out.println("show login form");
        return "formDN";
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserRequest userRequest) {
        try {
            LoginResponse loginResponse = userService.login(userRequest);

            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            LoginResponse errorResponse = new LoginResponse("Failed","Đăng nhập thất bại: " + e.getMessage(), null, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/baiviet/{id}")
    public String showBaiViet(@PathVariable Integer id, Model model) {
        System.out.println("show BaiViet");
        BaiViet baiViet = baiVietService.getBaiVietById(id).orElse(null);
        System.out.println("lay bai viet");
        if (baiViet != null) {
            model.addAttribute("baiViet", baiViet);
        } else {
            return "redirect:/api/public/";
        }
        return "baibao";
    }
    @GetMapping("/baiViet/{id}/comments")
    public ResponseEntity<?> getComments(@PathVariable("id") Integer baiVietId) {
        List<BinhLuan> comments = binhLuanService.getCommentsByBaiVietId(baiVietId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/test-role")
    public ResponseEntity<?> testRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Current User: " + auth.getName());
        System.out.println("Current Authorities: " + auth.getAuthorities());

        return ResponseEntity.ok("Check console for role info!");
    }


}
