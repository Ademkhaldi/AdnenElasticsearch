import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EmailDataCamembertService {

  apiUrl = 'http://localhost:9100/api/email-data/Camembert'; // Replace with your Spring Boot API URL

  constructor(private http: HttpClient) {}

  // Implement logic to retrieve email data (e.g., from an API or database)
  getEmailDataCamabert(): Observable<any> {
    return this.http.get<any>(this.apiUrl + '/retrieve-all/Camembert');
  }
}

