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


    //Affectation

    @Override
    public boolean affecterChartADatasource(String idDatasource, String idChart) {
        Optional<Datasource> optionalDatasource = datasourceRepository.findById(idDatasource);
        Optional<Chart> optionalChart = chartRepository.findById(idChart);

        if (optionalDatasource.isPresent() && optionalChart.isPresent()) {
            Datasource datasource = optionalDatasource.get();
            Chart chart = optionalChart.get();
            datasource.setChart(chart);
            datasourceRepository.save(datasource);
            return true;

        } else {
            // Gérer le cas où le portlet ou le tableau de bord n'est pas trouvé
            // Vous pouvez lancer une exception appropriée ou renvoyer null, selon vos besoins
            //System.out.println("Portlet ou Dashboard non trouvé");
            return false;

        }
    }
//pour communiquer avec elasticsearch et achaque fois on fait un build et on le ferme quand on lutilise pas
    @Override
    public RestHighLevelClient startRestClient(Datasource datasource) {
        // Crée un objet HttpHost en utilisant l'URL et le port de connexion du datasource
        HttpHost httpHost = new HttpHost(datasource.getUrl(),datasource.getConnection_port());
        // Crée un CredentialsProvider pour gérer les informations d'identification
        final CredentialsProvider credentialProvider = new BasicCredentialsProvider();
        credentialProvider.setCredentials(
                AuthScope.ANY,// AuthScope définit le domaine d'application des informations d'identification
                new UsernamePasswordCredentials(
                        datasource.getUser(),// Nom d'utilisateur pour l'authentification
                        datasource.getPassword()// Mot de passe pour l'authentification
                ));
        // Crée une instance de RestHighLevelClient en configurant le client HTTP avec les informations d'identification
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(httpHost)
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                    // Configure le client HTTP pour utiliser les informations d'identification
                    httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialProvider);
                            return httpAsyncClientBuilder;
                        }
                ));
        // Retourne l'instance configurée de RestHighLevelClient
        return client;
    }
/*La fonction startRestClient crée un client Elasticsearch en utilisant les informations d'un objet Datasource. Elle configure les informations d'identification pour l'authentification, construit un client HTTP avec ces informations, et retourne l'instance du client Elasticsearch.*/

    @Override
    public void closeRestClient(RestHighLevelClient client) throws IOException {
        // Ferme l'instance de RestHighLevelClient pour libérer les ressources
        client.close();
    }
/*La fonction closeRestClient ferme le client Elasticsearch pour libérer les ressources une fois que le client n'est plus nécessaire.*/
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

