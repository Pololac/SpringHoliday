package com.hb.cda.springholiday.controller;

import com.hb.cda.springholiday.controller.dto.LoginCredentialsDTO;
import com.hb.cda.springholiday.controller.dto.LoginResponseDTO;
import com.hb.cda.springholiday.entity.User;
import com.hb.cda.springholiday.service.AuthService;
import com.hb.cda.springholiday.service.impl.TokenPair;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("api/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginCredentialsDTO credentials){
        LoginResponseDTO responseDto = authService.login(credentials);
        String refreshToken = authService.generateRefreshToken(responseDto.getUser().getId());

        ResponseCookie refreshCookie = generateCookie(refreshToken);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(responseDto);
    }

    @PostMapping("api/refresh-token")
    public ResponseEntity<String> refreshToken(@CookieValue(name = "refresh-token") String token){
        try {
            TokenPair tokens = authService.validateRefreshToken(token);
            ResponseCookie refreshCookie = generateCookie(tokens.getRefreshToken());
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(tokens.getJwt());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid refresh token");
        }
    }

    @GetMapping("api/protected")
    public String protec(@AuthenticationPrincipal User user) {
        System.out.println("hola");
        return user.getEmail();
    }

    private ResponseCookie generateCookie(String refreshToken) {
        ResponseCookie refreshCookie = ResponseCookie.from("refresh-token", refreshToken)
                .httpOnly(true)
                .secure(false)  // Faudrait le mettre à "true" pr qu'il ne soit envoyé qu'en HTTPS, mais le temps du dev on le met en false
                .sameSite(SameSiteCookies.NONE.toString())
                .path("/api/refresh-token")
                .build();

        return refreshCookie;
    }

}
