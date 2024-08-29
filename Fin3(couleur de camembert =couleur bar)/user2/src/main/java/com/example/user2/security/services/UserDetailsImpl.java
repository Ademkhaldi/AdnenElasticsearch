package com.example.user2.security.services;

import com.example.user2.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;
/*Déclaration de la classe UserDetailsImpl qui implémente UserDetails.
Ajout d'un serialVersionUID pour la sérialisation.*/
	private String id;

	private String username;

	private String email;

	@JsonIgnore
	private String password;
	/*Déclaration des attributs de la classe :
id, username, email pour stocker les informations de l'utilisateur.
password marqué avec @JsonIgnore pour être ignoré lors de la sérialisation JSON.
authorities pour les rôles et permissions de l'utilisateur.*/

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(String id, String username, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}

	public static UserDetailsImpl build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());

		return new UserDetailsImpl(
				user.getId(), 
				user.getUsername(), 
				user.getEmail(),
				user.getPassword(), 
				authorities);
	}
/*En Java, GrantedAuthority est une interface de Spring Security
qui représente une autorité ou un rôle accordé à un utilisateur.
 Elle est utilisée pour décrire les permissions ou les rôles que possède un utilisateur dans le cadre de la sécurité d'application*/
	/*Méthode build pour créer une instance de UserDetailsImpl à partir d'un objet User :
Convertit les rôles de l'utilisateur en objets GrantedAuthority.
Retourne une nouvelle instance de UserDetailsImpl.*/

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	/*Méthodes pour vérifier l'état du compte :
isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled retournent true pour indiquer que le compte est valide.*/

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}

/*Méthode equals pour comparer les objets UserDetailsImpl :
Compare l'objet courant avec un autre objet.
Retourne true si les IDs sont égaux, sinon false.*/
/*Résumé
La classe UserDetailsImpl implémente l'interface UserDetails de Spring Security et représente les informations de l'utilisateur nécessaires pour l'authentification et l'autorisation*/
}
