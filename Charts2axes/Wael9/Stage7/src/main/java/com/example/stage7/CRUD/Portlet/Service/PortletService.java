package com.example.stage7.CRUD.Portlet.Service;

import com.example.stage7.CRUD.Chart.entity.Chart;
import com.example.stage7.CRUD.Chart.repository.ChartRepository;
import com.example.stage7.CRUD.Dashboard.entity.Dashboard;
import com.example.stage7.CRUD.Dashboard.repository.DashboardRepository;
import com.example.stage7.CRUD.Portlet.entity.Portlet;
import com.example.stage7.CRUD.Portlet.repository.PortletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PortletService implements IPortletService {

    @Autowired
    private PortletRepository portletRepository;

    @Autowired
    private DashboardRepository dashboardRepository;
    @Autowired
    private ChartRepository chartRepository;

    @Override

    public List<Portlet> getAllPortlets() {
        return portletRepository.findAll();
    }
    @Override
    public Portlet retrievePortlet(String id) {
        return portletRepository.findById(id).orElse(null);
    }
    @Override
    public Portlet createPortlet(Portlet portlet) {
        portlet.setCreationDate(new Date()); // Utilise la date et l'heure actuelles lors de la création
        portlet.setUpdate_date(portlet.getCreationDate()); // Met à jour la date de création
        return portletRepository.save(portlet);
    }
    @Override
    public Portlet updatePortlet(String id, Portlet portlet) {
        Optional<Portlet> existingPortletOptional = portletRepository.findById(id);
        if (existingPortletOptional.isPresent()) {
            Portlet existingPortlet = existingPortletOptional.get();

            // Conserver la creationDate existante
            if (existingPortlet.getCreationDate() == null) {
                portlet.setCreationDate(new Date());
            } else {
                portlet.setCreationDate(existingPortlet.getCreationDate());
            }

            existingPortlet.setRow(portlet.getRow());
            existingPortlet.setColumn(portlet.getColumn());
            existingPortlet.setUpdateSystemDate(); // Mettre à jour la date de mise à jour
            existingPortlet.setUpdator_id(portlet.getUpdator_id());

            return portletRepository.save(existingPortlet);
        } else {
            return null;
        }
    }


    @Override

    public boolean deletePortlet(String id) {
        if (portletRepository.existsById(id)) {
            portletRepository.deleteById(id);
            return true;
        } else {
            return false; // Gérer l'absence de l'élément à supprimer comme vous le souhaitez
        }
    }

    @Override
    public boolean deleteAllPortlets() {
        long countBeforeDelete = portletRepository.count();
        portletRepository.deleteAll();
        long countAfterDelete = portletRepository.count();
        return countBeforeDelete != countAfterDelete;

    }
    //Affectation

    @Override
    public boolean affecterDashboardAPortlet(String idPortlet, String idDashboard) {
        Optional<Portlet> optionalPortlet = portletRepository.findById(idPortlet);
        Optional<Dashboard> optionalDashboard = dashboardRepository.findById(idDashboard);

        if (optionalPortlet.isPresent() && optionalDashboard.isPresent()) {
            Portlet portlet = optionalPortlet.get();
            Dashboard dashboard = optionalDashboard.get();
            portlet.setDashboard(dashboard);
            portletRepository.save(portlet);
            return true;

        } else {
            // Gérer le cas où le portlet ou le tableau de bord n'est pas trouvé
            // Vous pouvez lancer une exception appropriée ou renvoyer null, selon vos besoins
            //System.out.println("Portlet ou Dashboard non trouvé");
            return false;

        }
    }

    public boolean affecterChartAPortlet(String idPortlet, String idChart) {
        Optional<Portlet> optionalPortlet = portletRepository.findById(idPortlet);
        Optional<Chart> optionalChart = chartRepository.findById(idChart);

        if (optionalPortlet.isPresent() && optionalChart.isPresent()) {
            Portlet portlet = optionalPortlet.get();
            Chart chart = optionalChart.get();
            portlet.setChart(chart);
            portletRepository.save(portlet);
            return true;

        } else {
            // Gérer le cas où le portlet ou le tableau de bord n'est pas trouvé
            // Vous pouvez lancer une exception appropriée ou renvoyer null, selon vos besoins
            //System.out.println("Portlet ou Dashboard non trouvé");
            return false;

        }
    }


}
