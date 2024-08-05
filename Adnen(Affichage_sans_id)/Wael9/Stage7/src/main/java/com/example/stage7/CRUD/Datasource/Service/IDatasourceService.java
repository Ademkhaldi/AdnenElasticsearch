package com.example.stage7.CRUD.Datasource.Service;

import com.example.stage7.CRUD.Chart.entity.Chart;
import com.example.stage7.CRUD.Datasource.entity.Datasource;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IDatasourceService {


    List<Datasource> getAllDatasources();

    Datasource retrieveDatasource(String id);

    Datasource createDatasource(Datasource datasource);

    Datasource updateDatasource(String id, Datasource datasource);


    boolean deleteDatasource(String id);

    boolean deleteAllDatasources();






    //Affectation
    boolean affecterChartADatasource(String idDatasource, String idChart);

    RestHighLevelClient startRestClient(Datasource datasource);
     void closeRestClient(RestHighLevelClient client) throws IOException;



    }
