package com.example.user2.controllers;


import com.example.user2.Service.IUserService;
import com.example.user2.models.ERole;
import com.example.user2.models.User;
import com.example.user2.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@CrossOrigin()
@RestController
@FeignClient(name = "APIgateway")
@RequestMapping("/user2")
public class UserController {


  @Autowired
    private IUserService userService;


  @GetMapping("/retrieve")
 /*@AuthenticationPrincipal UserDetailsImpl userDetails : Injecte les détails de l'utilisateur actuellement authentifié.
  */
public ResponseEntity<?> getUsers(@AuthenticationPrincipal UserDetailsImpl userDetails) {
/*Vérifie si l'utilisateur a le rôle ADMIN ou non en itérant sur les autorités de l'utilisateur.
Si l'utilisateur est un ADMIN, il pourra voir tous les utilisateurs ; sinon, il pourra uniquement voir ses propres informations.*/

Boolean exist = Boolean.FALSE;
   /*Boucle for avec Iterator : Cette boucle itère sur les autorités (rôles) de l'utilisateur. Elle vérifie si l'une des autorités est ROLE_ADMIN.*/



    for (Iterator<? extends GrantedAuthority> it = userDetails.getAuthorities().iterator(); it.hasNext(); ) {
     /*Condition if(String.valueOf(it.next()) == ERole.ROLE_ADMIN.name()): Cette condition compare les autorités de l'utilisateur avec le rôle ROLE_ADMIN. */
      if(String.valueOf(it.next())== ERole.ROLE_ADMIN.name())
        exist=Boolean.TRUE;
     // break;
      else {
        // Regular user can see only their own user details
        User user = userService.retrieveUser(userDetails.getId());
        //return new ResponseEntity<>(Collections.singletonList(user), HttpStatus.OK);
      }
    }
/*En fonction du rôle de l'utilisateur, renvoie tous les utilisateurs ou uniquement les détails de l'utilisateur actuel.
 */
/*Condition if (exist): Cette condition vérifie la variable exist qui aurait dû être définie dans le bloc de code précédent. Si exist est true, cela signifie que l'utilisateur a le rôle ROLE_ADMIN.
Retour de la réponse:
Si exist est vrai, la méthode renvoie tous les utilisateurs.
Sinon, elle renvoie uniquement les détails de l'utilisateur courant.*/

  if (exist) {
    // Admin can see all users
    return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
  } else {
    // Regular user can see only their own user details
    User user = userService.retrieveUser(userDetails.getId());
    return new ResponseEntity<>(Collections.singletonList(user), HttpStatus.OK);
  }
}



  @DeleteMapping("/DeleteUser/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable String id) {
    boolean deleted = userService.deleteUser(id);
    if (deleted) {
      return new ResponseEntity<>("User removed successfully", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Il n'y a aucun champ à supprimer", HttpStatus.NOT_FOUND);
    }

  }


  @GetMapping("/{id}")
  public User retrieveUser(@PathVariable("id") String id) {
    User user = userService.retrieveUser(id);
    return user;
  }



  @PutMapping("/Update/{id}")
  public ResponseEntity<Map<String, Object>> updateUser(@PathVariable String id, @RequestBody User user) {
    User updatedUser = userService.updateUser(id, user);
    if (updatedUser != null) {
      Map<String, Object> response = new HashMap<>();
      response.put("message", "User updated successfully");
      response.put("user", updatedUser);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } else {
      Map<String, Object> response = new HashMap<>();
      response.put("message", "User not found with id: " + id);
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
  }
}
