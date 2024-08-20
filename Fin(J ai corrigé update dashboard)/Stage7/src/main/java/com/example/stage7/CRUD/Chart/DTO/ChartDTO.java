package com.example.stage7.CRUD.Chart.DTO;

import com.example.stage7.CRUD.BusinessEntity.BusinessEntity;
import com.example.stage7.CRUD.Chart.entity.charttype;
import com.example.stage7.CRUD.Datasource.DTO.DatasourceDTO;
import com.example.stage7.CRUD.Portlet.DTO.PortletDTO;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
/*Data Transfer Object (DTO). Un DTO est utilisé pour transférer des données entre différentes couches d'une application, généralement entre le backend et le frontend, ou entre différentes parties du backend lui-même.*/
public class ChartDTO extends BusinessEntity {
    private String title;
    private charttype type;
    private String x_axis;
    private String y_axis;
    private String aggreg;
    private String index;
    private DatasourceDTO datasource;
    private PortletDTO portlet;
}


  /*  permet de simplifier les interactions entre les différentes couches de l'application sans exposer directement les entités de la base de données, favorisant une gestion plus sécurisée et plus flexible des données.*/
/*tiliser des DTOs : En utilisant des Data Transfer Objects (DTO), vous ne sérialisez que les données pertinentes sans inclure directement les relations complexes entre les entités. Vous choisissez explicitement les champs à inclure, ce qui permet de rompre la boucle.*/