package com.hb.cda.springholiday.security;

import com.hb.cda.springholiday.controller.dto.LoginCredentialsDTO;
import com.hb.cda.springholiday.controller.dto.LoginResponseDTO;
import com.hb.cda.springholiday.controller.dto.mapper.UserMapper;
import com.hb.cda.springholiday.entity.RefreshToken;
import com.hb.cda.springholiday.entity.User;

import com.hb.cda.springholiday.repository.RefreshTokenRepository;
import com.hb.cda.springholiday.repository.UserRepository;
import com.hb.cda.springholiday.security.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authManager;

    public AuthServiceImpl(JwtUtil jwtUtil, UserMapper userMapper, RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, AuthenticationManager authManager) {
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.authManager = authManager;
    }

    @Override
    public LoginResponseDTO login (LoginCredentialsDTO credentials) {
        //On authentifie le User avec l'AuthenticationManager de Spring Security
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(),
                            credentials.getPassword()));

        //On récupère le User authentifié
        User user = (User) authentication.getPrincipal();

        // Puis on lui génère un token
        String token = jwtUtil.generateToken(user);

        //On renvoie le token et le user sous forme de DTO
        return new LoginResponseDTO(token, userMapper.convertToDTO(user));
    }

    @Override
    public String generateRefreshToken(String idUser) {
        RefreshToken refreshToken = new RefreshToken();
        //On récupère le user par son identifiant
        User user = userRepository.findById(idUser).orElseThrow();
        //On le lie au refresh token
        refreshToken.setUser(user);
        //On met une durée d'expiration au token
        refreshToken.setExpiresAt(LocalDateTime.now().plus(30, ChronoUnit.DAYS));
        //On le persiste en base de données
        refreshTokenRepository.save(refreshToken);
        //On renvoie son Id
        return refreshToken.getId();
    }

    @Override
    public TokenPair validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findById(token).orElseThrow();
        if(refreshToken.isExpired()) {
            throw new RuntimeException("Refresh token expired");
        }

        User user = refreshToken.getUser();
        refreshTokenRepository.delete(refreshToken);
        String newToken = generateRefreshToken(user.getId());
        String jwt = jwtUtil.generateToken(user);

        return new TokenPair(newToken, jwt);
    }

    // Nettoyage de la table RefreshToken toutes les 24h
    @Transactional
    @Scheduled(fixedDelay = 24, timeUnit = TimeUnit.HOURS)
    void cleanExpiredTokens() {
        refreshTokenRepository.deleteExpiredToken();
    }

}
