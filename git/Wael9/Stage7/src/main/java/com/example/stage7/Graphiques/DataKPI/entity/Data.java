package com.example.stage7.Graphiques.DataKPI.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "dataKPI")
public class Data {
    @Id
    private String id;
    private String capacity;
    private String revenue;
    private String errors;
    private String followers;

    public Data() {
    }

    public Data(String capacity, String revenue, String errors, String followers) {
        this.capacity = capacity;
        this.revenue = revenue;
        this.errors = errors;
        this.followers = followers;
    }
}
