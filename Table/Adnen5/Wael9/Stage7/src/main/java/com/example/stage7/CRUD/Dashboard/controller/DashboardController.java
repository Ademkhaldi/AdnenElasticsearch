package com.example.stage7.CRUD.Dashboard.controller;

import com.example.stage7.CRUD.BusinessEntity.BusinessEntity;
import com.example.stage7.CRUD.Dashboard.DTO.DashboardDTO;
import com.example.stage7.CRUD.Dashboard.Service.IDashboardService;
import com.example.stage7.CRUD.Dashboard.entity.Dashboard;
import com.example.stage7.CRUD.Dashboard.repository.DashboardRepository;
import com.example.stage7.CRUD.Portlet.DTO.PortletDTO;
import com.example.stage7.CRUD.Portlet.entity.Portlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@CrossOrigin()
@RestController
@FeignClient(name = "APIgateway")
@RequestMapping("/dashboards")
public class DashboardController {


    @Autowired
    private IDashboardService dashboardService;

    @Autowired
    private DashboardRepository dashboardRepository;

/*
    @GetMapping("/getAllDashboards")
    public List<Dashboard> getAllDashboards() {
        List<Dashboard> dashboards = dashboardService.getAllDashboards();

        return dashboards;
    }
  */

    @GetMapping("/getAllDashboards")
    public List<DashboardDTO> getAllDashboards() {
        List<Dashboard> dashboards = dashboardService.getAllDashboards();
        List<DashboardDTO> dashboardDTOs = new ArrayList<>();
        for (Dashboard dashboard : dashboards) {
            DashboardDTO dashboardDTO = convertToDTO(dashboard);
            dashboardDTOs.add(dashboardDTO);
        }
        return dashboardDTOs;
    }

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
    }


    @GetMapping("/{id}")
    public DashboardDTO retrieveDashboard(@PathVariable("id") String id) {
        Dashboard dashboard = dashboardService.retrieveDashboard(id);
        return convertToDTO(dashboard);
    }

    @PostMapping("/Add")
    public ResponseEntity<Dashboard> createDashboard(@RequestBody Dashboard dashboard) {
        Dashboard createdDashboard = dashboardService.createDashboard(dashboard);
        return new ResponseEntity<>(createdDashboard, HttpStatus.CREATED);
    }

    @PutMapping("/Update/{id}")
    public ResponseEntity<Map<String, Object>> updateDashboard(@PathVariable String id, @RequestBody Dashboard dashboard) {
        Dashboard updatedDashboard = dashboardService.updateDashboard(id, dashboard);
        if (updatedDashboard != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Dashboard updated successfully");
            response.put("dashboard", updatedDashboard);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Dashboard not found with id: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<String> deleteDashboard(@PathVariable String id) {
        boolean deleted = dashboardService.deleteDashboard(id);
        if (deleted) {
            return new ResponseEntity<>("Dashboard removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Il n'y a aucun Dashboard à supprimer", HttpStatus.NOT_FOUND);
        }

    }


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
    }
    //Affectation
    // Endpoint pour assigner une liste de variables à une colonne

    @PostMapping("/{idDashboard}/assignerListePortletsADashboard")
    public Dashboard assignerListePortletsADashboard(@PathVariable String idDashboard, @RequestBody List<Portlet> portlets) {
        return dashboardService.assignerListePortletsADashboard(idDashboard, portlets);
    }

    @GetMapping("/getPortletsForDashboard/{dashboardId}")
    public ResponseEntity<Map<String, Object>> getPortletsForDashboard(@PathVariable("dashboardId") String dashboardId) {
        Set<Portlet> portlets = dashboardService.getPortletsForDashboard(dashboardId);
        if (portlets != null) {
            DashboardDTO dashboardDTO = convertToDTO(dashboardService.retrieveDashboard(dashboardId));
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Portlets retrieved successfully for dashboard: " + dashboardId);
            response.put("dashboard", dashboardDTO);
            response.put("portlets", portlets);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
              return new ResponseEntity<>(Collections.singletonMap("message", "Dashboard not found with id: " + dashboardId), HttpStatus.NOT_FOUND);
        }
    }




/*

    @GetMapping("/getPortletsForDashboard/{dashboardId}")
    public ResponseEntity<Map<String, Object>> getPortletsForDashboard(@PathVariable("dashboardId") String dashboardId) {
        Set<Portlet> portlets = dashboardService.getPortletsForDashboard(dashboardId);
        if (portlets != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("portlets", portlets);
            response.put("message", "Portlets are assigned successfully to dashboard:"+dashboardId);
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Portlets are not assigned successfully to dashboard:"+dashboardId);
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
    }


*/






}