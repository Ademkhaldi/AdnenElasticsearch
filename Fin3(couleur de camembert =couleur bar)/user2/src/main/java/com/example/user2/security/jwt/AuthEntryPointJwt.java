package com.example.user2.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
/*Annotation @Component pour indiquer que cette classe est un composant Spring.
Déclaration de la classe AuthEntryPointJwt qui implémente AuthenticationEntryPoint.*/
@Component



/*La classe AuthEntryPointJwt implémente l'interface AuthenticationEntryPoint de Spring Security
pour gérer les erreurs d'authentification. Elle est utilisée pour traiter les requêtes qui échouent à l'authentification.*/
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
/*Déclaration d'un logger statique pour enregistrer les messages de journalisation. LoggerFactory.getLogger(AuthEntryPointJwt.class) crée un logger pour cette classe.ce qui est utile pour le débogage et la surveillance.
 */
	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
/*Implémentation de la méthode commence de l'interface AuthenticationEntryPoint.
Cette méthode est appelée chaque fois qu'une exception AuthenticationException est levée.*/
/*Logger et LoggerFactory pour la journalisation.
AuthenticationException pour gérer les exceptions d'authentification.
AuthenticationEntryPoint pour définir le point d'entrée de l'authentification.
Component pour indiquer que cette classe est un composant Spring.*/



	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
/*commence Method: Implémentation de la méthode commence de l'interface AuthenticationEntryPoint.
Cette méthode est appelée lorsqu'une exception d'authentification (AuthenticationException) est levée.
 Elle permet de personnaliser la réponse envoyée au client lorsqu'une tentative d'accès non autorisée se produit.*/
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("Unauthorized error: {}", authException.getMessage());/*Enregistre un message d'erreur avec le logger, incluant le message de l'exception d'authentification*/
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
	/*Envoi d'une réponse HTTP avec le code d'état 401 Unauthorized et un message d'erreur.
	 Ce code indique que la requête nécessite une authentification et que l'accès est refusé en raison de l'absence
	 ou de l'échec de l'authentification.

*/

		/*Un logger est un outil utilisé dans les applications pour enregistrer des messages à des fins de diagnostic*/
	}

}


/*La classe AuthEntryPointJwt est un composant Spring qui gère les erreurs d'authentification dans une application sécurisée avec Spring Security. Elle :

Implémente l'interface AuthenticationEntryPoint pour personnaliser la réponse en cas d'échec d'authentification.
Utilise un logger pour enregistrer les détails de l'erreur d'authentification.
Envoie une réponse HTTP avec le code d'état 401 Unauthorized et un message d'erreur pour informer le client que l'accès est refusé en raison de l'échec de l'authentification.
Ce mécanisme est crucial pour gérer les tentatives d'accès non autorisées de manière appropriée et pour fournir des informations utiles aux développeurs et aux administrateurs système.









*/