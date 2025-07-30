package com.hb.cda.springholiday.service.impl;

import com.hb.cda.springholiday.controller.dto.LoginCredentialsDTO;
import com.hb.cda.springholiday.controller.dto.LoginResponseDTO;
import com.hb.cda.springholiday.controller.dto.mapper.UserMapper;
import com.hb.cda.springholiday.entity.RefreshToken;
import com.hb.cda.springholiday.entity.User;

import com.hb.cda.springholiday.repository.RefreshTokenRepository;
import com.hb.cda.springholiday.repository.UserRepository;
import com.hb.cda.springholiday.security.jwt.JwtUtil;
import com.hb.cda.springholiday.service.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public AuthServiceImpl(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserMapper userMapper, RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public LoginResponseDTO login (LoginCredentialsDTO credentials) {
       Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getEmail(),
                        credentials.getPassword()
                ));

       User user = (User) authentication.getPrincipal();

        // Puis génère le token
        String token = jwtUtil.generateToken(user);

        //Conversion en DTO et construction de la réponse
        return new LoginResponseDTO(token, userMapper.convertToDTO(user));
    }

    @Override
    public String generateRefreshToken(String idUser) {
        RefreshToken refreshToken = new RefreshToken();
        User user = userRepository.findById(idUser).orElseThrow();
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(30));
        refreshTokenRepository.save(refreshToken);

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
