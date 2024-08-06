import { Datasource } from "../datasource/datasource.model";
import { Portlet } from "../portlet/portlet.model";
import { charttype } from "./charttype.model";

export class Chart{
    
    title?:string;
    type?:charttype;
    x_axis?:string;
    y_axis?:string;
    aggreg?:string;
    index?:string;
    portlet?:Portlet;
    datasource?:Datasource;



    

}

