package org.example.doandemo3_2.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.doandemo3_2.dto.LoginRequest;
import org.example.doandemo3_2.dto.LoginResponse;
import org.example.doandemo3_2.models.BaiViet;
import org.example.doandemo3_2.models.User;
import org.example.doandemo3_2.service.BaiVietService;
import org.example.doandemo3_2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        System.out.println("show registration form");
        return "formDK";
    }

    @PostMapping("/auth/register")
    public String registerUser(User user, Model model) {
        String message = userService.registerUser(user);
        model.addAttribute("message", message);
        System.out.println("register user");
        return "redirect:/api/public/";
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
        return "home";  // trả về view home với danh sách bài viết
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        System.out.println("show login form");
        return "formDN";
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = userService.login(loginRequest);

            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            LoginResponse errorResponse = new LoginResponse("Đăng nhập thất bại: " + e.getMessage(), null, null);
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
        return "baibao";  // Trả về view baibao.html
    }
}
