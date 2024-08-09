package com.example.stage7.CRUD.Utilities;

import com.example.stage7.CRUD.Datasource.entity.Datasource;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ElasticService {

    List<String> getAllIndexByDatasourceId(String id) throws IOException;

    List<String> getAttributeByIndex(String idDatasource, String index) throws IOException;

    List<Map<String,Object>> getChartDataFromIndex(Datasource datasource, String index , String fieldX , String fieldY);

    Map<String,List<Map<String,Object>>> getAggregatedChartDataFromIndex(Datasource datasource,String index , String fieldX , String fieldY , String fieldAgg);
    List<Map<String, Object>> camambert(Datasource datasource, String index, String xAxisField, String yAxisField);

    List<Map<String, Object>> table(Datasource datasource, String index, String xAxisField, String yAxisField);


    public Map<String, List<Map<String, Object>>> histogramme2(Datasource datasource, String index, String xAxisField, String yAxisField, String aggField);

   /* public Map<String, List<Map<String, Object>>> getAggregatedChartDataFromIndex2(
            Datasource datasource, String index, String fieldX, String fieldY, String fieldAgg);
*/
    }