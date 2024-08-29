package com.example.stage7.CRUD.Dashboard.entity;

import com.example.stage7.CRUD.BusinessEntity.BusinessEntity;
import com.example.stage7.CRUD.Portlet.entity.Portlet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "dashboard")
@TypeAlias("dashboard")
public class Dashboard extends BusinessEntity {

    private String title;
    @DBRef/*Cette annotation indique que cet attribut est une référence à un autre document dans MongoDB, ici à une liste d'entités Portlet.*/
    @JsonIgnore/*Cette annotation est utilisée pour indiquer que la liste des portlets ne sera pas incluse lors de la sérialisation en JSON (par exemple, lorsqu'on renvoie un Dashboard via une API REST). Cela peut être utilisé pour éviter les problèmes de sérialisation circulaire ou pour réduire la taille de la réponse JSON.*/
    private List<Portlet> portlets;

}

