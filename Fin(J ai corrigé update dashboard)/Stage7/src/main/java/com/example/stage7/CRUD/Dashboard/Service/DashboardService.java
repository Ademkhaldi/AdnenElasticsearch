package com.example.stage7.CRUD.Dashboard.Service;

import com.example.stage7.CRUD.Dashboard.entity.Dashboard;
import com.example.stage7.CRUD.Dashboard.repository.DashboardRepository;
import com.example.stage7.CRUD.Portlet.Service.IPortletService;
import com.example.stage7.CRUD.Portlet.entity.Portlet;
import com.example.stage7.CRUD.Portlet.repository.PortletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DashboardService implements IDashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;
    @Autowired
    private IPortletService IportletService;

    @Autowired
    private PortletRepository portletRepository;


    @Override

    public List<Dashboard> getAllDashboards() {
        return dashboardRepository.findAll();
    }//Récupère tous les tableaux de bord en appelant la méthode findAll() du repository.



    @Override
    public Dashboard retrieveDashboard(String id) {
        return dashboardRepository.findById(id).orElse(null);
    }//Récupère un tableau de bord spécifique par son id. Si le tableau de bord n'est pas trouvé, renvoie null.
    @Override

    public Dashboard createDashboard(Dashboard dashboard) {



        dashboard.setCreationDate(new Date()); // Utilise la date et l'heure actuelles lors de la création
        dashboard.setUpdate_date(dashboard.getCreationDate()); // Assure que update_date est le même que creationDate
        return dashboardRepository.save(dashboard);
    }//Crée un nouveau tableau de bord. Lors de la création, il définit la date de création et met à jour la date de modification avec la même valeur, puis enregistre le tableau de bord dans la base de données.

    @Override
    public Dashboard updateDashboard(String id, Dashboard dashboard) {
        Optional<Dashboard> existingDashboardOptional = dashboardRepository.findById(id);
        if (existingDashboardOptional.isPresent()) {
            Dashboard existingDashboard = existingDashboardOptional.get();

            // Met à jour le titre et les autres propriétés
            existingDashboard.setTitle(dashboard.getTitle());
            existingDashboard.setUpdate_date(new Date());
            existingDashboard.setUpdator_id(dashboard.getUpdator_id());

            // Conserve les portlets existants sans les remplacer
            List<Portlet> updatedPortlets = existingDashboard.getPortlets();
            if (updatedPortlets == null) {
                updatedPortlets = new ArrayList<>();
            }

            // Ajoute les nouveaux portlets reçus dans la demande
            List<Portlet> newPortlets = dashboard.getPortlets();
            if (newPortlets != null) {
                for (Portlet newPortlet : newPortlets) {
                    // Vérifie si le portlet est déjà présent dans la liste
                    if (newPortlet.getId() == null || !containsPortlet(updatedPortlets, newPortlet.getId())) {
                        // Si le portlet n'est pas présent, ajoute-le
                        if (newPortlet.getId() == null) {
                            String newPortletId = UUID.randomUUID().toString();
                            newPortlet.setId(newPortletId);
                            portletRepository.save(newPortlet);
                        }
                        updatedPortlets.add(newPortlet);
                    }
                }
            }

            existingDashboard.setPortlets(updatedPortlets);

            return dashboardRepository.save(existingDashboard);
        } else {
            return null; // Gérer l'absence de l'élément à mettre à jour comme vous le souhaitez
        }
    }
    @Override

    public boolean deleteDashboard(String id) {
        if (dashboardRepository.existsById(id)) {
            dashboardRepository.deleteById(id);
            return true;
        } else {
            return false; // Gérer l'absence de l'élément à supprimer comme vous le souhaitez
        }
    }

    @Override
    public boolean deleteAllDashboards() {
        long countBeforeDelete = dashboardRepository.count();
        dashboardRepository.deleteAll();
        long countAfterDelete = dashboardRepository.count();
        return countBeforeDelete != countAfterDelete;

    }

    //Affectation
    @Override
    public Dashboard assignerListePortletsADashboard(String id, List<Portlet> portlets) {
        Optional<Dashboard> optionalDashboard = dashboardRepository.findById(id);

        if (optionalDashboard.isPresent()) {
            Dashboard dashboard = optionalDashboard.get();

            // Récupérer les portlets actuelles du tableau de bord
            List<Portlet> existingPortlets = dashboard.getPortlets();

            // Vérifier si la liste de portlets existantes est null
            if (existingPortlets == null) {
                existingPortlets = new ArrayList<>();
            }

            // Parcourir la liste des nouveaux portlets pour créer ceux avec ID null
            for (Portlet portlet : portlets) {
                // Vérifier si l'ID du portlet est null ou s'il est déjà présent dans la liste
                if (portlet.getId() == null || !containsPortlet(existingPortlets, portlet.getId())) {
                    // Si l'ID du portlet est null, générer un nouvel ID pour le portlet
                    if (portlet.getId() == null) {
                        String newPortletId = UUID.randomUUID().toString();
                        portlet.setId(newPortletId);
                        // Enregistrer le portlet dans la base de données
                        portletRepository.save(portlet);
                    }
                    // Ajouter le portlet à la liste existante de portlets du tableau de bord
                    existingPortlets.add(portlet);
                }
            }

            // Mettre à jour les portlets du tableau de bord avec la nouvelle liste
            dashboard.setPortlets(existingPortlets);

            // Enregistrer le tableau de bord mis à jour dans la base de données
            return dashboardRepository.save(dashboard);
        } else {
            // Gérer le cas où le tableau de bord n'est pas trouvé
            return null;
        }
    }//Met à jour le titre et les portlets du tableau de bord avec les valeurs fournies.

    /**
     * Vérifie si un portlet avec l'ID spécifié est déjà présent dans la liste de portlets.
     */
    private boolean containsPortlet(List<Portlet> portlets, String portletId) {
        for (Portlet portlet : portlets) {
            if (portlet.getId().equals(portletId)) {
                return true;
            }
        }
        return false;
    }




    @Override
    public List<Portlet> getPortletsForDashboard(String dashboardId) {
        Optional<Dashboard> optionalDashboard = dashboardRepository.findById(dashboardId);
        if (optionalDashboard.isPresent()) {
            return optionalDashboard.get().getPortlets();
        }
        return Collections.emptyList();
    }


    }//Récupère la liste des portlets associés à un tableau de bord spécifique. Retourne une liste vide si le tableau de bord n'est pas trouvé.





