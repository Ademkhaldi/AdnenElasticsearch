package com.example.stage7.Graphiques.EmailCamembert.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "EmailStatCamembert")
public class EmailStat {
    @Id
    private String id;
    private String label;
    private int value;



    public EmailStat() {
    }


    public EmailStat(String label, int value) {
        this.label = label;
        this.value = value;
    }
}
