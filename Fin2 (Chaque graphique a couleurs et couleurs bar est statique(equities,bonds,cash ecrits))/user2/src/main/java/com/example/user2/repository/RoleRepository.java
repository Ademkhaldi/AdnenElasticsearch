package com.example.user2.repository;


import com.example.user2.models.ERole;
import com.example.user2.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);/* pour encapsuler les résultats des méthodes qui peuvent retourner null.
   */

/*Déclaration d'une méthode personnalisée findByName pour rechercher un rôle par son nom (ERole).
Retourne un Optional<Role> pour gérer le cas où le rôle n'existe pas*/
}
