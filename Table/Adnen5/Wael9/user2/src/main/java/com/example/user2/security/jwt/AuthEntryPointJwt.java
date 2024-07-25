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
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
/*Déclaration d'un logger statique pour enregistrer les messages de journalisation. LoggerFactory.getLogger(AuthEntryPointJwt.class) crée un logger pour cette classe.
 */
	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
/*Implémentation de la méthode commence de l'interface AuthenticationEntryPoint.
Cette méthode est appelée chaque fois qu'une exception AuthenticationException est levée.*/
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {/*HttpServletRequest request : la requête HTTP qui a causé l'exception.
	 *//*HttpServletResponse response : la réponse HTTP.
	 *//*AuthenticationException authException : l'exception levée.
	 */
		logger.error("Unauthorized error: {}", authException.getMessage());/*Enregistre un message d'erreur avec le logger, incluant le message de l'exception d'authentification*/
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
	/*Envoie une réponse HTTP avec le code d'état `401 SC_UN


*/

		/*Un logger est un outil utilisé dans les applications pour enregistrer des messages à des fins de diagnostic*/
	}

}
