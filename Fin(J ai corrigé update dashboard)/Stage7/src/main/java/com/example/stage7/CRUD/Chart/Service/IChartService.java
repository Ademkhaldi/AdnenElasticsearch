package com.example.stage7.CRUD.Chart.Service;

import com.example.stage7.CRUD.Chart.entity.Chart;

import java.util.List;

public interface IChartService {


    List<Chart> getAllCharts();

    Chart retrieveChart(String id);

//    Chart createChart(Chart chart);
     Chart createChart(Chart chart, String idDatasource);


   //     Chart updateChart(String id, Chart chart);
    Chart updateChart(String id, Chart chart, String idDatasource);

    boolean deleteChart(String id);

    boolean deleteAllCharts();

    Chart retrieveTitle(String title);



    boolean affecterDatasourceAChart(String idChart, String idDatasource);

    boolean affecterPortletAChart(String idChart,String idPortlet);

    public String getElasticsearchUrl(String idChart);

}
