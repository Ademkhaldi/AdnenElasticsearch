package com.example.stage7.CRUD;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    //@Bean Cette annotation est utilisée pour indiquer à Spring
    // qu'une méthode retourne un bean géré par le conteneur Spring.
    @Bean
    //configurer des conversions personnalisées dans Spring Data MongoDB.
    public MongoCustomConversions customConversions() {
    //Cette ligne crée une liste de convertisseurs pour les conversions personnalisées.
        List<Converter<?, ?>> converters = new ArrayList<>();
    //Cette ligne ajoute un convertisseur de type Date vers String à la liste de convertisseurs.
        converters.add(new DateToStringConverter());
    // Cette ligne ajoute un convertisseur de type String vers Date à la liste de convertisseurs.
        converters.add(new StringToDateConverter());
    //Cette ligne retourne un nouvel objet MongoCustomConversions initialisé avec la liste de convertisseurs personnalisés.
        return new MongoCustomConversions(converters);
    }
//Cette annotation marque la classe DateToStringConverter comme un convertisseur pour l'écriture
    @WritingConverter
 // Cette ligne définit une classe interne statique DateToStringConverter
//convertir un objet Date en une chaîne String
    static class DateToStringConverter implements Converter<Date, String> {
        @Override
//la conversion d'une date en chaîne.
        public String convert(Date source) {
        //récupère le fuseau horaire par défaut
            TimeZone timeZone = TimeZone.getDefault();
        //crée un formateur de date avec le modèle spécifié
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         //Cette ligne définit le fuseau horaire pour le formateur de date.
            dateFormat.setTimeZone(timeZone);
          //Cette ligne formate la date donnée en une chaîne en utilisant le formateur de date.
            return dateFormat.format(source);
        }
    }
//pour la lecture
    @ReadingConverter
  // pour convertir une chaîne String en un objet Date
    static class StringToDateConverter implements Converter<String, Date> {
        @Override
  //Cette ligne définit la méthode convert qui effectue la conversion d'une chaîne en date
        public Date convert(String source) {
            try {
          // crée un formateur de date avec le modèle spécifié
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          //Cette ligne analyse la chaîne de date donnée en un objet Date en utilisant le formateur de date.
                return dateFormat.parse(source);
            //Cette ligne capture une exception si une erreur se produit lors de l'analyse de la chaîne en date.
            } catch (ParseException e) {
                // Gérer l'erreur de conversion de la date
              //Cette ligne affiche la trace de la pile de l'exception
               //Lorsqu'une exception se produit dans un programme Java, le programme peut être configuré pour afficher des informations sur cette exception afin de faciliter le processus de débogage
                e.printStackTrace();
                return null;
            }
        }
    }

}
