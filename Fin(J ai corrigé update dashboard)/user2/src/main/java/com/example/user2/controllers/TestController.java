package com.example.user2.controllers;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowedHeaders = "*")
@CrossOrigin()
@RestController
@FeignClient(name = "APIgateway")
@RequestMapping("/api/test")
public class TestController {
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
/*Cette méthode répond aux requêtes HTTP GET envoyées à l'endpoint /api/test/all.
 */
/*Méthode allAccess: Renvoie un contenu public.
 */

	@GetMapping("/user")
	/*Méthode userAccess: Renvoie un contenu accessible uniquement aux utilisateurs.
	 */
/*Restriction d'accès aux utilisateurs ayant le rôle USER ou ADMIN*/
	@PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

/*Cette méthode répond aux requêtes HTTP GET envoyées à l'endpoint /api/test/user.
Elle est annotée avec @PreAuthorize, qui spécifie que l'accès à cette méthode est restreint aux utilisateurs ayant le rôle USER ou ADMIN.
jav*/

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}

/*Cette méthode répond aux requêtes HTTP GET envoyées à l'endpoint /api/test/admin.
Elle est annotée avec @PreAuthorize, qui spécifie que l'accès à cette méthode est restreint aux utilisateurs ayant le rôle ADMIN.*/

}
