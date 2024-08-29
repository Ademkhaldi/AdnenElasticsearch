package com.example.stage7.CRUD.Dashboard.controller;

import com.example.stage7.CRUD.BusinessEntity.BusinessEntity;
import com.example.stage7.CRUD.Chart.DTO.ChartDTO;
import com.example.stage7.CRUD.Chart.entity.Chart;
import com.example.stage7.CRUD.Dashboard.DTO.DashboardDTO;
import com.example.stage7.CRUD.Dashboard.Service.IDashboardService;
import com.example.stage7.CRUD.Dashboard.entity.Dashboard;
import com.example.stage7.CRUD.Dashboard.repository.DashboardRepository;
import com.example.stage7.CRUD.Portlet.DTO.PortletDTO;
import com.example.stage7.CRUD.Portlet.entity.Portlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j//@Slf4j : Génère un logger pour consigner les informations.
@CrossOrigin()
@RestController
@FeignClient(name = "APIgateway")//@FeignClient : Définit ce contrôleur comme un client Feign pour interagir avec une passerelle API nommée APIgateway.
@RequestMapping("/dashboards")
public class DashboardController {


    @Autowired
    private IDashboardService dashboardService;

    @Autowired
    private DashboardRepository dashboardRepository;


    @GetMapping("/getAllDashboards")
    public List<DashboardDTO> getAllDashboards() {
        List<Dashboard> dashboards = dashboardService.getAllDashboards();
        List<DashboardDTO> dashboardDTOs = new ArrayList<>();
        for (Dashboard dashboard : dashboards) {
            DashboardDTO dashboardDTO = convertToDTO(dashboard);
            dashboardDTOs.add(dashboardDTO);
        }
        return dashboardDTOs;
    }//Cette fonction récupère tous les dashboards sous forme d'une liste et les convertit en DTOs. Elle retourne ensuite la liste de DashboardDTO pour la réponse HTTP.



    private DashboardDTO convertToDTO(Dashboard dashboard) {

        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setTitle(dashboard.getTitle());


        if (dashboard instanceof BusinessEntity) {
            BusinessEntity businessEntity = (BusinessEntity) dashboard;
            dashboardDTO.setId(businessEntity.getId());
            dashboardDTO.setCreationDate(businessEntity.getCreationDate());
            dashboardDTO.setCreator_id(businessEntity.getCreator_id());
            dashboardDTO.setUpdate_date(businessEntity.getUpdate_date());
            dashboardDTO.setUpdator_id(businessEntity.getUpdator_id());
        }


        // Vérifiez si la liste de portlets dans le tableau de bord n'est pas nulle
        if (dashboard.getPortlets() != null) {
            // Créez une liste pour stocker les DTO de portlet
            List<PortletDTO> portletDTOs = new ArrayList<>();

            // Parcourez chaque portlet dans la liste et convertissez-le en DTO
            for (Portlet portlet : dashboard.getPortlets()) {
                if (portlet != null) {
                    PortletDTO portletDTO = new PortletDTO();
                    portletDTO.setId(portlet.getId());
                    portletDTO.setRow(portlet.getRow());
                    portletDTO.setColumn(portlet.getColumn());
                    portletDTOs.add(portletDTO);
                }

            }




            // Affectez la liste de DTO de portlet au DTO de tableau de bord
            dashboardDTO.setPortlets(portletDTOs);
        }

        return dashboardDTO;
    }//Cette fonction privée convertit un objet Dashboard en objet DashboardDTO en transférant les informations essentielles (id, titre, dates, portlets associés).




    @GetMapping("/{id}")
    public DashboardDTO retrieveDashboard(@PathVariable("id") String id) {
        Dashboard dashboard = dashboardService.retrieveDashboard(id);
        return convertToDTO(dashboard);
    }//Cette fonction récupère un tableau de bord spécifique à partir de son identifiant et le convertit en DashboardDTO avant de le renvoyer.

    @PostMapping("/Add")
    public ResponseEntity<Dashboard> createDashboard(@RequestBody Dashboard dashboard) {
        Dashboard createdDashboard = dashboardService.createDashboard(dashboard);
        return new ResponseEntity<>(createdDashboard, HttpStatus.CREATED);
    }//Cette fonction crée un nouveau tableau de bord à partir des données envoyées dans la requête, en utilisant le service. Elle renvoie le dashboard créé avec un statut HTTP 201 (CREATED).



