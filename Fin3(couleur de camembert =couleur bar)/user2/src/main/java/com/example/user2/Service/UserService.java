package com.example.user2.Service;

import com.example.user2.models.User;
import com.example.user2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService implements IUserService{

@Autowired
    private UserRepository userRepository;


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Override

    public boolean deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false; // Gérer l'absence de l'élément à supprimer comme vous le souhaitez
        }
    }
    @Override
    public User retrieveUser(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateUser(String id, User user) {
        // Rechercher l'utilisateur existant dans la base de données par son ID
        Optional<User> existingUserOptional = userRepository.findById(id);

        // Vérifier si l'utilisateur existe
        if (existingUserOptional.isPresent()) {
            // Récupérer l'utilisateur existant à partir de l'Optional
            User existingUser = existingUserOptional.get();

            // Mettre à jour les informations de l'utilisateur avec les nouvelles valeurs
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());

            // Enregistrer les modifications dans la base de données et retourner l'utilisateur mis à jour
            return userRepository.save(existingUser);
        } else {
            // Si l'utilisateur n'existe pas, retourner null (ou gérer l'absence comme souhaité)
            return null;
        }
    }

}
/*En résumé, @Bean est une partie clé de la configuration Spring, permettant de définir et de gérer les objets qui composent une application Spring.


Les beans sont des objets que Spring gère et injecte dans d'autres composants de l'application

En Spring Framework, l'annotation @Bean est utilisée pour indiquer qu'une méthode dans une classe de configuration (@Configuration) produit un bean qui doit être géré par Spring

 */