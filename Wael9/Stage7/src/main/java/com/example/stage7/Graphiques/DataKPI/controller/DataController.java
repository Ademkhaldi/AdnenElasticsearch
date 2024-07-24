package com.example.stage7.Graphiques.DataKPI.controller;

import com.example.stage7.Graphiques.DataKPI.Repository.DataRepository;
import com.example.stage7.Graphiques.DataKPI.entity.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("/DataController/KPI")
public class DataController {

    private final DataRepository dataRepository;

    @Autowired
    public DataController(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }



    @GetMapping("/data/KPI")
    public Data getDataKPI() {
        return dataRepository.findAll().stream().findFirst().orElse(null);
    }

    @PostMapping("/addData")
    public Data addData(@RequestBody Data data) {
        // Insérer de nouvelles données dans la base de données
        return dataRepository.save(data);
    }
    @PutMapping("/update/DataKPI/{id}")
    public Data updateDataKPI(@PathVariable String id, @RequestBody Data newData) {
        // Vérifier si l'entité que vous essayez de mettre à jour existe dans la base de données
        Data existingData = dataRepository.findById(id).orElse(null);
        if (existingData != null) {
            // Mettre à jour les données existantes avec les nouvelles données
            existingData.setCapacity(newData.getCapacity());
            existingData.setRevenue(newData.getRevenue());
            existingData.setErrors(newData.getErrors());
            existingData.setFollowers(newData.getFollowers());

            // Sauvegarder les données mises à jour dans la base de données
            return dataRepository.save(existingData);
        } else {
            // Gérer le cas où l'entité à mettre à jour n'est pas trouvée
            // Vous pouvez choisir de lancer une exception, retourner null ou effectuer une autre action appropriée.
            return null;
        }
    }


}

