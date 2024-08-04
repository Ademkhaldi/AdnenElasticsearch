import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Datasource } from '../datasource.model';

@Injectable({
  providedIn: 'root'
})
export class DatasourceService {

  apiUrl = 'http://localhost:9100/datasources'; // Replace with your Spring Boot API URL
//  apiUrl = 'http://localhost:8087/Stage6'; // Replace with your Spring Boot API URL

  constructor(private http: HttpClient) { }

  getAllDatasources(): Observable<Datasource[]> {
    return this.http.get<Datasource[]>(this.apiUrl+"/getAllDatasources");
  }
  getElasticsearchUrl(idDatasource: string): Observable<string> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const body = { idDatasource: idDatasource };
    return this.http.post(`${this.apiUrl}/getElasticsearchUrl`, body, { headers, responseType: 'text' });
  }

  retrieveDatasource(id: String): Observable<Datasource> {
    return this.http.get<Datasource>(`${this.apiUrl}/${id}`);
  }

  createDatasource(datasource: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/Add`, datasource);
  }

  updateDatasource(id: String,datasource: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/Update/${id}`, datasource);
  }

  deleteDatasource(id: any): Observable<any> {
    return this.http.delete(`${this.apiUrl}/Delete/${id}`, { responseType: 'text' });

  }

  
  deleteAllDatasources(): Observable<any> {
    return this.http.delete(this.apiUrl+"/deleteAllDatasources",{ responseType: 'text' });
  }
  // chart dans datasource
  affecterChartADatasource(idDatasource: string, idChart: string): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/affecterChartADatasource/${idDatasource}/${idChart}`, {});
  }

  getAllIndexByDatasourceId(id: String): Observable<String[]> {
    return this.http.get<String[]>(`${this.apiUrl}/getAllIndexByDatasourceId/${id}`);
  }
     
  getAttributesByIndex(idDatasource: String, index: String): Observable<String[]> {
    return this.http.get<String[]>(`${this.apiUrl}/${idDatasource}/index/${index}/attributes`);
  }
getCamambertData(chartId: string): Observable<any> {
  return this.http.get<any>(`${this.apiUrl}/camambert/${chartId}`);
}


getTableData(chartId: string): Observable<any> {
  return this.http.get<any>(`${this.apiUrl}/table/${chartId}`);
}


getHistogramme2(chartId: string): Observable<any> {
  return this.http.get<any>(`${this.apiUrl}/histogramme2/${chartId}`);
}

}
