package com.hb.cda.springholiday.security;

import com.hb.cda.springholiday.controller.dto.LoginCredentialsDTO;
import com.hb.cda.springholiday.controller.dto.LoginResponseDTO;

public interface AuthService {
    /**
     * Méthode qui va utiliser le AuthenticationManager pour récupérer le UserDetails et céer le JWT de celui-ci
     * @param credentials les infos de connexion du user (email/password)
     * @return la réponse contenant le token et le user lié à ce token
     */
    LoginResponseDTO login(LoginCredentialsDTO credentials);

    /**
     * Méthode qui va créer un nouveau refresh token et le persister en database
     * @param idUser L'id du user auquel assigner le refresh token
     * @return L'id du token généré
     */
    String generateRefreshToken(String idUser);

    /**
     * Méthode qui vérifie qu'un refresh token exite en BDD, qu'il n'est pas expiré et si OK, génère un JWT
     * , régénère un refresh token et supprime l'ancien refresh token
     * @param token le refresh token à valider
     * @return Les nouveaux refresh et jwt token
     */
    TokenPair validateRefreshToken(String token);
}
