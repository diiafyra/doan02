//package org.example.doandemo3_2.controller;
//
//import org.example.doandemo3_2.models.BaiViet;
//import org.example.doandemo3_2.service.BaiVietService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/api/public")
//public class BaiVietController {
//
//    @Autowired
//    private BaiVietService baiVietService; // Đảm bảo bạn có service để lấy bài viết từ cơ sở dữ liệu
//
//    @GetMapping("/BaiViet")
//    public String showBaiViet(Model model) {
//        return "baibao"; // Tên của file baibao.html
//    }
//}
