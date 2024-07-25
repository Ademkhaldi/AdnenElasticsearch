package com.example.stage7.CRUD.Datasource.controller;

import com.example.stage7.CRUD.BusinessEntity.BusinessEntity;
import com.example.stage7.CRUD.Chart.DTO.ChartDTO;
import com.example.stage7.CRUD.Chart.entity.Chart;
import com.example.stage7.CRUD.Chart.repository.ChartRepository;
import com.example.stage7.CRUD.Dashboard.DTO.DashboardDTO;
import com.example.stage7.CRUD.Dashboard.entity.Dashboard;
import com.example.stage7.CRUD.Datasource.DTO.DatasourceDTO;
import com.example.stage7.CRUD.Datasource.Service.IDatasourceService;
import com.example.stage7.CRUD.Datasource.entity.Datasource;
import com.example.stage7.CRUD.Datasource.repository.DatasourceRepository;
import com.example.stage7.CRUD.Portlet.DTO.PortletDTO;
import com.example.stage7.CRUD.Portlet.entity.Portlet;
import com.example.stage7.CRUD.Utilities.ElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@FeignClient(name = "APIgateway")
@RequestMapping("/datasources")
public class DatasourceController {

    @Autowired
    private IDatasourceService datasourceService;

    @Autowired
    private DatasourceRepository datasourceRepository;

    @Autowired
    private ChartRepository chartRepository;

    @Autowired
    private ElasticService elasticService;

    /*Avec ces modifications, /Charts/getAllCharts renverra uniquement les informations pertinentes pour les charts, y compris les détails du Datasource associé, et /datasources/getAllDatasources renverra uniquement les informations pertinentes pour les datasources, y compris les détails du Chart associé.*/
    @GetMapping("/getAllDatasources")
    public List<DatasourceDTO> getAllDatasources() {
        List<Datasource> datasources = datasourceService.getAllDatasources();
        List<DatasourceDTO> datasourceDTOs = new ArrayList<>();
        for (Datasource datasource : datasources) {
            DatasourceDTO datasourceDTO = convertToDTO(datasource);
            datasourceDTOs.add(datasourceDTO);
        }
        return datasourceDTOs;
    }

    private DatasourceDTO convertToDTO(Datasource datasource) {
        DatasourceDTO datasourceDTO = new DatasourceDTO();
        datasourceDTO.setType(datasource.getType());
        datasourceDTO.setConnection_port(datasource.getConnection_port());

        datasourceDTO.setUrl(datasource.getUrl());
        datasourceDTO.setUser(datasource.getUser());
        datasourceDTO.setPassword(datasource.getPassword());



        if (datasource instanceof BusinessEntity) {
            BusinessEntity businessEntity = (BusinessEntity) datasource;
            datasourceDTO.setId(businessEntity.getId());
            datasourceDTO.setCreationDate(businessEntity.getCreationDate());
            datasourceDTO.setCreator_id(businessEntity.getCreator_id());
            datasourceDTO.setUpdate_date(businessEntity.getUpdate_date());
            datasourceDTO.setUpdator_id(businessEntity.getUpdator_id());


        }
        // Vérifiez si la liste de portlets dans le tableau de bord n'est pas nulle
        if (datasource.getCharts() != null) {
            // Créez une liste pour stocker les DTO de portlet
            List<ChartDTO> chartDTOs = new ArrayList<>();

            // Parcourez chaque portlet dans la liste et convertissez-le en DTO
            for (Chart chart : datasource.getCharts()) {
                if (chart != null) {
                    ChartDTO chartDTO = new ChartDTO();
                    chartDTO.setId(chart.getId());
                    chartDTO.setTitle(chart.getTitle());
                    chartDTO.setType(chart.getType());
                    chartDTO.setX_axis(chart.getX_axis());
                    chartDTO.setY_axis(chart.getY_axis());
                    chartDTO.setIndex(chart.getIndex());
                    chartDTOs.add(chartDTO);

                }

            }

            datasourceDTO.setCharts(chartDTOs);
        }

        return datasourceDTO;
    }
    @GetMapping("/{id}")
    public DatasourceDTO retrieveDatasource(@PathVariable("id") String id) {
        Datasource datasource = datasourceService.retrieveDatasource(id);
        return convertToDTO(datasource);
    }
    @PostMapping("/Add")
    public ResponseEntity<Datasource> createDatasource(@RequestBody Datasource datasource) {
        Datasource createdDatasource = datasourceService.createDatasource(datasource);
        return new ResponseEntity<>(createdDatasource, HttpStatus.CREATED);
    }

