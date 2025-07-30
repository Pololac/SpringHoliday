package com.hb.cda.springholiday.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hb.cda.springholiday.security.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class JwtUtil {
    private UserService userService;
    private KeyManager keyManager;

    /**
    * Génère un JWT contenant l'identifiant du user passé en paramètre
    * @param user le User pour lequel on souhaite créer le JWT
    * @return le JWT généré
     */
    public String generateToken(UserDetails user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
                .sign(keyManager.getAlgorithm());
    }

    /**
     * Génère un JWT contenant l'identifiant du user passé en paramètre
     * @param user le User pour lequel on souhaite créer le JWT
     * @param expiration Le temps d'expiration du token (pour la validation par mail par exemple)
     * @return le JWT généré
     */
    public String generateToken(UserDetails user, Instant expiration) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expiration)
                .sign(keyManager.getAlgorithm());
    }

    /**
     * Méthode pour vérifier la validité d'un token et récupérer le User associé au token en question
     * @param token le token en chaîne de caractères
     * @return le User lié au token
     */
    public UserDetails validateToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT
                    .require(keyManager.getAlgorithm())
                    .build()
                    .verify(token);

            String userIdentifier = decodedJWT.getSubject();

            return userService.loadUserByUsername(userIdentifier);
        } catch (JWTVerificationException | UsernameNotFoundException e) {
            throw new AuthorizationDeniedException("Error verifying token") {
            };
        }
    }

}
