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
/*@Service : Annotation qui indique que cette classe est un service, c'est-à-dire qu'elle contient la logique métier et sera gérée par Spring pour l'injection de dépendances.
 */

@Service
public class ChartService implements IChartService {
    /* Indique que Spring doit injecter les instances de ChartRepository, PortletRepository, et DatasourceRepository*/

    @Autowired
    private ChartRepository chartRepository;

   @Autowired
   private PortletRepository portletRepository;
    @Autowired
    private DatasourceRepository datasourceRepository;

    @Override
/*Cette méthode retourne une liste de tous les objets Chart en appelant la méthode findAll() du repository.*/
    public List<Chart> getAllCharts() {
        return chartRepository.findAll();
    }

    @Override
/*Cherche chart dans la base de données via son ID. Si le graphique n'est pas trouvé, retourne null.
 */
    public Chart retrieveChart(String id) {
        return chartRepository.findById(id).orElse(null);
    }

/*createChart() : Cette méthode crée un nouveau graphique :
 */
    public Chart createChart(Chart chart, String idDatasource) {
        // Vérifiez si le datasource avec l'ID donné existe
        Optional<Datasource> optionalDatasource = datasourceRepository.findById(idDatasource);

        // Définir les dates de création et de mise à jour
        chart.setCreationDate(new Date()); // Utilise la date et l'heure actuelles lors de la création
        chart.setUpdate_date(chart.getCreationDate()); // Met à jour la date de création

        // Si le datasource est trouvé, l'associer au chart
        if (optionalDatasource.isPresent()) {
            Datasource datasource = optionalDatasource.get();
            chart.setDatasource(datasource);
        }

        // Sauvegarder le chart
        return chartRepository.save(chart);
    }
    @Override
    public Chart updateChart(String id, Chart chart, String idDatasource) {
    /*Elle cherche le graphique et le datasource par leurs IDs respectifs.
     */
        Optional<Chart> existingChartOptional = chartRepository.findById(id);
        Optional<Datasource> optionalDatasource = datasourceRepository.findById(idDatasource);
/*Si le graphique est trouvé, ses propriétés sont mises à jour, ainsi que la date de mise à jour.*/
        if (existingChartOptional.isPresent()) {
            Chart existingChart = existingChartOptional.get();
            existingChart.setTitle(chart.getTitle());
            existingChart.setType(chart.getType());
            existingChart.setX_axis(chart.getX_axis());
            existingChart.setY_axis(chart.getY_axis());
            existingChart.setAggreg(chart.getAggreg());
            existingChart.setIndex(chart.getIndex());

            // Mise à jour de la date de mise à jour et de l'identifiant du metteur à jour
            existingChart.setUpdate_date(new Date());
            existingChart.setUpdator_id(chart.getUpdator_id());

            // Si le datasource est trouvé, l'associer au chart
            if (optionalDatasource.isPresent()) {
                Datasource datasource = optionalDatasource.get();
                existingChart.setDatasource(datasource);
            }

            return chartRepository.save(existingChart);
        } else {
            return null; // Gérer l'absence de l'élément à mettre à jour comme vous le souhaitez
        }
    }

    @Override
    /*Cette méthode supprime chart s'il existe dans la base de données.*/

    public boolean deleteChart(String id) {
        if (chartRepository.existsById(id)) {
            chartRepository.deleteById(id);
            return true;
        } else {
            return false; // Gérer l'absence de l'élément à supprimer comme vous le souhaitez
        }
    }