    @PutMapping("/Update/{id}")
    public ResponseEntity<Map<String, Object>> updateDashboard(@PathVariable String id, @RequestBody Datasource datasource) {
        Datasource updatedDatasource = datasourceService.updateDatasource(id, datasource);
        if (updatedDatasource != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Datasource updated successfully");
            response.put("dashboard", updatedDatasource);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Datasource not found with id: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<String> deleteDatasource(@PathVariable String id) {
        boolean deleted = datasourceService.deleteDatasource(id);
        if (deleted) {
            return new ResponseEntity<>("Datasource removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Il n'y a aucun Datasource à supprimer", HttpStatus.NOT_FOUND);
        }

    }


    @DeleteMapping("/deleteAllDatasources")
    public ResponseEntity<String> deleteAllDatasources() {
        try {
            boolean deleted = datasourceService.deleteAllDatasources();
            if (deleted) {
                return ResponseEntity.ok("Datasource removed successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Il n'y a aucun Datasource à supprimer");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors de la suppression");
        }
    }





    //Affectation
    @PostMapping("/{idDatasource}/assignerListeChartsADatasource")
    public Datasource affecterChartADatasource(@PathVariable String idDatasource, @RequestBody List<Chart> charts) {
        return datasourceService.assignerListeChartsADatasource(idDatasource, charts);
    }

    @GetMapping("/getChartsForDatasource/{datasourceId}")
    public ResponseEntity<Map<String, Object>> getChartsForDatasource(@PathVariable("datasourceId") String datasourceId) {
        Set<Chart> charts = datasourceService.getChartsForDatasource(datasourceId);
        if (charts != null) {
            DatasourceDTO datasourceDTO = convertToDTO(datasourceService.retrieveDatasource(datasourceId));
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Charts retrieved successfully for datasource: " + datasourceId);
            response.put("datasource", datasourceDTO);
            response.put("charts", charts);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Collections.singletonMap("message", "Datasource not found with id: " + datasourceId), HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/getAllIndexByDatasourceId/{datasourceId}")
    public List<String> getAllIndexByDatasourceId(@PathVariable("datasourceId") String datasourceId) throws IOException {

        return elasticService.getAllIndexByDatasourceId(datasourceId);

    }

    @GetMapping("/{idDatasource}/index/{index}/attributes")
    public List<String> getAttributesByIndex(
            @PathVariable String idDatasource,
            @PathVariable String index) {
        try {
            return elasticService.getAttributeByIndex(idDatasource, index);
        } catch (IOException e) {
            // Handle exception, return appropriate response or log the error
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

/*


    @GetMapping("/camambert")
    public ResponseEntity<List<Map<String, Object>>> getCamambertData(
            @RequestParam String datasourceId,
            @RequestParam String index,
            @RequestParam String xAxisField,
            @RequestParam String yAxisField) {

        // Récupérer le datasource depuis l'ID (assurez-vous d'ajouter cette méthode dans votre service si ce n'est pas déjà fait)
        Datasource datasource = datasourceRepository.findById(datasourceId).orElse(null);
        if (datasource == null) {
            return ResponseEntity.notFound().build();
        }

        // Appeler la méthode camambert
        List<Map<String, Object>> chartData = elasticService.camambert(datasource, index, xAxisField, yAxisField);

        // Retourner les données formatées
        return ResponseEntity.ok(chartData);
    }
    */

    @GetMapping("/camambert/{chartId}")
    public ResponseEntity<List<Map<String, Object>>> getCamambertData(
            @PathVariable("chartId") String chartId) {

        // Récupérer le datasource et les paramètres associés depuis l'ID du graphique (ChartId)
        // Assurez-vous d'adapter votre logique en fonction de la manière dont vous stockez et récupérez ces informations
        Chart chart = chartRepository.findById(chartId).orElse(null);
        if (chart == null) {
            return ResponseEntity.notFound().build();
        }

        // Exemple : Assumons que le Chart contient les informations nécessaires
        Datasource datasource = datasourceRepository.findById(chart.getDatasource().getId()).orElse(null);
        if (datasource == null) {
            return ResponseEntity.notFound().build();
        }

        // Appeler la méthode camambert avec les paramètres extraits du Chart
        List<Map<String, Object>> chartData = elasticService.camambert(
                datasource,
                chart.getIndex(),
                chart.getX_axis(),
                chart.getY_axis()
        );

        // Retourner les données formatées
        return ResponseEntity.ok(chartData);
    }




}



