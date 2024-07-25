import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class KPIDataService {

  private apiUrl = 'http://localhost:9100/DataController/KPI';

  constructor(private http: HttpClient) { }

  getDataKPI(): Observable<any> {
    return this.http.get<any>(this.apiUrl+'/data/KPI');
  }
}