    @Override
 /*Cette méthode construit une URL Elasticsearch basée sur les informations du Chart et du Datasource associées.
  */
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
/*@Override  Cette annotation indique que la méthode est une implémentation d'une méthode déclarée dans l'interface que cette classe implémente (IChartService). Elle est utilisée pour garantir que la méthode correspond exactement à celle définie dans l'interface et signale une erreur si ce n'est pas le cas.  */
    @Override
/*Boolean : Le type de retour est un booléen. Cette méthode retournera true ou false en fonction du succès ou de l'échec de l'opération de suppression.
 */
    public boolean deleteAllCharts() {

  /*Compter les enregistrements avant la suppression
   */
/*long countBeforeDelete : Déclare une variable de type long qui stocke le nombre total de Chart présents dans la base de données avant la suppression.*/
/*chartRepository.count() : Appelle la méthode count() du ChartRepository, qui renvoie le nombre total d'enregistrements dans la collection de Chart.
 */
        long countBeforeDelete = chartRepository.count();
/*Appelle la méthode deleteAll() du ChartRepository, qui supprime tous les graphiques présents dans la base de données.*/
        chartRepository.deleteAll();
/*Compter les enregistrements après la suppression
 */
        // Déclare une autre variable de type long qui stocke le nombre total de chart après la suppression.
        //Appelle à nouveau la méthode count() pour vérifier combien de graphiques restent après la suppression (normalement, ce nombre devrait être 0).
        long countAfterDelete = chartRepository.count();
        /*Comparaison du nombre d'enregistrements avant et après la suppression
         */
       /*Compare les deux valeurs. Si le nombre de graphiques avant la suppression (countBeforeDelete) est différent de celui après la suppression (countAfterDelete), cela signifie que des graphiques ont bien été supprimés.*/
/*Si les deux nombres sont différents, la méthode retourne true (indiquant que la suppression a été effectuée)*/

        return countBeforeDelete != countAfterDelete;

    }



    @Override
    public Chart retrieveTitle(String title) {
        return chartRepository.findByTitle(title).orElse(null);
    }


    //Affectation

    @Override
    /*boolean : Le type de retour est un booléen. La méthode retournera true si l'affectation a réussi, sinon false.*/
    /*String idChart : Paramètre représentant l'identifiant du graphique auquel la source de données sera associée.
String idDatasource : Paramètre représentant l'identifiant de la source de données à associer au graphique.
*/
    public boolean affecterDatasourceAChart(String idChart, String idDatasource) {
      /*Déclare une variable de type Optional pour contenir Chart si celui-ci est trouvé dans la base de données.*/
/*chartRepository.findById(idChart) : Appelle la méthode findById() du ChartRepository pour rechercher le graphique dont l'identifiant est idChart. La méthode retourne un Optional<Chart> qui sera vide si le graphique n'existe pas, ou contiendra le graphique si trouvé.
 */
        Optional<Chart> optionalChart = chartRepository.findById(idChart);
        Optional<Datasource> optionalDatasource = datasourceRepository.findById(idDatasource);
/*Vérification de la présence des deux entités*/
/*Vérifie si le Optional<Chart> contient un chart (c'est-à-dire que le chart a été trouvé)meme chose pour datasource.*/
        if (optionalChart.isPresent() && optionalDatasource.isPresent()) {
    /*Association de la source de données au graphique*/
/*Récupère chart contenu dans l'Optional<Chart>.*/
            Chart chart = optionalChart.get();
/*Récupère datasource contenue dans l'Optional<Datasource>.*/
            Datasource datasource = optionalDatasource.get();
/*Associe datasource au chart en utilisant la méthode setDatasource() du chart.*/
            chart.setDatasource(datasource);
/* Sauvegarde le graphique mis à jour dans la base de données*/
            chartRepository.save(chart);
            return true;
        } else {
            return false;
        }
    }


    public boolean affecterPortletAChart(String idChart,String idPortlet) {
   /*Optional<Chart> optionalChart : Variable qui contient un Optional pour chart recherché.*/
 /* Appelle la méthode findById() du ChartRepository pour obtenir le graphique correspondant à l'identifiant idChart*/
        Optional<Chart> optionalChart = chartRepository.findById(idChart);
        Optional<Portlet> optionalPortlet = portletRepository.findById(idPortlet);
/*Vérification de la présence des deux entités */
        if (optionalChart.isPresent() && optionalPortlet.isPresent()) {
/*Association du portlet au chart*/
/*Récupère chart contenu dans l'Optional<Chart>.*/
            Chart chart = optionalChart.get();
            Portlet portlet = optionalPortlet.get();
/*Associe le portlet au graphique en utilisant la méthode setPortlet() du graphique.*/
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