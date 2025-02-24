package org.example.doandemo3_2.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    public void sendVerificationEmail(String to, String verificationToken) {
        String subject = "Xác thực tài khoản của bạn";
        String verificationLink = "http://localhost:8080/api/public/verify?token=" + verificationToken;
        String content = "<p>Xin chào,</p>"
                + "<p>Vui lòng nhấn vào link dưới đây để xác thực tài khoản của bạn:</p>"
                + "<p><a href=\"" + verificationLink + "\">Xác thực tài khoản</a></p>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi khi gửi email xác thực");
        }
    }
}
