package com.example.stage7.CRUD.BusinessEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


/*@Getter et @Setter : Ces annotations de Lombok génèrent automatiquement les méthodes getter et setter pour tous les champs de la classe, évitant d'écrire manuellement ces méthodes.*/
@Getter
@Setter
/*@ToString : Génère automatiquement une méthode toString() pour la classe, permettant de représenter l'objet sous forme de chaîne de caractères.*/
@ToString
/*Indique que cette classe est un document MongoDB, et elle est mappée à la collection BusinessEntity dans la base de données MongoDB.*/
@Document(collection = "BusinessEntity")
/*@TypeAlias("BusinessEntity") : Cette annotation permet de définir un alias pour cette classe dans MongoDB, facilitant l'enregistrement et la récupération des objets de ce type.*/
@TypeAlias("BusinessEntity")
/*@NoArgsConstructor et @AllArgsConstructor : Génèrent respectivement un constructeur sans arguments et un constructeur avec tous les arguments, permettant ainsi d'initialiser facilement des objets de cette classe.
 */
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

/*setCreateSystemDate() : Cette méthode affecte la date système actuelle (new Date()) au champ creationDate. Cela signifie que lorsque cette méthode est appelée, elle enregistre l'heure exacte à laquelle elle est exécutée comme date de création.

 */

    public void setCreateSystemDate() {
        this.creationDate = new Date(); // Affecte la date système à creationDate
    }
    /*setUpdateSystemDate() : Cette méthode affecte la date système actuelle au champ update_date. Cela permet de mettre à jour automatiquement le champ avec la date et l'heure du moment où la méthode est appelée.

     */
    public void setUpdateSystemDate() {
        this.update_date = new Date(); // Affecte la date système à update_date
    }

}

