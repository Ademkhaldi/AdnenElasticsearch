package com.example.stage7.CRUD.Utilities;

import com.example.stage7.CRUD.Datasource.entity.Datasource;
import com.example.stage7.CRUD.Datasource.repository.DatasourceRepository;
import com.example.stage7.CRUD.Datasource.Service.DatasourceService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ElasticUtils implements ElasticService {

    @Autowired
    DatasourceRepository datasourceRepository;

    @Autowired
    DatasourceService datasourceService;


    @Override
    public List<String> getAllIndexByDatasourceId(String id) throws IOException {

        Datasource datasource = datasourceRepository.findById(id).get();
        RestHighLevelClient client = datasourceService.startRestClient(datasource);

        GetIndexRequest request = new GetIndexRequest("*");
        GetIndexResponse response = client.indices().get(request, RequestOptions.DEFAULT);
        String[] indices = response.getIndices();
        return Arrays.stream(indices).toList();

    }


    @Override
    public List<String> getAttributeByIndex(String idDatasource, String index) throws IOException {
        Datasource datasource = datasourceRepository.findById(idDatasource).orElse(null);
        if (datasource == null) {
            return Collections.emptyList();
        }

        RestHighLevelClient client = datasourceService.startRestClient(datasource);

        GetMappingsRequest request = new GetMappingsRequest();
        request.indices(index);
        GetMappingsResponse getMappingResponse = client.indices().getMapping(request, RequestOptions.DEFAULT);
        Map<String, Object> result;
        try {
            result = (Map<String, Object>) getMappingResponse.mappings().get(index).getSourceAsMap().get("properties");
        } catch (Exception e) {
            return Collections.emptyList();
        }

        datasourceService.closeRestClient(client);

        // List of fields to exclude
        List<String> excludeFields = Arrays.asList("path", "@timestamp", "@version", "host", "message", "type");

        return result.keySet().stream()
                .filter(field -> !excludeFields.contains(field))
                .collect(Collectors.toList());
    }
/*
    @Override
    public List<Map<String,Object>> getChartDataFromIndex(Datasource datasource, String index, String fieldX, String fieldY) {

        List<Map<String, Object>> results = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());

        String[] includes = new String[]{fieldX, fieldY};
        String[] excludes = new String[]{};
        sourceBuilder.fetchSource(new FetchSourceContext(true, includes, excludes));
        searchRequest.source(sourceBuilder);

        RestHighLevelClient client = datasourceService.startRestClient(datasource);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] searchHits = searchResponse.getHits().getHits();


            for (SearchHit hit : searchHits) {
                results.add(hit.getSourceAsMap());
            }
        }catch (Exception e)
        {

        }
        finally {
            return results;
        }

    }
*/
public List<Map<String, Object>> getChartDataFromIndex(Datasource datasource, String index, String fieldX, String fieldY) {
    List<Map<String, Object>> results = new ArrayList<>();
    SearchRequest searchRequest = new SearchRequest(index);
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    sourceBuilder.query(QueryBuilders.matchAllQuery());
    sourceBuilder.size(10000); // Adjust size as needed to ensure all documents are retrieved

    String[] includes = new String[]{fieldX, fieldY};
    String[] excludes = new String[]{};
    sourceBuilder.fetchSource(new FetchSourceContext(true, includes, excludes));
    searchRequest.source(sourceBuilder);

    RestHighLevelClient client = datasourceService.startRestClient(datasource);

    try {
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("Search Response: " + searchResponse.toString());

        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit hit : searchHits) {
            results.add(hit.getSourceAsMap());
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    return results;
}
/*
    @Override
    public Map<String,List<Map<String,Object>>> getAggregatedChartDataFromIndex(Datasource datasource , String index , String fieldX , String fieldY , String fieldAgg) {
        Map<String, List<Map<String, Object>>> aggregatedData = new HashMap<>();

        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());

        // Specify the fields to retrieve
        String[] includes = new String[]{fieldX, fieldY};
        String[] excludes = new String[]{};
        sourceBuilder.fetchSource(new FetchSourceContext(true, includes, excludes));

        // Add the aggregation
        sourceBuilder.aggregation(AggregationBuilders.terms("agg").field(fieldAgg));
        searchRequest.source(sourceBuilder);

        RestHighLevelClient client = datasourceService.startRestClient(datasource);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Terms terms = aggregations.get("agg");

            for (Terms.Bucket bucket : terms.getBuckets()) {
                String key = bucket.getKeyAsString();
                List<Map<String, Object>> bucketData = new ArrayList<>();

                // Retrieve documents in each bucket
                bucket.getDocCount();

                SearchSourceBuilder bucketSourceBuilder = new SearchSourceBuilder();
                bucketSourceBuilder.query(QueryBuilders.termQuery(fieldAgg, key));
                bucketSourceBuilder.fetchSource(new FetchSourceContext(true, includes, excludes));
                SearchRequest bucketSearchRequest = new SearchRequest(index).source(bucketSourceBuilder);

                SearchResponse bucketSearchResponse = client.search(bucketSearchRequest, RequestOptions.DEFAULT);
                bucketSearchResponse.getHits().forEach(hit -> bucketData.add(hit.getSourceAsMap()));

                aggregatedData.put(key, bucketData);
            }


        } catch (Exception e)
        {

        }
        finally {
            return aggregatedData;
        }


    }
*/
    /*

    @Override
    public Map<String, List<Map<String, Object>>> getAggregatedChartDataFromIndex(Datasource datasource, String index, String fieldX, String fieldY, String fieldAgg) {
        Map<String, List<Map<String, Object>>> aggregatedData = new HashMap<>();

        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());

        // Specify the fields to retrieve
        String[] includes = new String[]{fieldX, fieldY};
        String[] excludes = new String[]{};
        sourceBuilder.fetchSource(new FetchSourceContext(true, includes, excludes));

        // Add the aggregation
        sourceBuilder.aggregation(AggregationBuilders.terms("agg").field(fieldAgg).size(10000)); // Adjust size as needed to ensure all documents are retrieved
        searchRequest.source(sourceBuilder);

        RestHighLevelClient client = datasourceService.startRestClient(datasource);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Terms terms = aggregations.get("agg");

            for (Terms.Bucket bucket : terms.getBuckets()) {
                String key = bucket.getKeyAsString();
                List<Map<String, Object>> bucketData = new ArrayList<>();

                // Retrieve documents in each bucket
                bucket.getDocCount();

                SearchSourceBuilder bucketSourceBuilder = new SearchSourceBuilder();
                bucketSourceBuilder.query(QueryBuilders.termQuery(fieldAgg, key));
                bucketSourceBuilder.fetchSource(new FetchSourceContext(true, includes, excludes));
                bucketSourceBuilder.size(10000); // Adjust size as needed to ensure all documents are retrieved
                SearchRequest bucketSearchRequest = new SearchRequest(index).source(bucketSourceBuilder);

                SearchResponse bucketSearchResponse = client.search(bucketSearchRequest, RequestOptions.DEFAULT);
                bucketSearchResponse.getHits().forEach(hit -> bucketData.add(hit.getSourceAsMap()));

                aggregatedData.put(key, bucketData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return aggregatedData;
    }
    */
  /*

    @Override
    public Map<String, List<Map<String, Object>>> getAggregatedChartDataFromIndex(Datasource datasource, String index, String fieldX, String fieldY, String fieldAgg) {
        Map<String, List<Map<String, Object>>> aggregatedData = new HashMap<>();

        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());

        // Specify the fields to retrieve
        String[] includes = new String[]{fieldX, fieldY};
        String[] excludes = new String[]{};
        sourceBuilder.fetchSource(new FetchSourceContext(true, includes, excludes));

        // Add the aggregation
        sourceBuilder.aggregation(AggregationBuilders.terms("agg").field(fieldAgg));
        searchRequest.source(sourceBuilder);

        RestHighLevelClient client = datasourceService.startRestClient(datasource);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Terms terms = aggregations.get("agg");

            for (Terms.Bucket bucket : terms.getBuckets()) {
                String key = bucket.getKeyAsString();
                List<Map<String, Object>> bucketData = new ArrayList<>();

                // Retrieve documents in each bucket
                bucket.getDocCount();

                SearchSourceBuilder bucketSourceBuilder = new SearchSourceBuilder();
                bucketSourceBuilder.query(QueryBuilders.termQuery(fieldAgg, key));
                bucketSourceBuilder.fetchSource(new FetchSourceContext(true, includes, excludes));

                // Add sorting by fieldX
                bucketSourceBuilder.sort(fieldX, SortOrder.ASC);

                SearchRequest bucketSearchRequest = new SearchRequest(index).source(bucketSourceBuilder);

                SearchResponse bucketSearchResponse = client.search(bucketSearchRequest, RequestOptions.DEFAULT);
                bucketSearchResponse.getHits().forEach(hit -> bucketData.add(hit.getSourceAsMap()));

                aggregatedData.put(key, bucketData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return aggregatedData;
    }

    */


    @Override
    public Map<String, List<Map<String, Object>>> getAggregatedChartDataFromIndex(Datasource datasource, String index, String fieldX, String fieldY, String fieldAgg) {
        Map<String, List<Map<String, Object>>> aggregatedData = new HashMap<>();

        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());

        // Specify the fields to retrieve
        String[] includes = new String[]{fieldX, fieldY};
        String[] excludes = new String[]{};
        sourceBuilder.fetchSource(new FetchSourceContext(true, includes, excludes));

        // Add the aggregation
        sourceBuilder.aggregation(AggregationBuilders.terms("agg").field(fieldAgg));
        searchRequest.source(sourceBuilder);

        // Increase the size of the results returned
        sourceBuilder.size(100); // Adjust the size as needed

        RestHighLevelClient client = datasourceService.startRestClient(datasource);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Terms terms = aggregations.get("agg");

            for (Terms.Bucket bucket : terms.getBuckets()) {
                String key = bucket.getKeyAsString();
                List<Map<String, Object>> bucketData = new ArrayList<>();

                // Retrieve documents in each bucket
                bucket.getDocCount();

                SearchSourceBuilder bucketSourceBuilder = new SearchSourceBuilder();
                bucketSourceBuilder.query(QueryBuilders.termQuery(fieldAgg, key));
                bucketSourceBuilder.fetchSource(new FetchSourceContext(true, includes, excludes));

                // Add sorting by fieldX
                bucketSourceBuilder.sort(fieldX, SortOrder.ASC);

                // Increase the size of the results returned for each bucket
                bucketSourceBuilder.size(100); // Adjust the size as needed

                SearchRequest bucketSearchRequest = new SearchRequest(index).source(bucketSourceBuilder);

                SearchResponse bucketSearchResponse = client.search(bucketSearchRequest, RequestOptions.DEFAULT);
                bucketSearchResponse.getHits().forEach(hit -> bucketData.add(hit.getSourceAsMap()));

                aggregatedData.put(key, bucketData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return aggregatedData;
    }

    @Override
    public List<Map<String, Object>> camambert(Datasource datasource, String index, String xAxisField, String yAxisField) {
        // Appeler la fonction pour obtenir les données brutes du graphique à partir de l'index
        List<Map<String, Object>> rawData = getChartDataFromIndex(datasource, index, xAxisField, yAxisField);

        // Créer une liste pour stocker les données formatées pour le graphique à secteurs
        List<Map<String, Object>> formattedData = new ArrayList<>();

        // Utiliser une map pour agréger les valeurs par type d'actif (xAxisField)
        Map<String, Double> aggregatedData = new HashMap<>();

        // Traiter les données brutes
        for (Map<String, Object> dataPoint : rawData) {
            // Afficher le contenu du dataPoint pour le débogage
            System.out.println("Processing dataPoint: " + dataPoint);

            // Extraire le type d'actif et la valeur depuis les champs spécifiés
            String assetType = (String) dataPoint.get(xAxisField);
            Number valueObject = (Number) dataPoint.get(yAxisField); // Utiliser Number pour gérer différents types numériques

            // Afficher les valeurs extraites pour le débogage
            System.out.println("Asset Type: " + assetType + ", Value: " + valueObject);

            if (assetType == null) {
                // Gérer les valeurs nulles pour assetType
                System.err.println("Asset Type is null in dataPoint: " + dataPoint);
                continue; // Passer à l'entrée suivante
            }

            if (valueObject == null) {
                // Gérer les valeurs nulles pour valueObject
                System.err.println("Value is null for assetType: " + assetType);
                continue; // Passer à l'entrée suivante
            }

            // Convertir valueObject en Double
            double value = valueObject.doubleValue();

            // Agréger les valeurs pour chaque type d'actif
            aggregatedData.put(assetType, aggregatedData.getOrDefault(assetType, 0.0) + value);
        }

        // Formater les données pour le graphique à secteurs
        for (Map.Entry<String, Double> entry : aggregatedData.entrySet()) {
            Map<String, Object> chartEntry = new HashMap<>();
            chartEntry.put("label", entry.getKey()); // Nom de l'actif
            chartEntry.put("value", entry.getValue()); // Valeur totale
            formattedData.add(chartEntry);
        }

        return formattedData;
    }

    @Override
    public List<Map<String, Object>> table(Datasource datasource, String index, String xAxisField, String yAxisField) {
        // Appeler la fonction pour obtenir les données brutes du graphique à partir de l'index
        List<Map<String, Object>> rawData = getChartDataFromIndex(datasource, index, xAxisField, yAxisField);

        // Créer une liste pour stocker les données formatées pour le tableau
        List<Map<String, Object>> formattedData = new ArrayList<>();

        // Utiliser une map pour agréger les valeurs par type d'actif (xAxisField)
        Map<String, Double> aggregatedData = new HashMap<>();

        // Traiter les données brutes
        for (Map<String, Object> dataPoint : rawData) {
            // Afficher le contenu du dataPoint pour le débogage
            System.out.println("Processing dataPoint: " + dataPoint);

            // Extraire le type d'actif et la valeur depuis les champs spécifiés
            String assetType = (String) dataPoint.get(xAxisField);
            Number valueObject = (Number) dataPoint.get(yAxisField); // Utiliser Number pour gérer différents types numériques

            // Afficher les valeurs extraites pour le débogage
            System.out.println("Asset Type: " + assetType + ", Value: " + valueObject);

            if (assetType == null) {
                // Gérer les valeurs nulles pour assetType
                System.err.println("Asset Type is null in dataPoint: " + dataPoint);
                continue; // Passer à l'entrée suivante
            }

            if (valueObject == null) {
                // Gérer les valeurs nulles pour valueObject
                System.err.println("Value is null for assetType: " + assetType);
                continue; // Passer à l'entrée suivante
            }

            // Convertir valueObject en Double
            double value = valueObject.doubleValue();

            // Agréger les valeurs pour chaque type d'actif
            aggregatedData.put(assetType, aggregatedData.getOrDefault(assetType, 0.0) + value);
        }

        // Calculer la somme totale des valeurs
        Double totalValue = aggregatedData.values().stream().reduce(0.0, Double::sum);

        // Formater les données pour le tableau
        for (Map.Entry<String, Double> entry : aggregatedData.entrySet()) {
            Map<String, Object> tableEntry = new HashMap<>();
            tableEntry.put("label", entry.getKey()); // Nom de l'actif
            tableEntry.put("value", entry.getValue()); // Valeur totale
            formattedData.add(tableEntry);
        }

        // Ajouter une entrée pour la somme totale des valeurs
        Map<String, Object> totalEntry = new HashMap<>();
        totalEntry.put("label", "Total"); // Label pour la somme totale
        totalEntry.put("value", totalValue); // Valeur totale
        formattedData.add(totalEntry);

        return formattedData;
    }


    @Override
    public Map<String, List<Map<String, Object>>> histogramme2(Datasource datasource, String index, String fieldX, String fieldY, String fieldAgg) {
        // Appeler la fonction pour obtenir les données agrégées
        Map<String, List<Map<String, Object>>> aggregatedData = getAggregatedChartDataFromIndex(datasource, index, fieldX, fieldY, fieldAgg);

        // Créer une map pour stocker les données formatées
        Map<String, List<Map<String, Object>>> formattedData = new HashMap<>();

        // Traiter les données agrégées
        for (Map.Entry<String, List<Map<String, Object>>> entry : aggregatedData.entrySet()) {
            String assetType = entry.getKey();
            List<Map<String, Object>> bucketData = entry.getValue();

            // Créer une liste pour les données formatées pour cet assetType
            List<Map<String, Object>> formattedBucketData = new ArrayList<>();

            // Traiter chaque point de données dans le bucket
            for (Map<String, Object> dataPoint : bucketData) {
                Map<String, Object> formattedDataPoint = new HashMap<>();

                // Extraire et formater les valeurs pour xAxis et yAxis
                Object xValue = dataPoint.get(fieldX);
                Object yValue = dataPoint.get(fieldY);

                if (xValue == null || yValue == null) {
                    // Gérer les valeurs nulles
                    continue;
                }

                formattedDataPoint.put(fieldX, xValue);
                formattedDataPoint.put(fieldY, yValue);

                // Ajouter le point de données formaté à la liste
                formattedBucketData.add(formattedDataPoint);
            }

            // Ajouter les données formatées pour cet assetType au résultat
            formattedData.put(assetType, formattedBucketData);
        }

        return formattedData;
    }


}
