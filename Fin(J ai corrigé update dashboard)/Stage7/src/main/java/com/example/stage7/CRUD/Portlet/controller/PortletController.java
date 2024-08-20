package com.example.stage7.CRUD.Portlet.controller;

import com.example.stage7.CRUD.BusinessEntity.BusinessEntity;
import com.example.stage7.CRUD.Chart.DTO.ChartDTO;
import com.example.stage7.CRUD.Dashboard.DTO.DashboardDTO;
import com.example.stage7.CRUD.Dashboard.repository.DashboardRepository;
import com.example.stage7.CRUD.Portlet.DTO.PortletDTO;
import com.example.stage7.CRUD.Portlet.Service.IPortletService;
import com.example.stage7.CRUD.Portlet.entity.Portlet;
import com.example.stage7.CRUD.Portlet.repository.PortletRepository;
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
@RequestMapping("/portlets")
@FeignClient(name = "APIgateway")
public class PortletController {

    @Autowired
    private IPortletService portletService;

    @Autowired
    private PortletRepository portletRepository;

    @Autowired
    private DashboardRepository dashboardRepository;


    @GetMapping("/getAllPortlets")
    public List<PortletDTO> getAllPortlets() {
        List<Portlet> portlets = portletService.getAllPortlets();
        List<PortletDTO> portletDTOs = new ArrayList<>();
        for (Portlet portlet : portlets) {
            PortletDTO portletDTO = convertToDTO(portlet);
            portletDTOs.add(portletDTO);
        }
        return portletDTOs;
    }
    private PortletDTO convertToDTO(Portlet portlet) {

        PortletDTO portletDTO = new PortletDTO();
        portletDTO.setRow(portlet.getRow());
        portletDTO.setColumn(portlet.getColumn());


        if (portlet instanceof BusinessEntity) {
            BusinessEntity businessEntity = (BusinessEntity) portlet;
            portletDTO.setId(businessEntity.getId());
            portletDTO.setCreationDate(businessEntity.getCreationDate());
            portletDTO.setCreator_id(businessEntity.getCreator_id());
            portletDTO.setUpdate_date(businessEntity.getUpdate_date());
            portletDTO.setUpdator_id(businessEntity.getUpdator_id());
        }

        if (portlet.getChart() != null) {
            ChartDTO chartDTO = new ChartDTO();
            chartDTO.setId(portlet.getChart().getId());
            chartDTO.setTitle(portlet.getChart().getTitle());
            chartDTO.setType(portlet.getChart().getType());
            chartDTO.setX_axis(portlet.getChart().getX_axis());
            chartDTO.setY_axis(portlet.getChart().getY_axis());
            portletDTO.setChart(chartDTO);

        }
        if (portlet.getDashboard() != null) {
            DashboardDTO dashboardDTO = new DashboardDTO();
            dashboardDTO.setId(portlet.getId());
            dashboardDTO.setTitle(portlet.getDashboard().getTitle());
            portletDTO.setDashboard(dashboardDTO);
        }
        return portletDTO;
    }


    @GetMapping("/{id}")
    public PortletDTO retrievePortlet(@PathVariable("id") String id) {
        Portlet portlet = portletService.retrievePortlet(id);
        return convertToDTO(portlet);
    }


    @PostMapping("/Add")
    public ResponseEntity<Portlet> createPortlet(@RequestBody Portlet portlet) {
        Portlet createdPortlet = portletService.createPortlet(portlet);
        return new ResponseEntity<>(createdPortlet, HttpStatus.CREATED);
    }

    @PutMapping("/Update/{id}")
    public ResponseEntity<Map<String, Object>> updatePortlet(@PathVariable String id, @RequestBody Portlet portlet) {
        Portlet updatedPortlet = portletService.updatePortlet(id, portlet);
        if (updatedPortlet != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Portlet updated successfully");
            response.put("dashboard", updatedPortlet);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Portlet not found with id: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<String> deletePortlet(@PathVariable String id) {
        boolean deleted = portletService.deletePortlet(id);
        if (deleted) {
            return new ResponseEntity<>("Portlet removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Il n'y a aucun Portlet à supprimer", HttpStatus.NOT_FOUND);
        }

    }


    @DeleteMapping("/deleteAllPortlets")
    public ResponseEntity<String> deleteAllPortlets() {
        try {
            boolean deleted = portletService.deleteAllPortlets();
            if (deleted) {
                return ResponseEntity.ok("Portlets removed successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Il n'y a aucun Portlet à supprimer");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors de la suppression");
        }
    }
    //Affectation
                                                     //{idPortlet}{idDashboard}
    @PutMapping("/affecterDashboardAPortlet/{idPortlet}/{idDashboard}")
    public ResponseEntity<Map<String, String>> affecterDashboardAPortlet(
            /*{idPortlet}*/ @PathVariable("idPortlet") String idPortlet,
            /*{idDashboard}*/@PathVariable("idDashboard") String idDashboard) {

        boolean affectationReussie =portletService.affecterDashboardAPortlet(idPortlet, idDashboard);
        // Créez une carte pour stocker les informations de la réponse
        Map<String, String> response = new HashMap<>();
        if(affectationReussie){

            // Ajoutez les détails de l'affectation à la réponse
            response.put("message", "Affectation réussie");
            response.put("portletId", idPortlet);
            response.put("dashboardId", idDashboard);

            // Répondez avec un objet ResponseEntity contenant la carte de réponse
            return ResponseEntity.ok(response);
        } else {

            // Si le client ou le marché n'est pas trouvé, ajoutez un message d'erreur à la réponse
            response.put("message", "Portlet ou Dashboard non trouvé");

            // Répondez avec un statut NOT_FOUND et la carte de réponse
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }



    @PutMapping("/affecterChartAPortlet/{idPortlet}/{idChart}")
    public ResponseEntity<Map<String, String>> affecterChartAPortlet(
            /*{idPortlet}*/ @PathVariable("idPortlet") String idPortlet,
            /*{idDashboard}*/@PathVariable("idChart") String idChart) {

        boolean affectationReussie =portletService.affecterChartAPortlet(idPortlet, idChart);
        // Créez une carte pour stocker les informations de la réponse
        Map<String, String> response = new HashMap<>();
        if(affectationReussie){

            // Ajoutez les détails de l'affectation à la réponse
            response.put("message", "Affectation réussie");
            response.put("portletId", idPortlet);
            response.put("ChartId", idChart);

            // Répondez avec un objet ResponseEntity contenant la carte de réponse
            return ResponseEntity.ok(response);
        } else {

            // Si le client ou le marché n'est pas trouvé, ajoutez un message d'erreur à la réponse
            response.put("message", "Portlet ou idChart non trouvé");

            // Répondez avec un statut NOT_FOUND et la carte de réponse
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}
