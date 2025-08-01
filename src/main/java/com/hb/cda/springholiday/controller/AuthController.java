package com.hb.cda.springholiday.controller;

import com.hb.cda.springholiday.controller.dto.LoginCredentialsDTO;
import com.hb.cda.springholiday.controller.dto.LoginResponseDTO;
import com.hb.cda.springholiday.entity.User;
import com.hb.cda.springholiday.security.AuthService;
import com.hb.cda.springholiday.security.TokenPair;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("api/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginCredentialsDTO credentials){
        // On authentifie le User et si OK, on lui génère un JWT (mis dans responseDTO)
        LoginResponseDTO responseDto = authService.login(credentials);

        // On lui génère également un refresh token
        String refreshToken = authService.generateRefreshToken(responseDto.getUser().getId());

        // On crée un cookie dans lequel on stocke le refresh token (voir méthode ci-dessous)
        ResponseCookie refreshCookie = generateCookie(refreshToken);

        // Dans la réponse à la requête de login, on envoie le refresh ds cookie et le jwt dans le body
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(responseDto);
    }

    @PostMapping("/api/refresh-token")
    public ResponseEntity<String> refreshToken(@CookieValue(name = "refresh-token") String token) {
        try {
            // Validation du refresh et, si OK, récupération d'un nouveau JWT et d'un nouveau refresh
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
                .httpOnly(true) //HttpOnly permet que le refresh token ne soit pas manipulable par le JS
                .secure(false)// il faudrait plutôt mettre à true pour qu'il ne soit envoyé qu'en HTTPS, mais le temps du dev on le met à false
                .sameSite(SameSiteCookies.NONE.toString())
                .path("/api/refresh-token") //On indique le chemin sur lequel sera envoyé ce token par le client
                .build();

        return refreshCookie;
    }

}
