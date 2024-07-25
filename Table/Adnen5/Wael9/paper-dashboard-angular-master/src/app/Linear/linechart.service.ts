// emaildata.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LineChartService {

  apiUrl = 'http://localhost:9100/api/line-chart-data'; // Remplacez avec votre URL Spring Boot

  constructor(private http: HttpClient) { }

  getLineChartData(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/retrieve/lines`);
  }
}
