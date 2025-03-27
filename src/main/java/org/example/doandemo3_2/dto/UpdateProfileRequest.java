package org.example.doandemo3_2.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String idNguoiDung;
    private String tenNguoiDung;
    private String avatarUrl;
    private String matKhau; // Thêm trường mật khẩu
}