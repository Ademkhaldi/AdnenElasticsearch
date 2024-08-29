package com.example.stage7.CRUD.Chart.exception;



/*une exception personnalisée appelée ChartNotFoundException. Cette exception est une sous-classe de RuntimeException, ce qui signifie qu'elle est une exception non vérifiée (unchecked exception), c'est-à-dire qu'il n'est pas nécessaire de la déclarer dans les signatures de méthodes avec throws.*/
public class ChartNotFoundException extends RuntimeException {
/*ChartNotFoundException hérite de RuntimeException. Cela signifie qu'elle est une exception qui peut être lancée à tout moment dans le programme sans être nécessairement capturée ou déclarée dans une méthode.*/


/*Ce constructeur prend un message de type String en paramètre, qui représente la description de l'exception. Le message peut donner des détails supplémentaires sur l'erreur, comme pourquoi ou où le graphique (chart) n'a pas été trouvé.*/
    public ChartNotFoundException(String message) {
/*Ce constructeur prend un message de type String en paramètre, qui représente la description de l'exception. Le message peut donner des détails supplémentaires sur l'erreur, comme pourquoi ou où le graphique (chart) n'a pas été trouvé.*/
        super(message);
    }
}
