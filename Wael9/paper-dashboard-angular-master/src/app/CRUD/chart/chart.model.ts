import { Datasource } from "../datasource/datasource.model";
import { charttype } from "./charttype.model";

export class Chart{
    
    title?:string;
    type?:charttype;
    x_axis?:string;
    y_axis?:string;
    index?:string;
    datasource?:Datasource;

}

