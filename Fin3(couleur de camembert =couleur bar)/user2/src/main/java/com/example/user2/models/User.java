package com.example.user2.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;
/*HashSet et Set pour gérer les collections de rôles.
Email, NotBlank, Size pour les validations des champs.
Id, DBRef, Document pour les annotations spécifiques à MongoDB.
*/
@Document(collection = "users")
//Document qui indique que la classe est un document MongoDB.
public class User {
  @Id
  private String id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank//Indique que ce champ ne doit pas être vide.
  @Size(max = 50)//l'Limite la taille maximale du champ à 50 caractères.
  @Email//Valide que ce champ est une adresse email valide.
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  @DBRef //Indique que ce champ fait référence à d'autres documents dans une autre collection MongoDB (probablement une collection roles).
  private Set<Role> roles = new HashSet<>();
//HashSet, une collection qui n'autorise pas les doublons.
/*
Champ roles qui est une collection de Role, référencée avec @DBRef pour indiquer qu'il s'agit de références à d'autres documents dans MongoDB. Initialisé comme un HashSet.
*/
  public User() {
    //Constructeur par défaut de la classe User. Il permet de créer un objet User sans initialiser les champs au moment de la création.
  }

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
//constructeur de la classe User qui permet de créer un objet User en initialisant les champs username, email, et password.
  }
/*Constructeurs de la classe : un constructeur par défaut et un constructeur avec les paramètres username, email et password.
 */
  public String getId() {
    return id;
  }
//Méthode getter pour accéder au champ id.
  public void setId(String id) {
    this.id = id;
  }
//Méthode setter pour modifier le champ id.
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
}
/*Les annotations telles que @Document, @Id, et @DBRef sont utilisées pour mapper cette classe à un document MongoDB */


/*Set<Role> : Ne permet pas les doublons. Chaque élément dans un Set est unique. Si vous essayez d'ajouter un élément qui existe déjà, il ne sera pas ajouté de nouveau.
List<Role> : Permet les doublons. Vous pouvez ajouter plusieurs fois le même élément dans une List, et tous seront stockés.*/