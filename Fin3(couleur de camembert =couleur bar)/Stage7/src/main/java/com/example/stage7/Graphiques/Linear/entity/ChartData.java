package com.example.stage7.Graphiques.Linear.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "linear_chart_data")
public class ChartData {
    @Id
    private String id;

    private String[] labels;
    private Object[] datasets;
//Si vous souhaitez stocker les résultats de plusieurs données dans un tableau d'objets,

    // Getters and setters


    public ChartData() {
    }

    public ChartData(String[] labels, Object[] datasets) {
        this.labels = labels;
        this.datasets = datasets;
    }
}
