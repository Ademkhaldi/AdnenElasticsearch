package com.example.stage7.CRUD.Datasource.Service;

import com.example.stage7.CRUD.Chart.entity.Chart;
import com.example.stage7.CRUD.Chart.repository.ChartRepository;
import com.example.stage7.CRUD.Datasource.entity.Datasource;
import com.example.stage7.CRUD.Datasource.repository.DatasourceRepository;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.*;

@Service
public class DatasourceService implements IDatasourceService {

    @Autowired
    private DatasourceRepository datasourceRepository;

    @Autowired
    private ChartRepository chartRepository;

    @Override

    public List<Datasource> getAllDatasources() {
        return datasourceRepository.findAll();
    }
    @Override
    public Datasource retrieveDatasource(String id) {
        return datasourceRepository.findById(id).orElse(null);
    }    @Override


    public Datasource createDatasource(Datasource datasource) {
        datasource.setCreationDate(new Date()); // Utilise la date et l'heure actuelles lors de la création
        datasource.setUpdate_date(datasource.getCreationDate()); // Met à jour la date de création
        return datasourceRepository.save(datasource);
    }
    @Override
    public Datasource updateDatasource(String id, Datasource datasource) {
        Optional<Datasource> existingDatasourceOptional = datasourceRepository.findById(id);
        if (existingDatasourceOptional.isPresent()) {
            Datasource existingDatasource = existingDatasourceOptional.get();
            existingDatasource.setType(datasource.getType());
            existingDatasource.setConnection_port(datasource.getConnection_port());
            existingDatasource.setUrl(datasource.getUrl());
            existingDatasource.setUser(datasource.getUser());
            existingDatasource.setPassword(datasource.getPassword());


            // Mise à jour de la date de mise à jour et de l'identifiant du metteur à jour
            existingDatasource.setUpdate_date(new Date());
            existingDatasource.setUpdator_id(datasource.getUpdator_id());

            return datasourceRepository.save(existingDatasource);
        } else {
            return null; // Gérer l'absence de l'élément à mettre à jour comme vous le souhaitez
        }
    }

    @Override

    public boolean deleteDatasource(String id) {
        if (datasourceRepository.existsById(id)) {
            datasourceRepository.deleteById(id);
            return true;
        } else {
            return false; // Gérer l'absence de l'élément à supprimer comme vous le souhaitez
        }
    }

    @Override
    public boolean deleteAllDatasources() {
        long countBeforeDelete = datasourceRepository.count();
        datasourceRepository.deleteAll();
        long countAfterDelete = datasourceRepository.count();
        return countBeforeDelete != countAfterDelete;

    }





    //Affectation


    @Override
    public Datasource assignerListeChartsADatasource(String id, List<Chart> charts) {
        Optional<Datasource> optionalDatasource = datasourceRepository.findById(id);

        if (optionalDatasource.isPresent()) {
            Datasource datasource = optionalDatasource.get();

            // Récupérer les portlets actuelles du tableau de bord
            List<Chart> existingCharts = datasource.getCharts();

            // Vérifier si la liste de portlets existantes est null
            if (existingCharts == null) {
                existingCharts = new ArrayList<>();
            }

            // Parcourir la liste des nouveaux portlets pour créer ceux avec ID null
            for (Chart chart : charts) {
                // Vérifier si l'ID du portlet est null ou s'il est déjà présent dans la liste
                if (chart.getId() == null || !containsChart(existingCharts, chart.getId())) {
                    // Si l'ID du portlet est null, générer un nouvel ID pour le portlet
                    if (chart.getId() == null) {
                        String newChartId = UUID.randomUUID().toString();
                        chart.setId(newChartId);
                        // Enregistrer le portlet dans la base de données
                        chartRepository.save(chart);
                    }
                    // Ajouter le portlet à la liste existante de portlets du tableau de bord
                    existingCharts.add(chart);
                }
            }

            // Mettre à jour les portlets du tableau de bord avec la nouvelle liste
            datasource.setCharts(existingCharts);

            // Enregistrer le tableau de bord mis à jour dans la base de données
            return datasourceRepository.save(datasource);
        } else {
            // Gérer le cas où le tableau de bord n'est pas trouvé
            return null;
        }
    }


    @Override
    public Set<Chart> getChartsForDatasource(String datasourceId) {
        Optional<Datasource> optionalDatasource = datasourceRepository.findById(datasourceId);
        if (optionalDatasource.isPresent()) {
            Datasource datasource = optionalDatasource.get();
            return datasource.getCharts() != null ? new HashSet<>(datasource.getCharts()) : Collections.emptySet();
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public RestHighLevelClient startRestClient(Datasource datasource) {

        HttpHost httpHost = new HttpHost(datasource.getUrl(),datasource.getConnection_port());
        final CredentialsProvider credentialProvider = new BasicCredentialsProvider();
        credentialProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(
                        datasource.getUser(),
                        datasource.getPassword()
                ));

        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(httpHost)
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                            httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialProvider);
                            return httpAsyncClientBuilder;
                        }
                ));

        return client;
    }


    @Override
    public void closeRestClient(RestHighLevelClient client) throws IOException {
        client.close();
    }

    /**
     * Vérifie si un portlet avec l'ID spécifié est déjà présent dans la liste de portlets.
     */
    private boolean containsChart(List<Chart> charts, String chartId) {
        for (Chart chart : charts) {
            if (chart.getId().equals(chartId)) {
                return true;
            }
        }
        return false;
    }

}

