package com.example.user2.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;
/*La classe SignupRequest est utilisée pour encapsuler les données envoyées par un utilisateur lors de son inscription à une application*/
 
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    /*@Size(min = 3, max = 20) : Le nom d'utilisateur doit avoir une longueur comprise entre 3 et 20 caractères.
java*/
    private String username;
 
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
    private Set<String> roles;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
  
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
    
    public Set<String> getRoles() {
      return this.roles;
    }
    
    public void setRole(Set<String> roles) {
      this.roles = roles;
    }
}


/*La classe SignupRequest est un simple POJO (Plain Old Java Object) qui sert à encapsuler les informations d'inscription d'un utilisateur*/


/*POJO: Plain Old Java Object est un terme utilisé pour décrire des objets Java simples, non liés à des frameworks spécifiques ou des bibliothèques, et qui ne suivent aucune convention particulière au-delà de celles imposées par le langage Java lui-même.*/

/*Les POJO sont largement utilisés en Java parce qu'ils sont simples à écrire, comprendre, et utiliser. Ils sont également très flexibles car ils ne sont pas liés à des frameworks spécifiques*/