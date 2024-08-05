package com.example.stage7.Graphiques.EmailCamembert.controller;

import com.example.stage7.Graphiques.EmailCamembert.Repository.EmailRepository;
import com.example.stage7.Graphiques.EmailCamembert.entity.EmailStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("/api/email-data/Camembert")
public class EmailDataController {

    private final EmailRepository emailStatRepository;
    private final MongoTemplate mongoTemplate;


    @Autowired
    public EmailDataController(EmailRepository emailStatRepository, MongoTemplate mongoTemplate) {
        this.emailStatRepository = emailStatRepository;
        this.mongoTemplate = mongoTemplate;
        ;
    }


    @GetMapping("/retrieve-all/Camembert")
    public List<EmailStat> getEmailDataCamembert() {

        List<EmailStat> email = emailStatRepository.findAll();
        return email;
    }

    @PostMapping("/add-list/Camembert")
    public List<EmailStat> addEmailStats(@RequestBody List<EmailStat> emailStats) {
        // Insérer la liste de données dans la base de données
        return emailStatRepository.saveAll(emailStats);
    }



    @PutMapping("/update-all/Camembert")
    public ResponseEntity<List<EmailStat>> modifyAllEmailStats(@RequestBody List<EmailStat> updatedStats) {
        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, EmailStat.class);

        List<EmailStat> updatedEmailStats = updatedStats.stream()
                .map(updatedStat -> {
                    Optional<EmailStat> optionalEmailStat = emailStatRepository.findById(updatedStat.getId());
                    if (optionalEmailStat.isPresent()) {
                        EmailStat existingStat = optionalEmailStat.get();
                        existingStat.setLabel(updatedStat.getLabel());
                        existingStat.setValue(updatedStat.getValue());

                        // Construction de l'opération de mise à jour avec UpdateDefinition
                        UpdateDefinition update = new Update().set("label", existingStat.getLabel())
                                .set("value", existingStat.getValue());

                        // Utilisation de Criteria pour créer la condition de requête
                        Criteria criteria = Criteria.where("_id").is(existingStat.getId());
                        bulkOps.updateOne(new Query(criteria), update);

                        return existingStat;
                    }
                    return null;
                })
                .filter(stat -> stat != null)
                .collect(Collectors.toList());

        bulkOps.execute(); // Écriture des changements dans la base de données

        // Retourner la réponse avec les statistiques d'email mises à jour
        return ResponseEntity.ok(updatedEmailStats);
    }

}




