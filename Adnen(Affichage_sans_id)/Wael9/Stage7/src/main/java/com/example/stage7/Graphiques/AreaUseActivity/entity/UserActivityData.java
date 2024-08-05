package com.example.stage7.Graphiques.AreaUseActivity.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "Area_user_activity_data")
public class UserActivityData {

    @Id
    private String id;
    private String[] labels;
    private Object[] datasets;

    public UserActivityData() {
    }

    public UserActivityData(String id, String[] labels, Object[] datasets) {
        this.id = id;
        this.labels = labels;
        this.datasets = datasets;
    }

    // Constructeur, getters et setters
}
