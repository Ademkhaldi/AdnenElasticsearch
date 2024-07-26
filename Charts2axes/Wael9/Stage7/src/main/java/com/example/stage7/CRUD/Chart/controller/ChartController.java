package com.example.stage7.CRUD.Chart.controller;

import com.example.stage7.CRUD.BusinessEntity.BusinessEntity;
import com.example.stage7.CRUD.Chart.DTO.ChartDTO;
import com.example.stage7.CRUD.Chart.Service.IChartService;
import com.example.stage7.CRUD.Chart.entity.Chart;
import com.example.stage7.CRUD.Chart.exception.ChartNotFoundException;
import com.example.stage7.CRUD.Datasource.DTO.DatasourceDTO;
import com.example.stage7.CRUD.Portlet.DTO.PortletDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("/Charts")
@FeignClient(name = "APIgateway")

public class ChartController {

    @Autowired
    private IChartService chartService;



    @GetMapping("/getAllCharts")
    public List<ChartDTO> getAllCharts() {
        List<Chart> charts = chartService.getAllCharts();
        List<ChartDTO> chartDTOs = new ArrayList<>();
        for (Chart chart : charts) {
            ChartDTO chartDTO = convertToDTO(chart);
            chartDTOs.add(chartDTO);
        }
        return chartDTOs;
    }
    private ChartDTO convertToDTO(Chart chart) {
        ChartDTO chartDTO = new ChartDTO();
        chartDTO.setTitle(chart.getTitle());
        chartDTO.setType(chart.getType());
        chartDTO.setX_axis(chart.getX_axis());
        chartDTO.setY_axis(chart.getY_axis());
        chartDTO.setIndex(chart.getIndex());

        // Vérifiez d'abord si l'entité commerciale de chart n'est pas nulle
        if (chart instanceof BusinessEntity) {
            BusinessEntity businessEntity = (BusinessEntity) chart;
            chartDTO.setId(businessEntity.getId());
            chartDTO.setCreationDate(businessEntity.getCreationDate());
            chartDTO.setCreator_id(businessEntity.getCreator_id());
            chartDTO.setUpdate_date(businessEntity.getUpdate_date());
            chartDTO.setUpdator_id(businessEntity.getUpdator_id());
        }

        if (chart.getDatasource() != null) {
            DatasourceDTO datasourceDTO = new DatasourceDTO();
            datasourceDTO.setId(chart.getDatasource().getId());
            datasourceDTO.setType(chart.getDatasource().getType());
            datasourceDTO.setConnection_port(chart.getDatasource().getConnection_port());


            datasourceDTO.setUrl(chart.getDatasource().getUrl());
            datasourceDTO.setUser(chart.getDatasource().getUser());
            datasourceDTO.setPassword(chart.getDatasource().getPassword());

            chartDTO.setDatasource(datasourceDTO);


        }
        if (chart.getPortlet() != null) {
            PortletDTO portletDTO = new PortletDTO();
            portletDTO.setId(chart.getPortlet().getId());
            portletDTO.setRow(chart.getPortlet().getRow());
            portletDTO.setColumn(chart.getPortlet().getColumn());
            chartDTO.setPortlet(portletDTO);
        }
        return chartDTO;
    }



   @GetMapping("/{id}")
   public ChartDTO retrieveChart(@PathVariable("id") String id) {
       Chart chart = chartService.retrieveChart(id);
       return convertToDTO(chart);
   }



    @GetMapping("/title/{title}")
    public ChartDTO retrieveTitle(@PathVariable("title") String title) {
        Chart chart = chartService.retrieveTitle(title);
        if (chart != null) {
            return convertToDTO(chart);
        } else {
            throw new ChartNotFoundException("Chart with title " + title + " not found");
        }
    }


    @PostMapping("/Add/{idDatasource}")
    public ResponseEntity<Chart> createChart(@RequestBody Chart chart,@PathVariable String idDatasource) {
        Chart createChart = chartService.createChart(chart,idDatasource);
        return new ResponseEntity<>(createChart, HttpStatus.CREATED);
    }

