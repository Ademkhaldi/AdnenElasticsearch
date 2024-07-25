package com.example.user2.security.services;

import com.example.user2.models.User;
import com.example.user2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
/*Méthode loadUserByUsername pour charger les détails de l'utilisateur par nom d'utilisateur :
Annotée avec @Override pour indiquer qu'elle implémente une méthode de UserDetailsService.
Annotée avec @Transactional pour assurer que cette méthode est exécutée dans une transaction.
Utilise le dépôt userRepository pour trouver l'utilisateur par nom d'utilisateur.
Si l'utilisateur n'est pas trouvé, lance une exception UsernameNotFoundException*/
		return UserDetailsImpl.build(user);
	}
/*Si l'utilisateur est trouvé, construit et retourne une instance de UserDetailsImpl à partir de l'utilisateur.
 */

/*Résumé
La classe UserDetailsServiceImpl est responsable de charger les détails de l'utilisateur à partir de la base de données.*/

}
