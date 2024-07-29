package com.example.user2.payload.request;

import jakarta.validation.constraints.NotBlank;
/*Déclaration de la classe LoginRequest, utilisée pour encapsuler les données de demande de connexion*/
public class LoginRequest {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
