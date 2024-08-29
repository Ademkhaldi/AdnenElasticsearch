import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Dashboard } from 'app/CRUD/dashboard/dashboard.model';
import { Portlet } from 'app/CRUD/portlet/portlet.model';
import { Observable, catchError, of, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  apiUrl = 'http://localhost:9100/dashboards'; // Replace with your Spring Boot API URL
//  apiUrl = 'http://localhost:8087/Stage6'; // Replace with your Spring Boot API URL

  constructor(private http: HttpClient) { }

  getAllDashboards(): Observable<Dashboard[]> {
    return this.http.get<Dashboard[]>(this.apiUrl+"/getAllDashboards");
  }

  retrieveDashboard(id: string): Observable<Dashboard> {
    return this.http.get<Dashboard>(`${this.apiUrl}/${id}`);
  }

  createDashboard(dashboard: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/Add`, dashboard);
  }

  updateDashboard(id: string,dashboard: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/Update/${id}`, dashboard);
  }

  deleteDashboard(id: any): Observable<any> {
    return this.http.delete(`${this.apiUrl}/Delete/${id}`, { responseType: 'text' });

  }

  deleteAllDashboards(): Observable<any> {
    return this.http.delete(this.apiUrl+"/deleteAllDashboards",{ responseType: 'text' });
  }

  assignerListePortletsADashboard(idDashboard: string, portlets: Portlet[]): Observable<Dashboard> {
    return this.http.post<Dashboard>(`${this.apiUrl}/${idDashboard}/assignerListePortletsADashboard`, portlets)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          // Ne rien faire ici pour éviter l'affichage de l'erreur dans la console
          // Vous pouvez gérer l'erreur si nécessaire
          return throwError(error);
        })
      );
  }
  
  getPortletsForDashboard(dashboardId: string): Observable<Portlet[]> {
    return this.http.get<Portlet[]>(`${this.apiUrl}/${dashboardId}/getPortletsForDashboard`);
  }  

}
