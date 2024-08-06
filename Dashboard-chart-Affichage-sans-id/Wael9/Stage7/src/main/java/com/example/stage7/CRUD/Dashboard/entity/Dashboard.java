package com.example.stage7.CRUD.Dashboard.entity;

import com.example.stage7.CRUD.BusinessEntity.BusinessEntity;
import com.example.stage7.CRUD.Portlet.entity.Portlet;
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
    @DBRef
    //@JsonIgnoreProperties("dashboard")
    private List<Portlet> portlets;

}

