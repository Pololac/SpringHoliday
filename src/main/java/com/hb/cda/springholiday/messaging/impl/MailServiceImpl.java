package com.hb.cda.springholiday.messaging.impl;

import com.hb.cda.springholiday.entity.User;
import com.hb.cda.springholiday.messaging.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@SpringBootApplication(scanBasePackages = {"com.hb.cda.springholiday.entity", "com.hb.cda.springholiday.repository"})
class MailServiceImpl implements MailService {
    private JavaMailSender mailSender;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmailValidation(User user, String token) {
        String serverUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        String message = """
                    To validate your account click on <a href="%s">this link</a>
                    """
                .formatted(serverUrl+"/api/account/validate/"+token);
        sendMailBase(user.getEmail(), message, "Spring Holiday Email Validation");
    }

    @Override
    public void sendResetPassword(User user, String token) {
        String serverUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        String message = """
                    To reset your password click on <a href="%s">this link</a>
                    """
                .formatted(serverUrl+"/reset-password.html?token="+token);
        sendMailBase(user.getEmail(), message, "Spring Holiday Reset Password");
    }


    private void sendMailBase(String to, String message, String subject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(to);
            helper.setFrom("springholiday@human-booster.fr");
            helper.setSubject(subject);

            helper.setText(message,true); //Temporaire, email à remplacer par un JWT
            mailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            throw new RuntimeException("Unable to send mail", e);
        }
    }

}
