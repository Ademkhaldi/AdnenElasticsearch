package com.example.user2.payload.response;


/*Class Declaration: Déclare une classe publique nommée MessageResponse. Cette classe est utilisée pour représenter une réponse simple contenant un message.*/
/*inclut les messages d'erreur lorsqu'une authentification échoue, les confirmations de succès lorsqu'une inscription ou une connexion réussit,
et d'autres messages d'information pertinents.*/



public class MessageResponse {
	private String message;

	public MessageResponse(String message) {
	    this.message = message;
	  }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
