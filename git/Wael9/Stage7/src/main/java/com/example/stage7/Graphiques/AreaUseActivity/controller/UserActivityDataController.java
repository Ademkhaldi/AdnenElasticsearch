package com.example.stage7.Graphiques.AreaUseActivity.controller;

import com.example.stage7.Graphiques.AreaUseActivity.Repository.UserActivityDataRepository;
import com.example.stage7.Graphiques.AreaUseActivity.entity.UserActivityData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("/api/user-activity-data/Area")
public class UserActivityDataController {



    private final UserActivityDataRepository userActivityDataRepository;

    @Autowired
    public UserActivityDataController(UserActivityDataRepository userActivityDataRepository) {
        this.userActivityDataRepository = userActivityDataRepository;
    }


    @GetMapping("/retrieve/Area")
    public UserActivityData getAreaUserActivityData() {
        // return chartDataRepository.findAll();
        return userActivityDataRepository.findAll().stream().findFirst().orElse(null);

    }


    @PostMapping("/add/Area")
    public UserActivityData addAreaUserActivityData(@RequestBody UserActivityData userActivityData) {
        return userActivityDataRepository.save(userActivityData);
    }

    @PutMapping("/update/Area/{id}")
    public UserActivityData updateAreaUserActivityData(@PathVariable String id, @RequestBody UserActivityData newData) {
        // Vérifier si l'entité que vous essayez de mettre à jour existe dans la base de données
        UserActivityData existingData = userActivityDataRepository.findById(id).orElse(null);
        if (existingData != null) {
            // Mettre à jour les données existantes avec les nouvelles données
            existingData.setLabels(newData.getLabels());
            existingData.setDatasets(newData.getDatasets());

            // Sauvegarder les données mises à jour dans la base de données
            return userActivityDataRepository.save(existingData);
        } else {
            // Gérer le cas où l'entité à mettre à jour n'est pas trouvée
            // Vous pouvez choisir de lancer une exception, retourner null ou effectuer une autre action appropriée.
            return null;
        }
    }


}


