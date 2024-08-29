package com.example.user2.payload.response;

import java.util.List;

/*JSON Web Token*/
/*La classe JwtResponse est utilisée pour encapsuler les données
de réponse associées à un JSON Web Token (JWT).
Cette classe est typiquement utilisée dans une application pour envoyer des informations sur l'utilisateur après une authentification réussie. */



public class JwtResponse {
	private String token;/*stocker le JWT.
	 */
/*Field Declaration - token: Ce champ contient
le JSON Web Token (JWT) généré après l'authentification.
Le JWT est un jeton d'accès utilisé pour prouver l'identité de l'utilisateur.
 */
	private String type = "Bearer";/*stocker le type de token, initialisé à "Bearer"*/
/*"Bearer" : Dans le contexte des jetons d'accès HTTP,
 "Bearer" est un schéma d'authentification qui indique que le jeton (JWT)dans ce cas
  est utilisé pour l'authentification. Le schéma "Bearer" est utilisé pour spécifier
  que le client doit inclure le jeton dans l'en-tête HTTP Authorization avec le préfixe "Bearer "
  pour accéder à des ressources protégées.







En resumee Le schéma "Bearer" est utilisé pour spécifier que le jeton d'accès doit être envoyé dans l'en-tête Authorization des requêtes HTTP pour l'authentification.*/





	private String id;
	private String username;
	private String email;
	private List<String> roles;

	/*Constructor: Le constructeur initialise un objet JwtResponse avec les valeurs fournies pour token, id, username, email, et roles.*/
	public JwtResponse(String accessToken, String id, String username, String email, List<String> roles) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}

	public String getAccessToken() {
		return token;
	}
/*
getAccessToken() :
 Cette méthode retourne la valeur du champ token,
 qui contient le JSON Web Token (JWT) lui-même.
*/

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}
/*Setter Method for token: Définit la valeur du champ token.
 termes, elle assigne un JWT à l'objet JwtResponse
 */

	public String getTokenType() {
		return type;
	}
/*Getter Method for type: Retourne la valeur du champ type.qui indique le type du jeton. Par défaut, il est défini comme "Bearer".
 */
	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}
/*Setter Method for type: Définit la valeur du champ type.Cela pourrait être utilisé pour spécifier un autre type de jeton, bien que "Bearer" soit le type standard pour les JWT.
 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}
}


/*Cette classe facilite la transmission de ces informations au client de manière structurée et cohérente après une connexion réussie.*/