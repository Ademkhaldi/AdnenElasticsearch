import { Chart } from "../chart/chart.model";

export class Datasource{
    
    type?:string;
    connection_port?:number;

    
    
    url?:string;
    user?:string;
    password?:string;

    chart?:Chart;

     
}

// Méthode pour valider que l'index est dans l'intervalle spécifié
