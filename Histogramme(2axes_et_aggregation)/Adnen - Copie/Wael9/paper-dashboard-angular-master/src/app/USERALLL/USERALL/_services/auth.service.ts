import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenStorageService } from './token-storage.service'; // Importez le service de stockage de jetons
import { User } from '../user/user.model';
import { ERole, Role } from '../user/role.model';

const AUTH_API = 'http://localhost:9100/api/auth/'; // API Gateway URL for Auth
//const AUTH_API = 'http://localhost:8087/user';

//const AUTH_API = 'http://localhost:9089'; // API Gateway URL for Auth


const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUser: User | null = null;
  

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) { 

    this.currentUser = new User();
    this.currentUser.roles.add(new Role('4', ERole.ROLE_ADMIN));

  }

  login(username: string, password: string): Observable<any> {
    return this.http.post(AUTH_API + 'signin', {
      username,
      password,
      

    }, httpOptions);
  }

  register(username: string, email: string, password: string, role: string): Observable<any> {
    return this.http.post(AUTH_API + 'signup', {
      username,
      email,
      password,
      roles: [role] // Utilisez le rôle spécifié lors de l'inscription
    }, httpOptions);
  }

  
  hasRole(role: string): boolean {
    const user = this.tokenStorage.getUser();
    if (user && user.roles) {
      return user.roles.includes(role);
    }
    return false;
  }


  getCurrentUser(): User | null {
    return this.tokenStorage.getUser();
  }

}