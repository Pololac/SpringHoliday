package com.hb.cda.springholiday.business.impl;

import com.hb.cda.springholiday.business.AccountBusiness;
import com.hb.cda.springholiday.business.exception.UserAlreadyExistsException;
import com.hb.cda.springholiday.entity.User;
import com.hb.cda.springholiday.repository.RefreshTokenRepository;
import com.hb.cda.springholiday.repository.UserRepository;
import com.hb.cda.springholiday.security.jwt.JwtUtil;
import com.hb.cda.springholiday.service.MailService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class AccountBusinessImpl implements AccountBusiness {
    private MailService mailService;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private JwtUtil jwtUtil;

    public AccountBusinessImpl(MailService mailService, PasswordEncoder passwordEncoder, UserRepository userRepository, JwtUtil jwtUtil) {
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public User register(User user) {
        Optional<User> optUser = userRepository.findByEmail(user.getEmail());
        if (optUser.isPresent()) {
            throw new UserAlreadyExistsException();
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setRole("ROLE_USER");
        user.setActive(false);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user, Instant.now().plus(7, ChronoUnit.DAYS));

        mailService.sendEmailValidation(user, token); // Envoi un mail avec le token dans le lien

        return user;
    }

    @Override
    public void activateUser(String token) {
        User user = (User)jwtUtil.validateToken(token); // Le token utilisé pour le lien est valable 7 jours
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void resetPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email " + email));
        String token = jwtUtil.generateToken(user, Instant.now().plusSeconds(3600));
        mailService.sendResetPassword(user, token);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);

        // On supprime tous les refresh tokens du user pour le forcer à se reconnecter
        // sur tous ses devices avec le nouveau MdP
        refreshTokenRepository.deleteByUser(user);
    }

    @Override
    public void deleteAccount(User user) {

    }
}
