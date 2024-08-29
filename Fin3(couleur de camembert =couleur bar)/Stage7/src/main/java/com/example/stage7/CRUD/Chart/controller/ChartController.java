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
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")//(typiquement utilisé pour le développement local avec Angular).
@RestController//Indique que cette classe est un contrôleur Spring MVC qui gère les requêtes HTTP et renvoie des réponses HTTP.
@RequestMapping("/Charts")//Définit le chemin de base pour les requêtes
@FeignClient(name = "APIgateway")//Déclare ce contrôleur comme client Feign, permettant d'appeler d'autres services via une interface déclarée ailleurs dans l'application.

public class ChartController {

    @Autowired//Injecte automatiquement une instance de IChartServi
    private IChartService chartService;



    @GetMapping("/getAllCharts")
   /*etAllCharts : Récupère tous les objets Chart via chartService, les convertit en ChartDTO, et renvoie la liste des DTOs.*/
    public List<ChartDTO> getAllCharts() {
        List<Chart> charts = chartService.getAllCharts();
        List<ChartDTO> chartDTOs = new ArrayList<>();
        for (Chart chart : charts) {
            ChartDTO chartDTO = convertToDTO(chart);
            chartDTOs.add(chartDTO);
        }
        return chartDTOs;
    }
    /*convertToDTO : Convertit un objet Chart en un ChartDTO, en copiant les propriétés et en incluant des informations liées au Datasource et au Portlet si disponibles.*/
    /*convertToDTO(Chart chart) : Cette méthode prend un paramètre chart de type Chart pour le convertir en un objet ChartDTO*/
    private ChartDTO convertToDTO(Chart chart) {
     /*Crée une nouvelle instance de ChartDTO appelée chartDTO. Cet objet sera rempli avec les données extraites de chart.*/
        ChartDTO chartDTO = new ChartDTO();
        /*Un nouvel objet ChartDTO est instancié. Cet objet contiendra les données du Chart après la conversion.*/
/*Les champs du ChartDTO sont remplis avec les valeurs correspondantes de l'objet Chart. */
        chartDTO.setTitle(chart.getTitle());
       /*setTitle : Le titre du chart est défini avec la valeur récupérée du Chart (chart.getTitle()).*/
        chartDTO.setType(chart.getType());
        chartDTO.setX_axis(chart.getX_axis());
        chartDTO.setY_axis(chart.getY_axis());
        chartDTO.setAggreg(chart.getAggreg());
        chartDTO.setIndex(chart.getIndex());

        // Vérifiez d'abord si l'entité commerciale de chart n'est pas nulle
        if (chart instanceof BusinessEntity) {
   /*Vérifie si chart est une instance de BusinessEntity. Cela permet de s'assurer que les champs spécifiques à BusinessEntity (comme id, creationDate, etc.) sont disponibles.*/
            BusinessEntity businessEntity = (BusinessEntity) chart;
      /*      Convertit l'objet chart en BusinessEntity. Ceci est nécessaire pour accéder aux attributs spécifiques de BusinessEntity.  */
     /*Si chart est une instance de BusinessEntity, ces lignes vont :
setId : Affecter l'ID de l'entité commerciale au DTO.
setCreationDate : Définir la date de création de l'entité.
setCreator_id : Définir l'identifiant du créateur de l'entité.
setUpdate_date : Définir la date de mise à jour de l'entité.
setUpdator_id : Définir l'identifiant de la personne ayant mis à jour l'entité.*/
            chartDTO.setId(businessEntity.getId());
            chartDTO.setCreationDate(businessEntity.getCreationDate());
            chartDTO.setCreator_id(businessEntity.getCreator_id());
            chartDTO.setUpdate_date(businessEntity.getUpdate_date());
            chartDTO.setUpdator_id(businessEntity.getUpdator_id());
        }
/*Vérifie si le chart possède une datasource. Si ce n'est pas null, cela signifie que chart est lié à une datasource .*/
        if (chart.getDatasource() != null) {
/*Crée une nouvelle instance de DatasourceDTO pour stocker les détails de la datasource.
 */
   /*Définit les attributs de datasourceDTO à partir des valeurs de la datasource de chart :
setId : Affecte l'ID de la datasource.
setType : Affecte le type de la datasource.
setConnection_port : Affecte le port de connexion de la datasource.*/
            DatasourceDTO datasourceDTO = new DatasourceDTO();
            datasourceDTO.setId(chart.getDatasource().getId());
            datasourceDTO.setType(chart.getDatasource().getType());
            datasourceDTO.setConnection_port(chart.getDatasource().getConnection_port());
/*Définit les autres attributs de datasourceDTO :
setUrl : Affecte l'URL de la datasource.
setUser : Affecte l'utilisateur lié à la datasource.
setPassword : Affecte le mot de passe de la datasource.*/

            datasourceDTO.setUrl(chart.getDatasource().getUrl());
            datasourceDTO.setUser(chart.getDatasource().getUser());
            datasourceDTO.setPassword(chart.getDatasource().getPassword());
/*Associe la datasourceDTO au chartDTO.*/
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
   /*@PathVariable("id"): Extrait le id de l'URL et le passe en paramètre.
    */
   public ChartDTO retrieveChart(@PathVariable("id") String id) {
    /*chartService.retrieveChart(id): Appelle le service pour récupérer le Chart correspondant à l'id.*/
       Chart chart = chartService.retrieveChart(id);
  /*convertToDTO(chart): Convertit l'entité Chart en ChartDTO.
   */
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
        return new ResponseEntity<>(createChart, HttpStatus.CREATED);//Retourne une réponse HTTP avec le chart créé et le statut CREATED (201).
    }


    @PutMapping("/Update/{idChart}/{idDatasource}")
public ResponseEntity<Map<String, Object>> updateChart(
        @PathVariable String idChart,//L'ID du chart à mettre à jour est extrait de l'URL.
        @PathVariable String idDatasource,
        @RequestBody Chart chart)/*Le corps de la requête contient un objet Chart avec les nouvelles données à mettre à jour.*/{
    // Appel du service pour mettre à jour le chart
    Chart updatedChart = chartService.updateChart(idChart, chart, idDatasource);
    Map<String, Object> response = new HashMap<>();//Un Map<String, Object> est utilisé pour retourner la réponse, avec un message de succès ou un message d'erreur.
// Si le chart a bien été mis à jour

    if (updatedChart != null) {
        response.put("message", "Chart updated successfully");
        response.put("chart", updatedChart);
        return new ResponseEntity<>(response, HttpStatus.OK);
    } else {
        response.put("message", "Chart not found with id: " + idChart);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    /*Le service chartService.updateChart est appelé pour mettre à jour le chart correspondant à idChart avec les nouvelles données du chart et l'ID de la datasource.
Un Map<String, Object> est utilisé pour retourner la réponse, avec un message de succès ou un message d'erreur.*/




}

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<String> deleteChart(@PathVariable String id) {
        // Appel du service pour supprimer le chart

        boolean deleted = chartService.deleteChart(id);
        // Si la suppression a réussi

        if (deleted) {
            return new ResponseEntity<>("Chart removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Il n'y a aucun champ à supprimer", HttpStatus.NOT_FOUND);
        }
//Si la mise à jour est réussie, une réponse avec le chart mis à jour et un message de succès est renvoyée. Sinon, un message d'erreur avec le statut NOT_FOUND est renvoyé.
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
        /*Un objet Map<String, String> est utilisé pour construire la réponse, qui contiendra un message indiquant si l'affectation a réussi ou échoué.*/



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
