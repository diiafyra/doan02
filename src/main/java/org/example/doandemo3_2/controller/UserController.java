package org.example.doandemo3_2.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.doandemo3_2.dto.LoginRequest;
import org.example.doandemo3_2.dto.LoginResponse;
import org.example.doandemo3_2.models.User;
import org.example.doandemo3_2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/api/public")
public class UserController {

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

    @PostMapping("/api/user/upload-avatar/{userId}")
    public String uploadAvatar(@PathVariable String userId, @RequestParam("avatar") MultipartFile avatar) throws IOException {
        return userService.uploadAvatar(userId, avatar);
    }

    @GetMapping("/")
    public String homePage(Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("token");
        model.addAttribute("token", token);
        return "home";
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

    @GetMapping("/baiviet")
    public String showBaiViet(Model model) {
        System.out.println("show BaiViet form");
        return "baibao"; // Tên của file baibao.html
    }
}
