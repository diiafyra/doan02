package org.example.doandemo3_2.controller;

import org.example.doandemo3_2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/upload-avatar/{userId}")
    public String uploadAvatar(@PathVariable String userId, @RequestParam("avatar") MultipartFile avatar) throws IOException {
        return userService.uploadAvatar(userId, avatar);
    }



}
