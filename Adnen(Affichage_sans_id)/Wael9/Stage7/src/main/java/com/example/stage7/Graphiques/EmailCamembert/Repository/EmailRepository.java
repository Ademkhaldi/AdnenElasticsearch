package com.example.stage7.Graphiques.EmailCamembert.Repository;

import com.example.stage7.Graphiques.EmailCamembert.entity.EmailStat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends MongoRepository<EmailStat, String> {
    // Vous pouvez ajouter des méthodes personnalisées si nécessaire

    EmailStat  save(EmailStat emailStat);



}
