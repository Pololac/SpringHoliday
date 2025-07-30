package com.hb.cda.springholiday.service.impl;

import com.hb.cda.springholiday.entity.User;
import com.hb.cda.springholiday.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@Service
class MailServiceImpl implements MailService {
    private JavaMailSender mailSender;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmailValidation(User user, String token) {
        // Pour récupérer dynamiquement l’URL de base de votre application (schéma, hôte, port et context‐path)
        String serverUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(user.getEmail());
            helper.setFrom("springholiday@human-booster.fr");
            helper.setSubject("SpringHoliday Email Validation");

            helper.setText("""
                    To validate your account click on <a href="%s">this link</a>
                    """
                    .formatted(serverUrl+"/api/account/validate/" + token),true); //Url du lien
            mailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            throw new RuntimeException("Unable to send mail", e);
        }
    }

    @Override
    public void sendResetPassword(User user, String token) {
        String serverUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(user.getEmail());
            helper.setFrom("springholiday@human-booster.fr");
            helper.setSubject("SpringHoliday Reset Password");

            helper.setText("""
                    To reset your password, click on <a href="%s">this link</a>
                    """
                    .formatted(serverUrl+"/reset-password.html?token=" + token),true); //Url du lien
            mailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            throw new RuntimeException("Unable to send mail", e);
        }
    }

}
