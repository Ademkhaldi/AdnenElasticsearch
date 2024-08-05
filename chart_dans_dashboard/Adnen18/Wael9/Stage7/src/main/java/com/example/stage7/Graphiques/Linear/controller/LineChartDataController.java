package com.example.stage7.Graphiques.Linear.controller;


import com.example.stage7.Graphiques.Linear.Repository.ChartDataRepository;
import com.example.stage7.Graphiques.Linear.entity.ChartData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("/api/line-chart-data")
public class LineChartDataController {

    @Autowired
    private ChartDataRepository chartDataRepository;

    @Autowired
    public LineChartDataController(ChartDataRepository chartDataRepository) {
        this.chartDataRepository = chartDataRepository;
    }

    @GetMapping("/retrieve/lines")
    public ChartData getLineChartData() {
       // return chartDataRepository.findAll();
        return chartDataRepository.findAll().stream().findFirst().orElse(null);

    }

    @PostMapping("/add/line")
    public ChartData addLineChartData(@RequestBody ChartData ChartData) {
        return chartDataRepository.save(ChartData);
    }



    @PutMapping("/update/Line/{id}")
    public ChartData updateLineChartData(@PathVariable String id, @RequestBody ChartData newData) {
        // Vérifier si l'entité que vous essayez de mettre à jour existe dans la base de données
        ChartData existingData = chartDataRepository.findById(id).orElse(null);
        if (existingData != null) {
            // Mettre à jour les données existantes avec les nouvelles données
            existingData.setLabels(newData.getLabels());
            existingData.setDatasets(newData.getDatasets());

            // Sauvegarder les données mises à jour dans la base de données
            return chartDataRepository.save(existingData);
        } else {
            // Gérer le cas où l'entité à mettre à jour n'est pas trouvée
            // Vous pouvez choisir de lancer une exception, retourner null ou effectuer une autre action appropriée.
            return null;
        }
    }



}
