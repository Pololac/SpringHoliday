package com.hb.cda.springholiday.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtUtil {
    //On utilisera le UserService pour récupérer le user correspondant en base de données
    private UserDetailsService userService;
    //On utilise le KeyManager pour récupérer l'algorithme de signature/validation
    private KeyManager keyManager;

    public JwtUtil(UserDetailsService userService, KeyManager keyManager) {
        this.userService = userService;
        this.keyManager = keyManager;
    }

    // Méthode d'implémentation
    /**
     * Génère un JWT contenant l'identifiant du user passé en paramètre
     * @param user le User pour lequel on souhaite créer le JWT
     * @param expiration Le temps d'expiration du token (pour la validation par mail par exemple)
     * @return le JWT généré
     */
    public String generateToken(UserDetails user, Instant expiration) {
        return JWT.create()
                .withSubject(user.getUsername())  // avec le username/email du user en subject dans le payload
                .withExpiresAt(expiration)  //et une date d'expiration paramètrable
                .sign(keyManager.getAlgorithm()); // et on signe ce token avec nos clés
    }

    // Méthode minimale (de base), qui appelle la version longue en lui fournissant la valeur par défaut.
    /**
    * Génère un JWT contenant l'identifiant du user passé en paramètre
    * @param user le User pour lequel on souhaite créer le JWT
    * @return le JWT généré
     */
    public String generateToken(UserDetails user) {
        // 1 seule ligne : on fixe un expiry par défaut (30 min)
        return generateToken(user, Instant.now().plus(30, ChronoUnit.MINUTES));
    }


    /**
     * Méthode pour vérifier la validité d'un token et récupérer le User associé au token en question
     * @param token le token en chaîne de caractères
     * @return le User lié au token
     */
    public UserDetails validateToken(String token) {
        try {
            // On commence par vérifier le token : s'il n'est pas expiré, qu'il est valide, qu'il n'a pas été altéré...
            DecodedJWT decodedJWT = JWT
                    .require(keyManager.getAlgorithm())
                    .build()
                    .verify(token);

            //On récupère l'identifiant du user dans le payload
            String userIdentifier = decodedJWT.getSubject();

            //On utilise le service pour récupérer le user en base de données
            return userService.loadUserByUsername(userIdentifier);

        } catch (JWTVerificationException | UsernameNotFoundException e) {
            //En cas d'erreur de validation ou de chargement du User, on renvoie une erreur 403 Forbidden
            throw new AuthorizationDeniedException("Error verifying token") {};
        }
    }
}
