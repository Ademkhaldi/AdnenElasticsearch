import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Portlet } from '../portlet.model';

@Injectable({
  providedIn: 'root'
})
export class PortletService {

  apiUrl = 'http://localhost:9100/portlets'; // Replace with your Spring Boot API URL
//  apiUrl = 'http://localhost:8087/Stage6'; // Replace with your Spring Boot API URL

  constructor(private http: HttpClient) { }

  getAllPortlets(): Observable<Portlet[]> {
    return this.http.get<Portlet[]>(this.apiUrl+"/getAllPortlets");
  }

  retrievePortlet(id: string): Observable<Portlet> {
    return this.http.get<Portlet>(`${this.apiUrl}/${id}`);
  }

  createPortlet(portlet: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/Add`, portlet);
  }

  updatePortlet(id: string,portlet: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/Update/${id}`, portlet);
  }

  deletePortlet(id: any): Observable<any> {
    return this.http.delete(`${this.apiUrl}/Delete/${id}`, { responseType: 'text' });

  }

  deleteAllPortlets(): Observable<any> {
    return this.http.delete(this.apiUrl+"/deleteAllPortlets",{ responseType: 'text' });
  }
  affecterDashboardAPortlet(idPortlet: string, idDashboard: string): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/affecterDashboardAPortlet/${idPortlet}/${idDashboard}`, {});
  }
 //ChartDansPortlet
 affecterChartAPortlet(idPortlet: string, idChart: string): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/affecterChartAPortlet/${idPortlet}/${idChart}`, {});
  }
}
