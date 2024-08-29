package com.example.stage7.CRUD.Chart.controller;

import com.example.stage7.CRUD.Chart.exception.ChartNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice //Annotation qui marque cette classe comme un gestionnaire d'exceptions global pour les contrôleurs Spring MVC. Cela permet à cette classe de traiter les exceptions lancées par les contrôleurs dans toute l'application.
public class GlobalExceptionHandler {

    @ExceptionHandler(ChartNotFoundException.class)//Annotation qui indique que la méthode suivante doit gérer les exceptions de type ChartNotFoundException.
/*public ResponseEntity<String> handleChartNotFoundException(ChartNotFoundException ex) : Méthode publique qui prend en paramètre une exception ChartNotFoundException et retourne un ResponseEntity avec un corps de type String et un statut HTTP.*/
    public ResponseEntity<String> handleChartNotFoundException(ChartNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
/*return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); : Crée et retourne une réponse HTTP avec :
ex.getMessage() : Le message de l'exception, qui sera utilisé comme corps de la réponse.
HttpStatus.NOT_FOUND : Le code de statut HTTP 404 (Not Found), indiquant que la ressource demandée n'a pas été trouvée.*/
}
/*La classe GlobalExceptionHandler est une classe de gestion des exceptions globale pour une application Spring Boot. Elle utilise l'annotation @ControllerAdvice pour indiquer qu'elle gère les exceptions pour tous les contrôleurs. La méthode handleChartNotFoundException est annotée avec @ExceptionHandler pour traiter les exceptions de type ChartNotFoundException. Lorsque cette exception est lancée, la méthode renvoie une réponse HTTP avec le message de l'exception et un code de statut 404 (Not Found).


 */