    @PutMapping("/Update/{id}")
    public ResponseEntity<Map<String, Object>> updateChart(@PathVariable String id, @RequestBody Chart chart) {
        Chart updatedChart = chartService.updateChart(id, chart);
        if (updatedChart != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Chart updated successfully");
            response.put("chart", updatedChart);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Chart not found with id: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<String> deleteChart(@PathVariable String id) {
        boolean deleted = chartService.deleteChart(id);
        if (deleted) {
            return new ResponseEntity<>("Chart removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Il n'y a aucun champ à supprimer", HttpStatus.NOT_FOUND);
        }

    }


    @DeleteMapping("/deleteAllCharts")
    public ResponseEntity<String> deleteAllCharts() {
        try {
            boolean deleted = chartService.deleteAllCharts();
            if (deleted) {
                return ResponseEntity.ok("Charts removed successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune suppression");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors de la suppression");
        }
    }

    //Affectation
    //{idPortlet}{idDashboard}
    @PutMapping("/affecterDatasourceAChart/{idChart}/{idDatasource}")
    public ResponseEntity<Map<String, String>> affecterDatasourceAChart(
            /*{idPortlet}*/ @PathVariable("idChart") String idChart,
            /*{idDashboard}*/@PathVariable("idDatasource") String idDatasource) {

        boolean affectationReussie =chartService.affecterDatasourceAChart(idChart, idDatasource);
        // Créez une carte pour stocker les informations de la réponse
        Map<String, String> response = new HashMap<>();
        if(affectationReussie){

            // Ajoutez les détails de l'affectation à la réponse
            response.put("message", "Affectation réussie");
            response.put("ChartId", idChart);
            response.put("DatasourceId", idDatasource);

            // Répondez avec un objet ResponseEntity contenant la carte de réponse
            return ResponseEntity.ok(response);
        } else {

            // Si le client ou le marché n'est pas trouvé, ajoutez un message d'erreur à la réponse
            response.put("message", "Chart ou Datasource non trouvé");

            // Répondez avec un statut NOT_FOUND et la carte de réponse
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/affecterPortletAChart/{idChart}/{idPortlet}")
    public ResponseEntity<Map<String, String>> affecterPortletAChart(
            /*{idDashboard}*/@PathVariable("idChart") String idChart,
            /*{idPortlet}*/ @PathVariable("idPortlet") String idPortlet) {

        boolean affectationReussie =chartService.affecterPortletAChart(idChart,idPortlet);
        // Créez une carte pour stocker les informations de la réponse
        Map<String, String> response = new HashMap<>();
        if(affectationReussie){

            // Ajoutez les détails de l'affectation à la réponse
            response.put("message", "Affectation réussie");
            response.put("ChartId", idChart);
            response.put("portletId", idPortlet);

            // Répondez avec un objet ResponseEntity contenant la carte de réponse
            return ResponseEntity.ok(response);
        } else {

            // Si le client ou le marché n'est pas trouvé, ajoutez un message d'erreur à la réponse
            response.put("message", "idChart ou Portlet non trouvé");

            // Répondez avec un statut NOT_FOUND et la carte de réponse
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }



    @GetMapping("/getElasticsearchUrl/{idDatasource}")
    public ResponseEntity<String> getElasticsearchUrl(@PathVariable String idChart) {
        String elasticsearchUrl = chartService.getElasticsearchUrl(idChart);
        if (elasticsearchUrl != null) {
            return ResponseEntity.ok(elasticsearchUrl);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Datasource or Chart not found");
        }
    }


    @PostMapping("/getElasticsearchUrl")
    public ResponseEntity<String> getElasticsearchUrl(@RequestBody Map<String, String> request) {
        String idChart = request.get("idChart");
        String url = chartService.getElasticsearchUrl(idChart);
        if (url != null) {
            return ResponseEntity.ok(url);
        } else {
            return ResponseEntity.status(404).body("Datasource or Chart not found");
        }
    }


}
