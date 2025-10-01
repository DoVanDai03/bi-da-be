package com.fpt_be.fpt_be.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationEmail(String to, String name) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Đăng ký tài khoản thành công");
            String bodyHtml = "<p>Chào <strong>" + name + "</strong>,</p>"
                + "<p>Bạn đã đăng ký tài khoản thành công! Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.</p>";
            String htmlContent = buildEmailTemplate(
                "Đăng ký thành công",
                bodyHtml,
                null,
                null
            );
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email xác nhận đăng ký: " + e.getMessage());
        }
    }

    public void sendVerificationEmail(String to, String name, String verificationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Xác minh tài khoản của bạn");
            String bodyHtml = "<p>Chào <strong>" + name + "</strong>,</p>"
                + "<p>Vui lòng nhấp vào nút bên dưới để xác minh và kích hoạt tài khoản của bạn.</p>"
                + "<p style=\"margin-top: 16px; color: #555; font-size: 13px;\">Liên kết sẽ hết hạn sau 24 giờ.</p>";
            String htmlContent = buildEmailTemplate(
                "Xác minh tài khoản",
                bodyHtml,
                "Xác minh tài khoản",
                verificationLink
            );
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email xác minh: " + e.getMessage());
        }
    }

    private String buildEmailTemplate(String title, String bodyHtml, String buttonText, String buttonLink) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html lang=\"vi\">\n<head>\n<meta charset=\"UTF-8\"/>\n<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>");
        sb.append("<title>").append(title).append("</title>\n</head>\n<body style=\"margin:0; background:#f5f7fb;\">");
        sb.append("<table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"background:#f5f7fb; padding:24px 12px;\">");
        sb.append("<tr><td align=\"center\">\n");
        sb.append("  <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"max-width:560px; background:#ffffff; border-radius:12px; box-shadow:0 4px 20px rgba(0,0,0,0.06); overflow:hidden; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, 'Noto Sans', 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'Noto Color Emoji', sans-serif; color:#1f2937;\">");
        // Header
        sb.append("    <tr><td style=\"background:linear-gradient(135deg,#4f46e5,#7c3aed); padding:24px 28px; color:#ffffff;\">");
        sb.append("      <div style=\"font-size:18px; font-weight:600; letter-spacing:.3px;\">Xbilliard - Store</div>");
        sb.append("      <div style=\"font-size:14px; opacity:.95; margin-top:6px;\">").append(title).append("</div>");
        sb.append("    </td></tr>");
        // Body
        sb.append("    <tr><td style=\"padding:28px;\">");
        sb.append("      ").append(bodyHtml);
        if (buttonLink != null && !buttonLink.isEmpty() && buttonText != null && !buttonText.isEmpty()) {
            sb.append("      <div style=\"margin-top:24px;\">");
            sb.append("        <a href=\"").append(buttonLink).append("\" style=\"display:inline-block; padding:12px 18px; background:#4f46e5; color:#ffffff; text-decoration:none; border-radius:8px; font-weight:600;\">").append(buttonText).append("</a>");
            sb.append("      </div>");
        }
        sb.append("      <div style=\"margin-top:28px; font-size:12px; color:#6b7280; line-height:1.5;\">Nếu bạn không yêu cầu email này, vui lòng bỏ qua.</div>");
        sb.append("    </td></tr>");
        // Footer
        sb.append("    <tr><td style=\"padding:18px 28px; border-top:1px solid #eef2f7; background:#fafbff; color:#6b7280; font-size:12px;\">");
        sb.append("      © ").append(java.time.Year.now()).append(" Xbilliard - Store. Bảo lưu mọi quyền.");
        sb.append("    </td></tr>");
        sb.append("  </table>\n");
        sb.append("</td></tr></table>\n");
        sb.append("</body></html>");
        return sb.toString();
    }
}
