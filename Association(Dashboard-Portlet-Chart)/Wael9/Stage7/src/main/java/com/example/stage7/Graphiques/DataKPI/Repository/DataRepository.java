package com.example.stage7.Graphiques.DataKPI.Repository;

import com.example.stage7.Graphiques.DataKPI.entity.Data;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends MongoRepository<Data, String> {
    // Vous pouvez ajouter des méthodes personnalisées si nécessaire

    Data save(Data data);

}
