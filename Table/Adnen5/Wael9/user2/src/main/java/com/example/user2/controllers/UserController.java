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
public ResponseEntity<?> getUsers(@AuthenticationPrincipal UserDetailsImpl userDetails) {


Boolean exist = Boolean.FALSE;
    for (Iterator<? extends GrantedAuthority> it = userDetails.getAuthorities().iterator(); it.hasNext(); ) {
      if(String.valueOf(it.next())== ERole.ROLE_ADMIN.name())
        exist=Boolean.TRUE;
     // break;
      else {
        // Regular user can see only their own user details
        User user = userService.retrieveUser(userDetails.getId());
        //return new ResponseEntity<>(Collections.singletonList(user), HttpStatus.OK);
      }
    }



  if (exist) {
    // Admin can see all users
    return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
  } else {
    // Regular user can see only their own user details
    User user = userService.retrieveUser(userDetails.getId());
    return new ResponseEntity<>(Collections.singletonList(user), HttpStatus.OK);
  }
}


/*
  @GetMapping("/retrieve")
  public ResponseEntity<?> getUsers(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<User> allUsers = userService.getAllUsers();
    return new ResponseEntity<>(allUsers, HttpStatus.OK);
  }
*/
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
