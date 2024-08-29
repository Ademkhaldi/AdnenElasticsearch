import { Component, OnInit } from '@angular/core';
import { DatasourceService } from '../service/datasource.service';
import { Router } from '@angular/router';
import { Datasource } from '../datasource.model';
import { UserService } from 'app/USERALLL/USERALL/_services/user.service';
import { AuthService } from 'app/USERALLL/USERALL/_services/auth.service';
import { User } from 'app/USERALLL/USERALL/user/user.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-add-datasource',
  templateUrl: './add-datasource.component.html',
  styleUrls: ['./add-datasource.component.scss']
})
export class AddDatasourceComponent implements OnInit {
  identifier: string = '';
 

  datasource: Datasource = {
    type: '',
    connection_port: 0,
    url: '',
    user: '',
    password: '',
  

  };
  submitted = false;
  updator_id:string;
  errorMessages: any[] = [];
  public users: User[] = [];
  user: User = new User();
  currentUser: User | null = null; // Déclarez la variable currentUser de type User ou null
  creator_id: string ; // Nouveau champ creator_id
  navbarTitle: string = 'List'; // Provide a default value for navbarTitle
  passwordFieldType: string = 'password'; // Field type for password input
  passwordMaxLength: number = 8; // Maximum length for password
  portMaxLength: number = 4; // Maximum length for connection port

  constructor(private datasourceService: DatasourceService, private router: Router,private authService: AuthService,private userService: UserService) { }
  
  
  

  ngOnInit(): void {
    this.reloadData2();
  }




  






  reloadData2() {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser.id) {
      this.creator_id = currentUser.id;
      this.userService.retrieveUser(currentUser.id)
        .subscribe(
          data => {
            console.log(data);
            this.user = data;
            this.creator_id = this.user.username; // Update creator_id with the retrieved username
          },
          error => console.log(error)
        );
    }
  }


  saveDatasource(): void {
    this.errorMessages = [];
    
    // Validation côté client
    if (this.datasource.type.length === 0) {
      this.errorMessages.push({ inputId: 'type', message: "Type ne peut pas être vide" });
    }
    
    if (this.datasource.connection_port.toString().length > 4) {
      this.errorMessages.push({ inputId: 'connection_port', message: "Connection port ne peut pas dépasser 4 caractères" });
    }
  
    if (this.datasource.connection_port === 0) {
      this.errorMessages.push({ inputId: 'connection_port', message: "Connection port ne peut pas être égal à 0" });
    }
  
    if (this.datasource.user.length === 0) {
      this.errorMessages.push({ inputId: 'user', message: "User ne peut pas être vide" });
    }
  
    if (this.datasource.url.length === 0) {
      this.errorMessages.push({ inputId: 'url', message: "URL ne peut pas être vide" });
    }
  
    if (this.datasource.password.length === 0) {
      this.errorMessages.push({ inputId: 'password', message: "Le mot de passe ne peut pas être vide" });
    }
  
    // Si des erreurs sont présentes, les afficher et arrêter le processus
    if (this.errorMessages.length > 0) {
      this.errorMessages.forEach(error => this.showErrorMessage(error.inputId, error.message));
      return;
    }
  
    // Vérifications supplémentaires avec les appels API
    this.validateWithApi().subscribe(() => {
      // Enregistrer les données si tout est correct
      const data = {
        type: this.datasource.type,
        connection_port: this.datasource.connection_port,
        url: this.datasource.url,
        user: this.datasource.user,
        password: this.datasource.password,
        creator_id: this.creator_id
      };
  
      this.datasourceService.createDatasource(data)
        .subscribe({
          next: (res) => {
            console.log(res);
            this.submitted = true;
            this.router.navigate(['/getAllDatasources']); // Redirige vers la liste des datasources après l'enregistrement
          },
          error: (e) => {
            console.error(e);
            this.errorMessages.push({ inputId: 'api', message: "Erreur lors de l'enregistrement des données" });
            this.showErrorMessage('api', "Erreur lors de l'enregistrement des données");
          }
        });
    }, (err) => {
      // Gérer les erreurs de validation API
      console.error(err);
      this.errorMessages.forEach(error => this.showErrorMessage(error.inputId, error.message));
    });
  }
  
  validateWithApi() {
    return new Observable(observer => {
      // Valider l'utilisateur
      this.datasourceService.getElasticsearchUser().subscribe((elasticUser: string) => {
        if (this.datasource.user != elasticUser.trim()) {
          this.errorMessages.push({ inputId: 'user', message: "Faux utilisateur Elasticsearch" });
        }
  
        // Valider le port
        this.datasourceService.getElasticsearchPort().subscribe((elasticPort: number) => {
          if (this.datasource.connection_port != elasticPort) {
            this.errorMessages.push({ inputId: 'connectionPort', message: "Faux Port Elasticsearch" });
          }
  
          // Vérifier le mot de passe
          this.datasourceService.verifyPassword(this.datasource.password).subscribe((isPasswordValid: boolean) => {
            if (!isPasswordValid) {
              this.errorMessages.push({ inputId: 'password', message: "Faux mot de passe" });
            }
  
            // Vérifier l'URL
            this.datasourceService.getUrl().subscribe((configUrl: string) => {
              if (this.datasource.url != configUrl.trim()) {
                this.errorMessages.push({ inputId: 'url', message: "Faux URL Elasticsearch" });
              }
  
              // S'il y a des erreurs, renvoyer une erreur
              if (this.errorMessages.length > 0) {
                observer.error(new Error('Validation échouée'));
              } else {
                observer.next();
                observer.complete();
              }
            });
          });
        });
      });
    });
  }
  
  



  showErrorMessage(inputId: string, message: string): void {
    const inputElement = document.getElementById(inputId);
    const errorDiv = inputElement?.nextElementSibling;
    if (errorDiv && errorDiv.classList.contains('text-danger')) {
      errorDiv.textContent = message;
    } else {
      const div = document.createElement('div');
      div.textContent = message;
      div.classList.add('text-danger');
      inputElement?.insertAdjacentElement('afterend', div);
    }
  }


  newDatasource(): void {
    this.submitted = false;
    this.datasource = {
      type: '',
      connection_port: 0,
      url: '',
      user: '',
      password: '',
  
    };
  }

  gotoList() {
    this.router.navigate(['/getAllDatasources']); // Make sure the URL is correct for the list of datasources
  }

  togglePasswordVisibility(): void {
    // Toggle password field type between 'password' and 'text'
    this.passwordFieldType = (this.passwordFieldType === 'password') ? 'text' : 'password';
  }

}

