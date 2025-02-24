package org.example.doandemo3_2.service;

import lombok.RequiredArgsConstructor;
import org.example.doandemo3_2.dto.LoginRequest;
import org.example.doandemo3_2.dto.LoginResponse;
import org.example.doandemo3_2.models.User;
import org.example.doandemo3_2.repository.UserRepository;
import org.example.doandemo3_2.security.JwtTokenProvider;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    public String registerUser(User user) {
        System.out.println("Đang xử lý đăng ký cho: " + user.getEmail());  // Kiểm tra xem thông tin có được nhận hay không

        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email đã tồn tại!";
        }

        user.setMatKhau(passwordEncoder.encode(user.getMatKhau()));

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);

        user.setTrangThaiTaiKhoan(User.TrangThaiTaiKhoan.CHUA_XAC_THUC);
        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());

        return "Đăng ký thành công! Kiểm tra email để xác thực tài khoản.";
    }


    public String verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Mã xác thực không hợp lệ!"));

        user.setTrangThaiTaiKhoan(User.TrangThaiTaiKhoan.DA_XAC_THUC);
        user.setVerificationToken(null);
        userRepository.save(user);

        return "Tài khoản đã được xác thực!";
    }

    public String uploadAvatar(String userId, MultipartFile avatar) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));

        if (avatar != null && !avatar.isEmpty()) {
            String avatarFileName = UUID.randomUUID().toString() + "_" + avatar.getOriginalFilename();
            Path avatarPath = Paths.get(UPLOAD_DIR + avatarFileName);
            Files.createDirectories(avatarPath.getParent()); // Tạo thư mục nếu chưa có
            avatar.transferTo(avatarPath); // Lưu ảnh vào thư mục
            user.setAvatarUrl(avatarPath.toString()); // Lưu đường dẫn ảnh vào cơ sở dữ liệu
            userRepository.save(user);
            return "Ảnh đại diện đã được tải lên thành công!";
        }
        return "Không có ảnh đại diện để tải lên!";
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Tài khoản không tồn tại!");
        }

        User user = userOptional.get();

        if (user.getTrangThaiTaiKhoan() == TrangThaiTaiKhoan.CHUA_XAC_THUC) {
            throw new RuntimeException("Tài khoản chưa xác thực! Vui lòng kiểm tra email để xác nhận.");
        }
        if (user.getTrangThaiTaiKhoan() == TrangThaiTaiKhoan.KHOA_TAI_KHOAN) {
            throw new RuntimeException("Tài khoản bị khóa. Vui lòng liên hệ quản trị viên.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getMatKhau())) {
            throw new RuntimeException("Sai mật khẩu! Vui lòng thử lại.");
        }

        System.out.println("Raw Password: " + request.getPassword());
        System.out.println("Encoded Password from DB: " + user.getMatKhau());
        System.out.println("Match result: " + passwordEncoder.matches(request.getPassword(), user.getMatKhau()));


        try {
            System.out.println(request.getEmail());
            String jwt = jwtTokenProvider.generateToken(request.getEmail(), "USER");

            System.out.println(jwt);
            return new LoginResponse("Đăng nhập thành công!", jwt, jwtTokenProvider.getRoleFromToken(jwt));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Xác thực thất bại! Vui lòng thử lại.");
        }
    }
}
