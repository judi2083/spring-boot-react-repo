package com.example.springboot.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import com.example.springboot.config.MailFromProperties;

@Profile("!test")
@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailFromProperties mailFromProperties;

    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            log.info("toEmail::" + toEmail);
            log.info("resetLink::" + resetLink);

            String from = String.format("\"%s\" <%s>",
                    mailFromProperties.getName(),
                    mailFromProperties.getEmail());

            helper.setTo(toEmail);
            helper.setSubject("🔐 Reset Your Password");
            helper.setFrom(from);

            String htmlContent = String.format("""
                <!DOCTYPE html>
                <html>
                <head><meta charset="UTF-8"><title>Password Reset</title></head>
                <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 40px;">
                  <table width="100%%" style="max-width:600px; margin:auto; background-color:#fff; border-radius:10px; box-shadow:0 2px 10px rgba(0,0,0,0.1);">
                    <tr>
                      <td style="padding:30px 40px;">
                        <h2 style="color:#333; text-align:center;">🔒 Password Reset Request</h2>
                        <p style="font-size:16px; color:#555;">Hello,</p>
                        <p style="font-size:16px; color:#555;">
                          We received a request to reset your password. To proceed, please click the button below:
                        </p>
                        <div style="text-align:center; margin:30px 0;">
                          <a href="%s" style="background-color:#4CAF50; color:#fff; padding:14px 28px; text-decoration:none; font-weight:bold; border-radius:5px;">
                            Reset Password
                          </a>
                        </div>
                        <p style="font-size:14px; color:#999;">
                          If you did not request this, please ignore this email. This link will expire in 15 minutes.
                        </p>
                        <hr style="border:none; border-top:1px solid #eee; margin:30px 0;" />
                        <p style="font-size:12px; color:#aaa; text-align:center;">&copy; 2025 MyCompany. All rights reserved.</p>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
            """, resetLink);

            helper.setText(htmlContent, true); // ✅ Send as HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
