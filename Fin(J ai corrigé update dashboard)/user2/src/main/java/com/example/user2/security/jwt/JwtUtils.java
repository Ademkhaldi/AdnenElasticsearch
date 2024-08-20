package com.example.user2.security.jwt;

import com.example.user2.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
/*Déclaration d'un logger statique pour enregistrer les messages de journalisation.
 */
  @Value("${user.app.jwtSecret}")
  private String jwtSecret;

  @Value("${user.app.jwtExpirationMs}")
  private int jwtExpirationMs;
  /*Injection des valeurs de configuration pour le secret JWT (jwtSecret) et la durée d'expiration (jwtExpirationMs).
   */

  public String generateJwtToken(Authentication authentication) {

    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    return Jwts.builder()
        .setSubject((userPrincipal.getUsername()))
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }
  /*Méthode generateJwtToken pour générer un token JWT :
Obtient les détails de l'utilisateur à partir de l'objet Authentication.
Construit un token JWT avec le nom d'utilisateur (setSubject), la date de création (setIssuedAt), et la date d'expiration (setExpiration).
Signe le token avec la clé secrète en utilisant l'algorithme HS256 (signWith).
Retourne le token JWT généré.
*/
  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }
/*Méthode key pour obtenir la clé de signature JWT :
Décode le secret JWT en Base64 et crée une clé HMAC SHA.
Retourne la clé.
*/
  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
               .parseClaimsJws(token).getBody().getSubject();
  }
/*Méthode getUserNameFromJwtToken pour extraire le nom d'utilisateur du token JWT :
Utilise la clé de signature pour parser le token et extraire le corps des réclamations (claims).
Retourne le sujet (subject) du token, qui est le nom d'utilisateur.
*/
  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
/*
  Méthode validateJwtToken pour valider le token JWT :
  Tente de parser le token avec la clé de signature.
  Si le parsing réussit, retourne true.
  En cas d'exception, enregistre un message d'erreur approprié et retourne false.
*/
/*Résumé
La classe JwtUtils gère les opérations principales relatives aux tokens JWT :

Génération de tokens JWT.
Extraction du nom d'utilisateur des tokens JWT.
Validation des tokens JWT.
Utilisation d'une clé secrète pour signer et vérifier les tokens JWT.*/
}


/*Un parser est un programme ou une fonction qui lit
 une séquence d'entrées (texte, code source, données, etc.) et convertit cette séquence
 en une structure de données plus significative ou en un format que le système peut utiliser*/

/*Analyse de Langages de Programmation
* Analyse de Fichiers de Données
 * Traitement du Langage Naturel
 * */