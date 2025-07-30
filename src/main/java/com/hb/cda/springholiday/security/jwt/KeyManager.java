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
    @Value("${jwt.key.location}") // Indique le dossier où sont stockées les clés (via dossier "env")
    private Path keyLocation;


    @PostConstruct
    private void initialize() throws Exception {
        Path pubFile = keyLocation.resolve("public.key");
        Path privFile = keyLocation.resolve("private.key");
        KeyPair keyPair;

        if (Files.notExists(pubFile) || Files.notExists(privFile)) {

            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            keyPair = generator.generateKeyPair();
            Files.write(pubFile, keyPair.getPublic().getEncoded());
            Files.write(privFile, keyPair.getPrivate().getEncoded());

        } else {

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            keyPair = new KeyPair(
                    keyFactory.generatePublic( new X509EncodedKeySpec(Files.readAllBytes(pubFile))),
                    keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Files.readAllBytes(privFile)))
            );

        }

        // Cet objet sert ensuite à signer (avec la clé privée) et vérifier (avec la clé publique) des JSON Web Tokens.
        algorithm = Algorithm.RSA256(
                (RSAPublicKey)keyPair.getPublic(),
                (RSAPrivateKey)keyPair.getPrivate()
        );
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }
}

