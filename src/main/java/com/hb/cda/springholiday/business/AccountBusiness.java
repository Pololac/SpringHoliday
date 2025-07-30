package com.hb.cda.springholiday.business;

import com.hb.cda.springholiday.entity.User;

public interface AccountBusiness {
    /**
     * Méthode d'inscription d'un nouveau user qui va assigner un rôle par défaut, hasher
     * le mot de passe, faire persister le user et envoyer un mail de validation contenant un token
     * @param user Le nouveau user à faire persister
     * @return Le user persisté
     */
    User register(User user);
    /**
     * Méthode qui va valider et décoder un token, en extraire un User et passer son compte
     * à active dans la base de données
     * @param token
     */
    void activateUser(String token);
    /**
     * Méthode qui va envoyer un lien avec un token permettant de créer un nouveau mot
     * de passe
     * @param email Le mail de la personne qui souhaite réinitialiser son mot de passe
     */
    void resetPassword(String email);

    /**
     * Méthode qui va hasher le nouveau mot de passe du user et
     * lui assigner avant de le faire persister
     * @param user Le User souhaitant modifier son mot de passe
     * @param newPassword Le nouveau mot de passe du User
     */
    void updatePassword(User user, String newPassword);

    /**
     * Méthode permettant à un user de supprimer son compte, en accordance avec le RGPD
     * @param user Le user souhaitant supprimer son compte
     */
    void deleteAccount(User user);

}
