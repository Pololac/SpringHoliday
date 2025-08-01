package com.hb.cda.springholiday.business.impl;

import com.hb.cda.springholiday.business.AccountBusiness;
import com.hb.cda.springholiday.business.exception.UserAlreadyExistsException;
import com.hb.cda.springholiday.entity.User;
import com.hb.cda.springholiday.repository.RefreshTokenRepository;
import com.hb.cda.springholiday.repository.UserRepository;
import com.hb.cda.springholiday.security.jwt.JwtUtil;
import com.hb.cda.springholiday.messaging.MailService;
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
        //On vérifie si le User n'existe pas déjà
        if (optUser.isPresent()) {
            throw new UserAlreadyExistsException();
        }
        // On hashe le mot de passe
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        //On lui assigne un rôle par défaut
        user.setRole("ROLE_USER");
        //et on met son compte comme non activé
        user.setActive(false);
        // User persisté en BDD
        userRepository.save(user);

        //On génère un JWT avec notre utilitaire vu précédemment (expiration 7j car validation nécessaire)
        String token = jwtUtil.generateToken(user, Instant.now().plus(7, ChronoUnit.DAYS));

        //On envoie ce JWT dans un lien cliquable au mail indiqué pour le User qu'on a persisté
        mailService.sendEmailValidation(user, token);

        return user;
    }

    @Override
    public void activateUser(String token) {
        //On valide le token envoyé et on en extrait le user
        // (on le cast car le validateToken renvoie un UserDetails
        // et dans ce cas là, on a besoin de la classe concrète pour activer)
        User user = (User)jwtUtil.validateToken(token);
        //On l'active
        user.setActive(true);
        //On met à jour dans la base de données
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
