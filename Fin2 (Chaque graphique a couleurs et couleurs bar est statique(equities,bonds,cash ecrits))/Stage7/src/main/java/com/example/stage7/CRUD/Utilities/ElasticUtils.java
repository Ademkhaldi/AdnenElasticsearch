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
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ElasticUtils implements ElasticService {

    @Autowired
    DatasourceRepository datasourceRepository;

    @Autowired
    DatasourceService datasourceService;

//partie bigdata pour envoyé les donées d elasticsearch vers id
    @Override
//But : Cette méthode récupère tous les indices Elasticsearch disponibles pour une source de données donnée.
    public List<String> getAllIndexByDatasourceId(String id) throws IOException {

        Datasource datasource = datasourceRepository.findById(id).get();//Récupère un objet Datasource à partir de l'identifiant fourni (id).

        RestHighLevelClient client = datasourceService.startRestClient(datasource);//La méthode startRestClient(datasource) du service datasourceService est appelée pour initialiser un client Elasticsearch (RestHighLevelClient), en utilisant les informations fournies dans l'objet Datasource.
        //Ce client est utilisé pour interagir avec le cluster Elasticsearch.


        GetIndexRequest request = new GetIndexRequest("*");//Effectue une requête pour obtenir tous les indices (GetIndexRequest avec un motif de * pour tous les indices).
        GetIndexResponse response = client.indices().get(request, RequestOptions.DEFAULT);/*Exécution de la requête d'index :
Utilise le client Elasticsearch pour envoyer la requête d'index précédemment créée.
La méthode client.indices().get(request, RequestOptions.DEFAULT) envoie la requête et récupère la réponse de type GetIndexResponse.
RequestOptions.DEFAULT utilise les paramètres par défaut pour la requête.*/
        String[] indices = response.getIndices();/*Récupération des indices :
À partir de la réponse (response), la méthode getIndices() récupère un tableau (String[]) contenant les noms de tous les indices trouvés dans Elasticsearch.*/
        return Arrays.stream(indices).toList();//Retourne une liste de noms d'indices.
/*Conversion en liste et retour :
Convertit le tableau indices en un flux (Stream<String>) avec Arrays.stream(indices).
Utilise la méthode toList() pour transformer ce flux en une liste de chaînes de caractères (List<String>).
La méthode retourne cette liste, contenant tous les noms d'indices Elasticsearch.*/

    }


    @Override
//But : Cette méthode récupère les attributs (champs) disponibles pour un index Elasticsearch spécifique.
    public List<String> getAttributeByIndex(String idDatasource, String index) throws IOException {
        Datasource datasource = datasourceRepository.findById(idDatasource).orElse(null);//Récupère un objet Datasource à partir de l'identifiant fourni (idDatasource).

        if (datasource == null) {
            return Collections.emptyList();
        }
        RestHighLevelClient client = datasourceService.startRestClient(datasource);//Crée un client Elasticsearch haute-niveau//RestHighLevelClient permet de se connecter à un ou plusieurs nœuds d'un cluster Elasticsearch et d'exécuter des opérations telles que l'indexation, la recherche, la mise à jour, et la suppression de documents.
//datasourceService.startRestClient(datasource) : Utilise le service datasourceService pour créer et démarrer un client Elasticsearch en utilisant les informations de la source de données.

        GetMappingsRequest request = new GetMappingsRequest();//Effectue une requête pour obtenir la cartographie des champs pour l'index spécifié (GetMappingsRequest).

        request.indices(index);
        GetMappingsResponse getMappingResponse = client.indices().getMapping(request, RequestOptions.DEFAULT);//Crée une variable pour stocker la réponse de la requête de cartographie.//Envoie la requête au client Elasticsearch pour obtenir la cartographie des champs de l'index.
        Map<String, Object> result;//Map<String, Object> result; : Crée une variable pour stocker les résultats de la cartographie des champs.

        try {// Essaie de récupérer la cartographie des champs et gère les exceptions potentielles.
            result = (Map<String, Object>) getMappingResponse.mappings().get(index).getSourceAsMap().get("properties");//Récupère la cartographie des propriétés de l'index en convertissant le résultat en une carte (Map<String, Object>). Si l'index ou les propriétés sont absents, cela peut lancer une exception.
        } catch (Exception e) {
            return Collections.emptyList();
        }

        datasourceService.closeRestClient(client);//Ferme le client Elasticsearch pour libérer les ressources après l'utilisation.

        // List of fields to exclude
        List<String> excludeFields = Arrays.asList("path", "@timestamp", "@version", "host", "message", "type");//Exclut certains champs par défaut et retourne la liste des attributs disponibles.


        return result.keySet().stream()//Convertit les clés de la cartographie des propriétés en un flux (stream) pour traitement.
                .filter(field -> !excludeFields.contains(field))//Filtre les clés pour exclure celles présentes dans la liste excludeFields.
                .collect(Collectors.toList());//Collecte les clés restantes dans une liste et la retourne.
    }

    /*En résumé, cette méthode se connecte à Elasticsearch, récupère la cartographie des champs d'un index spécifique, exclut certains champs communs, et retourne les noms des attributs disponibles sous forme de liste.*/
/*En résumé, la cartographie dans Elasticsearch est cruciale pour définir comment les documents sont structurés, indexés, et recherchés. Elle permet à Elasticsearch de traiter efficacement les données en fonction des types et des structures définis dans le mapping.*/


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
    //But : Cette méthode récupère des données pour les champs spécifiés d'un index, les renvoyant sous forme de liste de cartes (maps).
public List<Map<String, Object>> getChartDataFromIndex(Datasource datasource, String index, String fieldX, String fieldY) {
    // Crée une liste pour stocker les résultats de la recherche.

    List<Map<String, Object>> results = new ArrayList<>();
    // Crée une requête de recherche pour l'index spécifié.

    SearchRequest searchRequest = new SearchRequest(index);
    // Configure les paramètres de la recherche, tels que les champs à inclure ou à exclure.

    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    // Définit la requête pour rechercher tous les documents dans l'index.

    sourceBuilder.query(QueryBuilders.matchAllQuery());
    // Définit le nombre maximum de documents à récupérer (ici 10 000 documents).

    sourceBuilder.size(10000); // Adjust size as needed to ensure all documents are retrieved
    // Spécifie les champs à inclure dans les résultats (fieldX et fieldY) et aucun champ à exclure.

    String[] includes = new String[]{fieldX, fieldY};
    String[] excludes = new String[]{};
    sourceBuilder.fetchSource(new FetchSourceContext(true, includes, excludes));
    // Associe les paramètres de recherche à la requête de recherche.
    searchRequest.source(sourceBuilder);
    // Démarre un client Elasticsearch pour interagir avec le cluster.
    RestHighLevelClient client = datasourceService.startRestClient(datasource);

    try {
        // Exécute la requête de recherche et obtient la réponse.
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("Search Response: " + searchResponse.toString());
        // Récupère les résultats de recherche.
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        // Parcourt les résultats de la recherche et les ajoute à la liste de résultats.
        for (SearchHit hit : searchHits) {
            results.add(hit.getSourceAsMap());
        }
    } catch (Exception e) {
        // Affiche une trace de la pile en cas d'exception.
        e.printStackTrace();
    } finally {
        try {
            // Ferme le client Elasticsearch pour libérer les ressources.
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Retourne la liste des résultats de recherche.
    return results;
}
/*En résumé, cette fonction interagit avec un cluster Elasticsearch pour récupérer des données spécifiques à partir d'un index donné, se concentrant sur les champs fieldX et fieldY, et retourne les résultats sous forme de liste de maps.*/
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
        // Crée une map pour stocker les données agrégées avec les clés correspondant aux valeurs de l'agrégation.
        Map<String, List<Map<String, Object>>> aggregatedData = new HashMap<>();
        // Crée une requête de recherche pour l'index spécifié.
        SearchRequest searchRequest = new SearchRequest(index);
        // Configure les paramètres de la recherche.
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // Définit la requête pour rechercher tous les documents dans l'index.
        sourceBuilder.query(QueryBuilders.matchAllQuery());

        // Specify the fields to retrieve
        // Spécifie les champs à inclure dans les résultats (fieldX et fieldY) et aucun champ à exclure.
        String[] includes = new String[]{fieldX, fieldY};
        String[] excludes = new String[]{};
        sourceBuilder.fetchSource(new FetchSourceContext(true, includes, excludes));

        // Ajoute une agrégation par terme sur le champ spécifié (fieldAgg).
        sourceBuilder.aggregation(AggregationBuilders.terms("agg").field(fieldAgg));
        searchRequest.source(sourceBuilder);

        // Increase the size of the results returned
        // Augmente le nombre maximum de résultats retournés par la recherche principale.
        sourceBuilder.size(120); // Adjust the size as needed
        // Démarre un client Elasticsearch pour interagir avec le cluster.
        RestHighLevelClient client = datasourceService.startRestClient(datasource);

        try {
            // Exécute la requête de recherche et obtient la réponse.
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            // Récupère les agrégations de la réponse de recherche.
            Aggregations aggregations = searchResponse.getAggregations();
            Terms terms = aggregations.get("agg");
            // Parcourt chaque seau (bucket) de l'agrégation.
            for (Terms.Bucket bucket : terms.getBuckets()) {
                String key = bucket.getKeyAsString();
                List<Map<String, Object>> bucketData = new ArrayList<>();

                // Retrieve documents in each bucket
                bucket.getDocCount();
                // Crée une nouvelle requête pour récupérer les documents dans chaque seau.
                SearchSourceBuilder bucketSourceBuilder = new SearchSourceBuilder();
                bucketSourceBuilder.query(QueryBuilders.termQuery(fieldAgg, key));
                bucketSourceBuilder.fetchSource(new FetchSourceContext(true, includes, excludes));

                // Add sorting by fieldX
                // Ajoute un tri par fieldX.
                bucketSourceBuilder.sort(fieldX, SortOrder.ASC);

                // Increase the size of the results returned for each bucket
                // Augmente le nombre maximum de résultats retournés pour chaque seau.
                bucketSourceBuilder.size(120); // Adjust the size as needed
                // Crée une nouvelle requête de recherche pour chaque seau.
                SearchRequest bucketSearchRequest = new SearchRequest(index).source(bucketSourceBuilder);
                // Exécute la recherche pour chaque seau et récupère les résultats.
                SearchResponse bucketSearchResponse = client.search(bucketSearchRequest, RequestOptions.DEFAULT);
                bucketSearchResponse.getHits().forEach(hit -> bucketData.add(hit.getSourceAsMap()));
                // Ajoute les données du seau à la map des données agrégées.
                aggregatedData.put(key, bucketData);
            }
        } catch (Exception e) {
            // Affiche une trace de la pile en cas d'exception.
            e.printStackTrace();
        } finally {
            try {
                // Ferme le client Elasticsearch pour libérer les ressources.
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Retourne les données agrégées.
        return aggregatedData;
    }
/*Donc dans ce contexte, oui, chaque bucket correspond bien à un type d'actif (asset type) ou à une autre valeur unique de fieldAgg*//*concept utilisé dans les agrégations pour représenter un groupe de documents qui partagent certaines caractéristiques. Les buckets sont créés lors de l'agrégation des données, en fonction des critères définis par l'utilisateur.

 */
    @Override
    public List<Map<String, Object>> camambert(Datasource datasource, String index, String xAxisField, String yAxisField) {
        // Appeler la fonction pour obtenir les données brutes du graphique à partir de l'index
        List<Map<String, Object>> rawData = getChartDataFromIndex(datasource, index, xAxisField, yAxisField);

        // Créer une liste pour stocker les données formatées pour le graphique à secteurs
        List<Map<String, Object>> formattedData = new ArrayList<>();

        // Utiliser une map pour agréger les valeurs par type d'actif (xAxisField)//Cette carte est utilisée pour stocker la somme totale des valeurs pour chaque type d'actif.
        Map<String, Double> aggregatedData = new HashMap<>();

        // Traiter les données brutes/*Parcours des données : Vous obtenez une liste de données brutes (rawData), où chaque élément est une carte représentant un point de données. Vous parcourez cette liste pour extraire et agréger les valeurs.*/
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
            /*getOrDefault : C'est une méthode de la classe Map qui retourne la valeur associée à la clé assetType. Si cette clé n'existe pas encore dans la Map, la méthode retourne la valeur par défaut spécifiée (ici 0.0).*/
        /*La ligne de code aggregatedData.put(assetType, aggregatedData.getOrDefault(assetType, 0.0) + value); permet de mettre à jour ou d'ajouter la somme des valeurs pour chaque type d'actif*/
/*Cette ligne est utilisée dans une boucle où vous traitez chaque point de données (dataPoint) extrait des données brutes. Le but est d'agréger les valeurs (value) pour chaque type d'actif (assetType).*/


        }

        // Formater les données pour le graphique à secteurs
        for (Map.Entry<String, Double> entry : aggregatedData.entrySet())//for (Map.Entry<String, Double> entry : aggregatedData.entrySet()) : Itère sur chaque entrée de aggregatedData.
/*Map.Entry<String, Double> : Map.Entry est une interface imbriquée dans Map qui représente une paire clé-valeur. Dans cet exemple, String est le type de la clé (qui représente le type d'actif) et Double est le type de la valeur (qui représente la somme des valeurs pour ce type d'actif).*/

/*Explication de entrySet()
entrySet() : Cette méthode retourne un ensemble (Set) de toutes les paires clé-valeur (ou entrées) contenues dans la carte (Map). Chaque élément dans cet ensemble est un objet de type Map.Entry.*/


        {
            Map<String, Object> chartEntry = new HashMap<>();//Map<String, Object> chartEntry = new HashMap<>(); : Crée une nouvelle carte pour chaque entrée.






            chartEntry.put("label", entry.getKey()); // Nom de l'actif/*chartEntry.put("label", entry.getKey()); : Ajoute le type d'actif comme étiquette.*/
            /*En résumé, chartEntry.put("label", entry.getKey()) prend le type d'actif à partir de la carte aggregatedData (où les types d'actifs sont les clés) et l'ajoute à chartEntry pour l'afficher sur le graphique à secteurs.
             */
/*En résumé, aggregatedData contient Asset Type comme clés et les valeurs totales associées comme valeurs grâce à l'agrégation des données brutes à partir de la liste rawData.






*/





            chartEntry.put("value", entry.getValue()); // Valeur totale/*Ajoute la valeur totale associée à cette étiquette.*/
            formattedData.add(chartEntry);/*formattedData.add(chartEntry); : Ajoute la carte formatée à la liste formattedData.
             */
        }

        return formattedData;/*return formattedData; : Retourne la liste des données formatées, prête à être utilisée pour le graphique à secteurs.
         */
    }
    private static final DecimalFormat df = new DecimalFormat("#.00");

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
/*reduce : C'est une opération de réduction qui combine les éléments du flux en une seule valeur.
 *//*0.0 : C'est la valeur initiale pour la réduction. Si la collection est vide, reduce retournera 0.0.
         */
/*Double::sum : C'est une méthode de la classe Double qui sert de fonction pour additionner les éléments du flux. Elle est équivalente à x + y.
 */
/*La ligne Double totalValue = aggregatedData.values().stream().reduce(0.0, Double::sum); calcule la somme totale de toutes les valeurs agrégées dans la Map. Cela permet de créer une entrée pour la somme totale dans le tableau final formaté.
*/
/*reduce démarre avec une valeur initiale de 0.0 et ajoute à chaque étape la valeur du prochain élément.*/
/*La fonction Double::sum est utilisée pour effectuer l'addition à chaque étape.*/
/*Vous obtenez un flux (Stream<Double>) des valeurs de la map aggregatedData. Ces valeurs sont les totaux agrégés pour chaque type d'actif.*/
        // Formater les données pour le tableau
        for (Map.Entry<String, Double> entry : aggregatedData.entrySet()) {
            Map<String, Object> tableEntry = new HashMap<>();
            tableEntry.put("label", entry.getKey()); // Nom de l'actif
            tableEntry.put("value", df.format(entry.getValue())); // Valeur totale formatée
            formattedData.add(tableEntry);
        }

        // Ajouter une entrée pour la somme totale des valeurs
        Map<String, Object> totalEntry = new HashMap<>();
        totalEntry.put("label", "Total"); // Label pour la somme totale
        totalEntry.put("value", df.format(totalValue)); // Valeur totale formatée
        formattedData.add(totalEntry);

        return formattedData;
    }

/*Un Stream:un outil puissant pour le traitement de collections d'éléments, permettant une approche fonctionnelle et
expressive pour effectuer des opérations complexes sur les données. Il facilite la manipulation de collections
de manière fluide et offre des fonctionnalités pour traiter les données de manière efficace et concise.
 */
/*Aggréger signifie regrouper ou combiner des données en fonction de certains critères pour obtenir une vue d'ensemble ou des résumés utiles. */

    @Override
    public Map<String, List<Map<String, Object>>> histogramme2(Datasource datasource, String index, String fieldX, String fieldY, String fieldAgg) {
        // 1. Appeler la fonction pour obtenir les données agrégées
        Map<String, List<Map<String, Object>>> aggregatedData = getAggregatedChartDataFromIndex(datasource, index, fieldX, fieldY, fieldAgg);

        // 2. Créer une map pour stocker les données formatées
        Map<String, List<Map<String, Object>>> formattedData = new HashMap<>();

        // 3. Traiter les données agrégées
        for (Map.Entry<String, List<Map<String, Object>>> entry : aggregatedData.entrySet()) {
            String assetType = entry.getKey(); // Obtenir le type d'actif (clé)
            List<Map<String, Object>> bucketData = entry.getValue(); // Obtenir les données associées à ce type d'actif

            // 4. Créer une liste pour les données formatées pour cet assetType
            List<Map<String, Object>> formattedBucketData = new ArrayList<>();

            // 5. Traiter chaque point de données dans le bucket
            for (Map<String, Object> dataPoint : bucketData) {
                Map<String, Object> formattedDataPoint = new HashMap<>();

                // 6. Extraire et formater les valeurs pour label (fieldX) et value (fieldY)
                Object labelValue = dataPoint.get(fieldX); // Obtenir la valeur du champ fieldX (ex : date)
                Object valueValue = dataPoint.get(fieldY); // Obtenir la valeur du champ fieldY (ex : valeur numérique)

                if (labelValue == null || valueValue == null) {
                    // Gérer les valeurs nulles (ne pas inclure ces points de données)
                    continue;
                }

                // 7. Utiliser label et value au lieu de fieldX et fieldY
                formattedDataPoint.put("label", labelValue); // Ajouter la date ou l'étiquette
                formattedDataPoint.put("value", valueValue); // Ajouter la valeur numérique

                // 8. Ajouter le point de données formaté à la liste
                formattedBucketData.add(formattedDataPoint);
            }

            // 9. Ajouter les données formatées pour cet assetType au résultat
            formattedData.put(assetType, formattedBucketData);
        }

        // 10. Retourner les données formatées
        return formattedData;
    }


}
