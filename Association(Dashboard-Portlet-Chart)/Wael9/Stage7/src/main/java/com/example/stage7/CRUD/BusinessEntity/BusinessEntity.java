package com.example.stage7.CRUD.BusinessEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@ToString
@Document(collection = "BusinessEntity")
@TypeAlias("BusinessEntity")
@NoArgsConstructor
@AllArgsConstructor
public class BusinessEntity {
    @Id
   private String id;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Etc/UTC")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Africa/Tunis")
   // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Paris")

    private Date creationDate;
    private String creator_id;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "ETC/UTC")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Africa/Tunis")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Paris")


    private Date update_date;
    private String updator_id;



    public void setCreateSystemDate() {
        this.creationDate = new Date(); // Affecte la date système à creationDate
    }
    public void setUpdateSystemDate() {
        this.update_date = new Date(); // Affecte la date système à update_date
    }

}

