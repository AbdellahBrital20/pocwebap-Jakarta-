package com.euroclear.pocwebap.service;

import java.net.URL;
import java.text.ParseException;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import com.euroclear.pocwebap.config.OAuthConfig;

/**
 * Service pour valider les tokens JWT
 */
public class JwtService {
    
    private ConfigurableJWTProcessor<SecurityContext> jwtProcessor;
    
    public JwtService() {
        try {
            // Créer le processeur JWT
            jwtProcessor = new DefaultJWTProcessor<>();
            
            // Configurer la source JWKS
            JWKSource<SecurityContext> keySource = new RemoteJWKSet<>(new URL(OAuthConfig.JWKS_URL));
            
            // Configurer l'algorithme (RS256 est le plus courant)
            JWSAlgorithm expectedAlgorithm = JWSAlgorithm.RS256;
            JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(expectedAlgorithm, keySource);
            
            jwtProcessor.setJWSKeySelector(keySelector);
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur initialisation JWT Service", e);
        }
    }
    
    /**
     * Valider un token JWT et retourner les claims
     */
    public JWTClaimsSet validateToken(String token) throws Exception {
        return jwtProcessor.process(token, null);
    }
    
    /**
     * Extraire l'identité RACF du token
     */
    public String getRacfId(JWTClaimsSet claims) {
        try {
            String racfId = claims.getStringClaim("racf_user");
            if (racfId == null) {
                racfId = claims.getStringClaim("racf_id");
            }
            if (racfId == null) {
                racfId = claims.getSubject();
            }
            return racfId;
        } catch (ParseException e) {
            return claims.getSubject();
        }
    }
    
    /**
     * Extraire le nom d'utilisateur du token
     */
    public String getUsername(JWTClaimsSet claims) {
        try {
            String username = claims.getStringClaim("preferred_username");
            if (username == null) {
                username = claims.getStringClaim("name");
            }
            if (username == null) {
                username = claims.getSubject();
            }
            return username;
        } catch (ParseException e) {
            return claims.getSubject();
        }
    }
}