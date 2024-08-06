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
        if (datasource.getChart() != null) {
            ChartDTO chartDTO = new ChartDTO();
            chartDTO.setId(datasource.getChart().getId());
            chartDTO.setTitle(datasource.getChart().getTitle());
            chartDTO.setType(datasource.getChart().getType());
            chartDTO.setX_axis(datasource.getChart().getX_axis());
            chartDTO.setY_axis(datasource.getChart().getY_axis());
            datasourceDTO.setChart(chartDTO);
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
    //{idPortlet}{idDashboard}
    @PutMapping("/affecterChartADatasource/{idDatasource}/{idChart}")
    public ResponseEntity<Map<String, String>> affecterChartADatasource(
            /*{idPortlet}*/ @PathVariable("idDatasource") String idDatasource,
            /*{idDashboard}*/@PathVariable("idChart") String idChart) {

        boolean affectationReussie =datasourceService.affecterChartADatasource(idDatasource, idChart);
        // Créez une carte pour stocker les informations de la réponse
        Map<String, String> response = new HashMap<>();
        if(affectationReussie){

            // Ajoutez les détails de l'affectation à la réponse
            response.put("message", "Affectation réussie");
            response.put("datasourceId", idDatasource);
            response.put("chartId", idChart);

            // Répondez avec un objet ResponseEntity contenant la carte de réponse
            return ResponseEntity.ok(response);
        } else {

            // Si le client ou le marché n'est pas trouvé, ajoutez un message d'erreur à la réponse
            response.put("message", "Chart ou Datasource non trouvé");

            // Répondez avec un statut NOT_FOUND et la carte de réponse
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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

        @GetMapping("/table/{chartId}")
        public ResponseEntity<List<Map<String, Object>>> gettableData(
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
            List<Map<String, Object>> chartData = elasticService.table(
                    datasource,
                    chart.getIndex(),
                    chart.getX_axis(),
                    chart.getY_axis()
            );

            // Retourner les données formatées
            return ResponseEntity.ok(chartData);
        }





    @GetMapping("/histogramme2/{chartId}")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getHistogramme2(
            @PathVariable String chartId) {

        // Retrieve the Chart entity using the provided chartId
        Chart chart = chartRepository.findById(chartId).orElse(null);
        if (chart == null) {
            return ResponseEntity.notFound().build();
        }

        // Get the Datasource associated with the Chart
        Datasource datasource = chart.getDatasource();
        if (datasource == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // Retrieve the histogram data using the provided chart details
        Map<String, List<Map<String, Object>>> histogramData = elasticService.histogramme2(
                datasource,
                chart.getIndex(),
                chart.getX_axis(),
                chart.getY_axis(),
                chart.getAggreg()
        );

        return ResponseEntity.ok(histogramData);
    }










}



