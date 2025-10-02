package com.fpt_be.fpt_be.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fpt_be.fpt_be.Entity.User;
import com.fpt_be.fpt_be.Repository.UserRepository;

@Service
public class QuenMatKhauService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Value("${app.frontend.reset-password-url:http://localhost:5173/reset-password}")
    private String frontendResetPasswordUrl;

    /**
     * Gửi email reset mật khẩu
     * @param email Email của người dùng
     * @return Thông báo kết quả
     */
    public String sendResetPasswordEmail(String email) {
        // Kiểm tra email có tồn tại không
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Email không tồn tại trong hệ thống");
        }

        // Kiểm tra tài khoản có bị khóa không
        if (user.getIsBlock() == 0) {
            throw new RuntimeException("Tài khoản đã bị khóa, vui lòng liên hệ quản trị viên");
        }

        // Tạo token reset mật khẩu
        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(1); // Token hết hạn sau 1 giờ

        // Lưu token vào database
        user.setResetPasswordToken(resetToken);
        user.setResetPasswordTokenExpires(expiresAt);
        userRepository.save(user);

        // Tạo link reset password
        String resetLink = frontendResetPasswordUrl + "?token=" + resetToken;

        // Gửi email
        emailService.sendResetPasswordEmail(user.getEmail(), user.getHoVaTen(), resetLink);

        return "Email reset mật khẩu đã được gửi đến " + email;
    }

    /**
     * Reset mật khẩu mới
     * @param token Token reset mật khẩu
     * @param newPassword Mật khẩu mới
     * @return Thông báo kết quả
     */
    public String resetPassword(String token, String newPassword) {
        // Kiểm tra token có hợp lệ không
        User user = userRepository.findByResetPasswordToken(token);
        if (user == null) {
            throw new RuntimeException("Token reset mật khẩu không hợp lệ");
        }

        // Kiểm tra token có hết hạn không
        if (user.getResetPasswordTokenExpires() == null || 
            user.getResetPasswordTokenExpires().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token reset mật khẩu đã hết hạn");
        }

        // Kiểm tra tài khoản có bị khóa không
        if (user.getIsBlock() == 0) {
            throw new RuntimeException("Tài khoản đã bị khóa");
        }

        // Validate mật khẩu mới
        if (newPassword == null || newPassword.length() < 6) {
            throw new RuntimeException("Mật khẩu mới phải có ít nhất 6 ký tự");
        }

        // Mã hóa mật khẩu mới
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        // Xóa token reset password
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpires(null);

        // Lưu thay đổi
        userRepository.save(user);

        return "Đặt lại mật khẩu thành công";
    }

    /**
     * Kiểm tra token reset password có hợp lệ không
     * @param token Token cần kiểm tra
     * @return True nếu token hợp lệ, false nếu không
     */
    public boolean validateResetToken(String token) {
        User user = userRepository.findByResetPasswordToken(token);
        if (user == null) {
            return false;
        }

        // Kiểm tra token có hết hạn không
        if (user.getResetPasswordTokenExpires() == null || 
            user.getResetPasswordTokenExpires().isBefore(LocalDateTime.now())) {
            return false;
        }

        // Kiểm tra tài khoản có bị khóa không
        if (user.getIsBlock() == 0) {
            return false;
        }

        return true;
    }

    /**
     * Lấy thông tin user từ token reset password
     * @param token Token reset password
     * @return User object
     */
    public User getUserByResetToken(String token) {
        User user = userRepository.findByResetPasswordToken(token);
        if (user == null) {
            throw new RuntimeException("Token reset mật khẩu không hợp lệ");
        }

        // Kiểm tra token có hết hạn không
        if (user.getResetPasswordTokenExpires() == null || 
            user.getResetPasswordTokenExpires().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token reset mật khẩu đã hết hạn");
        }

        // Kiểm tra tài khoản có bị khóa không
        if (user.getIsBlock() == 0) {
            throw new RuntimeException("Tài khoản đã bị khóa");
        }

        return user;
    }
}