package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.enums.MailType;
import com.grd.gradingbe.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public MailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendLinkEmail(MailType type, String to, String link) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariable("link", link);

            String htmlContent;

            switch (type) {
                case MailType.CHANGE_PASSWORD -> {
                    htmlContent = templateEngine.process("change-password-mail-template", context);
                    helper.setSubject("Đổi mật khẩu");
                }
                case MailType.REGISTRATION -> {
                    htmlContent = templateEngine.process("registration-mail-template", context);
                    helper.setSubject("Xác nhận tài khoản");
                }
                default -> throw new IllegalArgumentException("Mail type is not valid");
            }

            helper.setTo(to);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new MessagingException("Failed to send email", e);
        }

    }
}
