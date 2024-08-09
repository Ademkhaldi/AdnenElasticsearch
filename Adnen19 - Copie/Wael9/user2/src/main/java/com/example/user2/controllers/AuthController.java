package com.example.user2.controllers;


import com.example.user2.models.ERole;
import com.example.user2.models.Role;
import com.example.user2.models.User;
import com.example.user2.payload.request.LoginRequest;
import com.example.user2.payload.request.SignupRequest;
import com.example.user2.payload.response.JwtResponse;
import com.example.user2.payload.response.MessageResponse;
import com.example.user2.repository.RoleRepository;
import com.example.user2.repository.UserRepository;
import com.example.user2.security.jwt.JwtUtils;
import com.example.user2.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowedHeaders = {"Authorization", "Content-Type"})
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowedHeaders = "*")
@CrossOrigin()
@FeignClient(name = "APIgateway")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        /*Authentifie l'utilisateur en utilisant les informations fournies (nom d'utilisateur et mot de passe).
		 */
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        /*Définit le contexte de sécurité avec l'objet Authentication et génère un JWT à l'aide de JwtUtils.
		 */
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		/*Récupère les détails de l'utilisateur authentifié et extrait les rôles sous forme de liste.
		 */
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
       /*Renvoie une réponse HTTP 200 OK avec le JWT et les détails de l'utilisateur.
		*/
		return ResponseEntity.ok(new JwtResponse(jwt,
												 userDetails.getId(),
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}
/*Cette annotation @PostMapping indique que cette méthode répond aux requêtes HTTP POST envoyées à l'endpoint /api/auth/signin.
@Valid est une annotation utilisée pour activer la validation des paramètres de la requête.
@RequestBody indique que le corps de la requête HTTP sera mappé sur l'objet LoginRequest.*/


	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

		/*Vérifie si le nom d'utilisateur ou l'email est déjà pris et renvoie une réponse HTTP 400 Bad Request en cas de duplication.
		 */


		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		/*Crée un nouvel utilisateur avec les informations fournies et encode le mot de passe.
		 */
		User user = new User(signUpRequest.getUsername(),
				signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));
        /*Récupère les rôles spécifiés et prépare un ensemble pour les stocker.
		 */
		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();
        /*Associe les rôles à l'utilisateur en fonction des rôles spécifiés dans la requête.
		 */
		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
					case "admin":
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(adminRole);
						break;

					default:
						Role userRole = roleRepository.findByName(ERole.ROLE_USER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRole);
				}
			});
		}
/*Associe les rôles à l'utilisateur en fonction des rôles spécifiés dans la requête.
 */
		user.setRoles(roles);
		userRepository.save(user);
/*Renvoie une réponse HTTP 200 OK indiquant que l'utilisateur a été enregistré avec succès.
 */
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}



	/*	Cette méthode répond aux requêtes HTTP POST envoyées à l'endpoint /api/auth/signup.
	Elle utilise également les annotations @Valid et @RequestBody.*/

}
