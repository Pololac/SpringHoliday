package com.hb.cda.springholiday.security.jwt;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.algorithms.Algorithm;

import jakarta.annotation.PostConstruct;

@Service
public class KeyManager {

    private Algorithm algorithm;
    @Value("${jwt.key.location}") // Variable d'environnement qui indique le dossier où sont stockées les clés (via dossier "env")
    private Path keyLocation;

    //Cette annotation fait que l'initialisation des clés se fera à l'instanciation de cette classe
    // et donc dans ce cas-ci, au lancement de l'application
    @PostConstruct
    private void initialize() throws Exception {
        Path pubFile = keyLocation.resolve("public.key");
        Path privFile = keyLocation.resolve("private.key");
        KeyPair keyPair;

        //Si les fichiers contenant les clés n'existent pas, on génère la paire de clés
        if (Files.notExists(pubFile) || Files.notExists(privFile)) {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            keyPair = generator.generateKeyPair();
            Files.write(pubFile, keyPair.getPublic().getEncoded());
            Files.write(privFile, keyPair.getPrivate().getEncoded());

        } else {
            // Si elles existent déjà
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            //On récupère le contenus des fichiers et on les interprète en instance de clés voulues
            keyPair = new KeyPair(
                    keyFactory.generatePublic( new X509EncodedKeySpec(Files.readAllBytes(pubFile))),
                    keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Files.readAllBytes(privFile)))
            );
        }

        // On utilise les clés en question (existantes ou tout juste générées) pour créer l'algorithme de signature/validation
        algorithm = Algorithm.RSA256(
                (RSAPublicKey)keyPair.getPublic(),
                (RSAPrivateKey)keyPair.getPrivate()
        );
    }

    //On rend accessible cet algorithme
    public Algorithm getAlgorithm() {
        return algorithm;
    }
}

