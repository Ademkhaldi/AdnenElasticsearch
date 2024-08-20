package com.example.stage7.CRUD.BusinessEntity.controller;

import com.example.stage7.CRUD.BusinessEntity.BusinessEntity;
import com.example.stage7.CRUD.BusinessEntity.BusinessEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("/businessEntities")
public class BusinessEntityController {

    @Autowired
    private BusinessEntityRepository businessEntityRepository;


    // Endpoint pour créer une entité métier
    @PostMapping("/add")
    public ResponseEntity<BusinessEntity> createBusinessEntity(@RequestBody BusinessEntity businessEntity) {
        //businessEntity.setCreationDate(new Date()); // Utilise la date et l'heure actuelles lors de la création
        businessEntity.setCreator_id(businessEntity.getCreator_id());
        businessEntity.setCreateSystemDate(); // Met à jour la date de création
        businessEntity.setUpdate_date(businessEntity.getCreationDate()); // Met à jour la date de création

        // Enregistrement de l'entité métier dans la base de données
        BusinessEntity savedBusinessEntity = businessEntityRepository.save(businessEntity);
        return new ResponseEntity<>(savedBusinessEntity, HttpStatus.CREATED);
    }




    // Endpoint pour récupérer toutes les entités métier
    @GetMapping("/getbusinessEntities")
    public ResponseEntity<List<BusinessEntity>> getAllBusinessEntities() {
        List<BusinessEntity> businessEntities = (List<BusinessEntity>) businessEntityRepository.findAll();
        return new ResponseEntity<>(businessEntities, HttpStatus.OK);
    }

    // Endpoint pour récupérer une entité métier par son ID
    @GetMapping("/{id}")
    public ResponseEntity<BusinessEntity> getBusinessEntityById(@PathVariable("id") String id) {
        Optional<BusinessEntity> businessEntityOptional = businessEntityRepository.findById(id);
        if (businessEntityOptional.isPresent()) {
            return new ResponseEntity<>(businessEntityOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint pour mettre à jour une entité métier par son ID
    @PutMapping("/update/{id}")
    public ResponseEntity<BusinessEntity> updateBusinessEntity(@PathVariable("id") String id, @RequestBody BusinessEntity businessEntity) {
        Optional<BusinessEntity> existingBusinessEntityOptional = businessEntityRepository.findById(id);
        if (existingBusinessEntityOptional.isPresent()) {
            BusinessEntity existingBusinessEntity = existingBusinessEntityOptional.get();

            // Mettre à jour les propriétés de l'entité métier existante
            businessEntity.setCreationDate(existingBusinessEntity.getCreationDate());
            existingBusinessEntity.setCreator_id(businessEntity.getCreator_id());
            existingBusinessEntity.setUpdateSystemDate();
            existingBusinessEntity.setUpdator_id(businessEntity.getUpdator_id());

            // Enregistrer les modifications
            BusinessEntity updatedBusinessEntity = businessEntityRepository.save(existingBusinessEntity);
            return new ResponseEntity<>(updatedBusinessEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint pour supprimer une entité métier par son ID
    @DeleteMapping("Delete/{id}")
    public ResponseEntity<Void> deleteBusinessEntity(@PathVariable("id") String id) {
        businessEntityRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
