package org.example.doandemo3_2.service;

import lombok.RequiredArgsConstructor;
import org.example.doandemo3_2.dto.UpdateProfileRequest;
import org.example.doandemo3_2.dto.UserRequest;
import org.example.doandemo3_2.dto.LoginResponse;
import org.example.doandemo3_2.dto.RegisterResponse;
import org.example.doandemo3_2.models.User;
import org.example.doandemo3_2.repository.UserRepository;
import org.example.doandemo3_2.security.JwtTokenProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.example.doandemo3_2.models.User.TrangThaiTaiKhoan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private static final String UPLOAD_DIR = "uploads/avatars/";
    private final JwtTokenProvider jwtTokenProvider;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public RegisterResponse registerUser(User user) {
        System.out.println("Đang xử lý đăng ký cho: " + user.getEmail());  // Kiểm tra xem thông tin có được nhận hay không

        if (userRepository.existsByEmail(user.getEmail())) {
            return new RegisterResponse("Failed", "Email đã tồn tại!");
        }

        user.setMatKhau(passwordEncoder.encode(user.getMatKhau()));

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);

        user.setTrangThaiTaiKhoan(User.TrangThaiTaiKhoan.CHUA_XAC_THUC);
        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());

        return new RegisterResponse(null,"Đăng ký thành công! Kiểm tra email để xác thực tài khoản.");
    }


    public String verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Mã xác thực không hợp lệ!"));

        user.setTrangThaiTaiKhoan(User.TrangThaiTaiKhoan.DA_XAC_THUC);
        user.setVerificationToken(null);
        userRepository.save(user);

        return "Tài khoản đã được xác thực!";
    }


    public LoginResponse login(UserRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return new  LoginResponse("Failed","Tài khoản không tồn tại!", null, null);
        }

        User user = userOptional.get();

        if (user.getTrangThaiTaiKhoan() == TrangThaiTaiKhoan.CHUA_XAC_THUC) {
            return new LoginResponse("Failed","Tài khoản chưa xác thực! Vui lòng kiểm tra email để xác nhận.", null, null);
        }
        if (user.getTrangThaiTaiKhoan() == TrangThaiTaiKhoan.KHOA_TAI_KHOAN) {
            return new LoginResponse("Failed","Tài khoản bị khóa. Vui lòng liên hệ quản trị viên.", null, null);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getMatKhau())) {
            return new LoginResponse("Failed","Sai mật khẩu! Vui lòng thử lại.", null, null);
        }

        System.out.println("Raw Password: " + request.getPassword());
        System.out.println("Encoded Password from DB: " + user.getMatKhau());
        System.out.println("Match result: " + passwordEncoder.matches(request.getPassword(), user.getMatKhau()));


        try {
            System.out.println(request.getEmail());
            String jwt = jwtTokenProvider.generateToken(user.getIdNguoiDung(), "USER");

            System.out.println(jwt);
            return new LoginResponse(null,"Đăng nhập thành công!", jwt, jwtTokenProvider.getRoleFromToken(jwt));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new LoginResponse("Failed","Xác thực thất bại! Vui lòng thử lại.",null,null);
        }
    }


    public void updateUserInfo(User user) {
        userRepository.save(user);
    }

}
