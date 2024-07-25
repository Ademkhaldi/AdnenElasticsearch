package com.example.stage7.CRUD.Chart.Service;


import com.example.stage7.CRUD.Chart.entity.Chart;
import com.example.stage7.CRUD.Chart.repository.ChartRepository;
import com.example.stage7.CRUD.Datasource.entity.Datasource;
import com.example.stage7.CRUD.Datasource.repository.DatasourceRepository;
import com.example.stage7.CRUD.Portlet.entity.Portlet;
import com.example.stage7.CRUD.Portlet.repository.PortletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChartService implements IChartService {

    @Autowired
    private ChartRepository chartRepository;

   @Autowired
   private PortletRepository portletRepository;
    @Autowired
    private DatasourceRepository datasourceRepository;

    @Override

    public List<Chart> getAllCharts() {
        return chartRepository.findAll();
    }

    @Override
    public Chart retrieveChart(String id) {
        return chartRepository.findById(id).orElse(null);
    }

    @Override
   /* public Chart createChart(Chart chart) {
        chart.setCreationDate(new Date()); // Utilise la date et l'heure actuelles lors de la création
        chart.setUpdate_date(chart.getCreationDate()); // Met à jour la date de création
        return chartRepository.save(chart);
    }*/
    public Chart createChart(Chart chart, String idDatasource) {
        // Vérifiez si le portlet avec l'ID donné existe
        Optional<Datasource> optionalDatasource = datasourceRepository.findById(idDatasource);

        // Définir les dates de création et de mise à jour
        chart.setCreationDate(new Date()); // Utilise la date et l'heure actuelles lors de la création
        chart.setUpdate_date(chart.getCreationDate()); // Met à jour la date de création

        // Si le portlet est trouvé, l'associer au chart
        if (optionalDatasource.isPresent()) {
            Datasource datasource = optionalDatasource.get();
            chart.setDatasource(datasource);
        }

        // Sauvegarder le chart
        return chartRepository.save(chart);
    }

    @Override
    public Chart updateChart(String id, Chart chart) {
        Optional<Chart> existingChartOptional = chartRepository.findById(id);
        if (existingChartOptional.isPresent()) {
            Chart existingChart = existingChartOptional.get();
            existingChart.setTitle(chart.getTitle());
            existingChart.setType(chart.getType());
            existingChart.setX_axis(chart.getX_axis());
            existingChart.setY_axis(chart.getY_axis());
            existingChart.setIndex(chart.getIndex());

            // Mise à jour de la date de mise à jour et de l'identifiant du metteur à jour
            existingChart.setUpdate_date(new Date());
            existingChart.setUpdator_id(chart.getUpdator_id());

            return chartRepository.save(existingChart);
        } else {
            return null; // Gérer l'absence de l'élément à mettre à jour comme vous le souhaitez
        }
    }


    @Override

    public boolean deleteChart(String id) {
        if (chartRepository.existsById(id)) {
            chartRepository.deleteById(id);
            return true;
        } else {
            return false; // Gérer l'absence de l'élément à supprimer comme vous le souhaitez
        }
    }



    @Override
// Méthode pour construire l'URL Elasticsearch
    public String getElasticsearchUrl(String idChart) {
        Optional<Chart> optionalChart = chartRepository.findById(idChart);
        if (optionalChart.isPresent()) {
            Chart chart = optionalChart.get();
            Datasource datasource = chart.getDatasource();
            if (chart != null) {
                return "http://" + datasource.getUrl() + "/" + chart.getIndex() + "/_search";
            }
        }
        return null; // Replace with actual logic
    }

    @Override
    public boolean deleteAllCharts() {
        long countBeforeDelete = chartRepository.count();
        chartRepository.deleteAll();
        long countAfterDelete = chartRepository.count();
        return countBeforeDelete != countAfterDelete;

    }



    @Override
    public Chart retrieveTitle(String title) {
        return chartRepository.findByTitle(title).orElse(null);
    }


    //Affectation

    @Override
    public boolean affecterDatasourceAChart(String idChart, String idDatasource) {
        Optional<Chart> optionalChart = chartRepository.findById(idChart);
        Optional<Datasource> optionalDatasource = datasourceRepository.findById(idDatasource);

        if (optionalChart.isPresent() && optionalDatasource.isPresent()) {
            Chart chart = optionalChart.get();
            Datasource datasource = optionalDatasource.get();
            chart.setDatasource(datasource);
            chartRepository.save(chart);
            return true;
        } else {
            return false;
        }
    }


    public boolean affecterPortletAChart(String idChart,String idPortlet) {
        Optional<Chart> optionalChart = chartRepository.findById(idChart);
        Optional<Portlet> optionalPortlet = portletRepository.findById(idPortlet);

        if (optionalChart.isPresent() && optionalPortlet.isPresent()) {
            Chart chart = optionalChart.get();
            Portlet portlet = optionalPortlet.get();
            chart.setPortlet(portlet);
            chartRepository.save(chart);
            return true;

        } else {
            // Gérer le cas où le portlet ou le tableau de bord n'est pas trouvé
            // Vous pouvez lancer une exception appropriée ou renvoyer null, selon vos besoins
            //System.out.println("Portlet ou Dashboard non trouvé");
            return false;

        }
    }










}