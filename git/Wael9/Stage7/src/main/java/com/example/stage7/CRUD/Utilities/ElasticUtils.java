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


    @Override
    public List<Map<String, Object>> camambert(Datasource datasource, String index, String xAxisField, String yAxisField) {
        // Call the function to get raw chart data from index
        List<Map<String, Object>> rawData = getChartDataFromIndex(datasource, index, xAxisField, yAxisField);

        // Create a list to store formatted data for the pie chart
        List<Map<String, Object>> formattedData = new ArrayList<>();

        // Use a map to aggregate values by asset type (xAxisField)
        Map<String, Float> aggregatedData = new HashMap<>();

        // Process the raw data
        for (Map<String, Object> dataPoint : rawData) {
            String assetType = (String) dataPoint.get(xAxisField);
            String valueString = (String) dataPoint.get(yAxisField);

            try {
                // Try to convert value to float
                Float value = Float.parseFloat(valueString);
                // Aggregate values for each asset type
                aggregatedData.put(assetType, aggregatedData.getOrDefault(assetType, 0.0f) + value);
            } catch (NumberFormatException e) {
                // Handle non-numeric values
                System.err.println("Invalid number format for value: " + valueString);
                // Optionally: Skip or handle the entry in a specific way
            }
        }

        // Format the data for the pie chart
        for (Map.Entry<String, Float> entry : aggregatedData.entrySet()) {
            Map<String, Object> chartEntry = new HashMap<>();
            chartEntry.put("label", entry.getKey()); // Asset name
            chartEntry.put("value", entry.getValue()); // Total value
            formattedData.add(chartEntry);
        }

        return formattedData;
    }

    /*
    @Override
    public List<Map<String, Object>> camambert(Datasource datasource, String index, String xAxisField, String yAxisField) {
        // Appel à la fonction getChartDataFromIndex
        List<Map<String, Object>> rawData = getChartDataFromIndex(datasource, index, xAxisField, yAxisField);

        // Créer une liste pour stocker les données formatées pour le graphique Camembert
        List<Map<String, Object>> formattedData = new ArrayList<>();

        // Utiliser un Map pour agréger les valeurs pour 'Bonds' et 'Equities'
        Map<String, Float> aggregatedData = new HashMap<>();
        aggregatedData.put("Bonds", 0.0f);
        aggregatedData.put("Equities", 0.0f);

        // Traitement des données brutes
        for (Map<String, Object> dataPoint : rawData) {
            String assetType = (String) dataPoint.get(xAxisField);
            Float value = Float.parseFloat((String) dataPoint.get(yAxisField)); // Convertir en float

            // Vérifier et agréger les valeurs pour 'Bonds' et 'Equities'
            if (assetType.equals("Bonds") || assetType.equals("Equities")) {
                aggregatedData.put(assetType, aggregatedData.get(assetType) + value);
            }
        }

        // Formater les données pour le graphique Camembert
        for (Map.Entry<String, Float> entry : aggregatedData.entrySet()) {
            Map<String, Object> chartEntry = new HashMap<>();
            chartEntry.put("label", entry.getKey()); // Nom de l'actif
            chartEntry.put("value", entry.getValue()); // Valeur totale en float
            formattedData.add(chartEntry);
        }

        return formattedData;
    }
*/
}