    @PutMapping("/Update/{id}")
    public ResponseEntity<Map<String, Object>> updateDashboard(@PathVariable String id, @RequestBody Dashboard dashboard) {
        Dashboard updatedDashboard = dashboardService.updateDashboard(id, dashboard);//Cette ligne appelle la méthode updateDashboard du service (dashboardService) en lui passant l'ID du tableau de bord et les nouvelles données. Le service traite la logique de mise à jour et retourne le tableau de bord mis à jour, ou null si aucun tableau de bord n'a été trouvé avec cet ID.
        if (updatedDashboard != null) {//Cette condition vérifie si le tableau de bord a été trouvé et mis à jour avec succès. Si updatedDashboard n'est pas null, cela signifie que la mise à jour a bien eu lieu.
            Map<String, Object> response = new HashMap<>();// Cette ligne initialise une nouvelle HashMap pour stocker les données à renvoyer dans la réponse HTTP. La clé est une chaîne de caractères (String) et la valeur peut être de n'importe quel type (Object).
            response.put("message", "Dashboard updated successfully");//joute un message de succès dans la map avec la clé "message" et la valeur "Dashboard updated successfully". Ce message sera inclus dans la réponse HTTP.
            response.put("dashboard", updatedDashboard);//Ajoute le tableau de bord mis à jour dans la map avec la clé "dashboard" et la valeur correspondant à l'objet updatedDashboard. Cet objet contient les nouvelles données du tableau de bord après la mise à jour.
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();//Crée une nouvelle HashMap pour préparer une réponse contenant un message d'erreur.
            response.put("message", "Dashboard not found with id: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }//Cette fonction met à jour un tableau de bord en fonction de l'ID et des nouvelles données fournies dans la requête. Elle renvoie un message avec les détails du tableau de bord mis à jour ou un message d'erreur si le tableau de bord n'existe pas.
//HashMap est une classe concrète qui implémente l'interface Map. Elle est l'une des implémentations les plus courantes de l'interface Map. Comme son nom l'indique, elle utilise une table de hachage pour stocker les paires clé-valeur, ce qui permet une recherche rapide en fonction de la clé.
//Map est une interface dans le package java.util. Elle définit les méthodes qui doivent être implémentées par toutes les classes qui veulent représenter une collection de paires clé-valeur. Map ne peut pas être directement instanciée, car elle est abstraite. Les principales opérations qu'elle définit sont :
//
//Insertion de paires clé-valeur.
//Recherche de valeur à partir d'une clé.
//Suppression de paires clé-valeur.
//Vérification de la présence d'une clé ou d'une valeur.

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<String> deleteDashboard(@PathVariable String id) {
        boolean deleted = dashboardService.deleteDashboard(id);
        if (deleted) {
            return new ResponseEntity<>("Dashboard removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Il n'y a aucun Dashboard à supprimer", HttpStatus.NOT_FOUND);
        }

    }//Cette fonction supprime un tableau de bord à partir de son identifiant. Elle renvoie un message de succès ou un message d'erreur si le tableau de bord n'a pas été trouvé.




    @DeleteMapping("/deleteAllDashboards")
    public ResponseEntity<String> deleteAllDashboards() {
        try {
            boolean deleted = dashboardService.deleteAllDashboards();
            if (deleted) {
                return ResponseEntity.ok("Dashboards removed successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Il n'y a aucun Dashboard à supprimer");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors de la suppression");
        }
    }//Cette fonction supprime tous les tableaux de bord et renvoie un message de confirmation ou une erreur.


    //Affectation


    @PostMapping("/{idDashboard}/assignerListePortletsADashboard")
    public ResponseEntity<Dashboard> assignerListePortletsADashboard(@PathVariable String idDashboard, @RequestBody List<Portlet> portlets) {
        Dashboard dashboard = dashboardService.assignerListePortletsADashboard(idDashboard, portlets);

        if (dashboard != null) {
            log.info("Returning dashboard: {}", dashboard);
            return ResponseEntity.ok(dashboard);
        } else {
            log.warn("Dashboard not found for ID: {}", idDashboard);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

//Cette fonction assigne une liste de portlets à un tableau de bord. Elle renvoie le tableau de bord mis à jour ou un message d'erreur si l'ID est incorrect.






    @GetMapping("{dashboardId}/getPortletsForDashboard")
    public ResponseEntity<List<PortletDTO>> getPortletsForDashboard(@PathVariable String dashboardId) {
        try {
            List<Portlet> portlets = dashboardService.getPortletsForDashboard(dashboardId);
            List<PortletDTO> portletDTOs = new ArrayList<>();

            for (Portlet portlet : portlets) {
                PortletDTO portletDTO = new PortletDTO();
                portletDTO.setId(portlet.getId());
                portletDTO.setRow(portlet.getRow());
                portletDTO.setColumn(portlet.getColumn());

                // Adding Chart details to PortletDTO
                Chart chart = portlet.getChart();
                if (chart != null) {
                    ChartDTO chartDTO = new ChartDTO();
                    chartDTO.setId(chart.getId());
                    chartDTO.setTitle(chart.getTitle());
                    chartDTO.setType(chart.getType());
                    chartDTO.setX_axis(chart.getX_axis());
                    chartDTO.setY_axis(chart.getY_axis());
                    chartDTO.setAggreg(chart.getAggreg());
                    chartDTO.setIndex(chart.getIndex());

                    portletDTO.setChart(chartDTO);
                }
                portletDTOs.add(portletDTO);
            }

            return ResponseEntity.ok(portletDTOs);
        } catch (Exception e) {
            log.error("Error fetching portlets: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//Cette fonction récupère tous les portlets associés à un tableau de bord spécifique et les retourne sous forme de DTOs, incluant les informations de leurs charts respectifs.



}