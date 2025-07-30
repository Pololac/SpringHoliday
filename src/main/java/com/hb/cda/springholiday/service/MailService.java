package com.hb.cda.springholiday.service;

import com.hb.cda.springholiday.entity.User;

public interface MailService {
    void sendEmailValidation(User user, String token);
    void sendResetPassword(User user, String token);
}
