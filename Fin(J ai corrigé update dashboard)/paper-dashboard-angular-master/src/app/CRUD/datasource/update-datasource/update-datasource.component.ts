import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatasourceService } from '../service/datasource.service';
import { Datasource } from '../datasource.model';
import { User } from 'app/USERALLL/USERALL/user/user.model';
import { UserService } from 'app/USERALLL/USERALL/_services/user.service';
import { AuthService } from 'app/USERALLL/USERALL/_services/auth.service';

@Component({
  selector: 'app-update-datasource',
  templateUrl: './update-datasource.component.html',
  styleUrls: ['./update-datasource.component.scss']
})
export class UpdateDatasourceComponent implements OnInit {

  id: string = '';
  datasource: Datasource = new Datasource();
  public users: User[] = [];
  user: User = new User();
  currentUser: User | null = null; // Déclarez la variable currentUser de type User ou null
  updator_id: string ; // Nouveau champ creator_id
  passwordFieldType: string = 'password'; // Field type for password input
  passwordMaxLength:number = 8
  constructor(private route: ActivatedRoute, private router: Router,
    private authService: AuthService,private userService: UserService,private datasourceService: DatasourceService) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];

    this.datasourceService.retrieveDatasource(this.id)
      .subscribe(data => {
        console.log(data);
        this.datasource = data;
      }, error => console.log(error));
this.reloadData2(); 
   } 

  updateDatasource() {
// Vérification de Type
const errorMessages = [];

    
// Vérification de Type
if (this.datasource.type.length === 0) {
 errorMessages.push({ inputId: 'type', message: "Type ne peut pas être vide" });
}

 

  // Vérification du connection_port
if (this.datasource.connection_port.toString().length > 4) {
 errorMessages.push({ inputId: 'connection_port', message: "connection_port ne peut pas dépasser 4 caractères" });
}

if (this.datasource.connection_port === 0) {
 errorMessages.push({ inputId: 'connection_port', message: "connection_port ne peut pas être égal à 0" });
}


if (this.datasource.connection_port != 9200) {
 errorMessages.push({ inputId: 'connection_port', message: "faux connection_port" });
}
 



 // Vérification des champs spécifiques

 
// Vérification de l'URL
if (!this.datasource.url.trim()) {
errorMessages.push({ inputId: 'url', message: "L'URL ne peut pas être vide." });
}
if (this.datasource.url != 'localhost') {
errorMessages.push({ inputId: 'url', message: "faux URL" });
}


// Vérification de User
if (this.datasource.user.length === 0) {
errorMessages.push({ inputId: 'user', message: "User ne peut pas être vide" });
}

if (this.datasource.user != 'admin') {
errorMessages.push({ inputId: 'user', message: "faux user" });
}

// Vérification de Password
if (this.datasource.password.length === 0) {
errorMessages.push({ inputId: 'password', message: "password ne peut pas être vide" });
}

if (this.datasource.password != '0207') {
errorMessages.push({ inputId: 'password', message: "faux password" });
}

// Si des erreurs sont présentes, les afficher toutes
if (errorMessages.length > 0) {
errorMessages.forEach(error => {
 this.showErrorMessage(error.inputId, error.message);
});
return; // Arrêtez le processus de sauvegarde si des erreurs existent
}


    const updateData = {
      ...this.datasource, // Copier toutes les autres propriétés du tableau de bord
      updator_id: this.updator_id // Ajouter l'updator_id
    };
  
    this.datasourceService.updateDatasource(this.id, updateData).subscribe(
      (data) => {
        console.log(data);
        this.gotoList();
      },
      (error) => {
        console.log(error);
        this.gotoList();
      }
    );
  }
// Méthode pour afficher un message d'erreur sous le champ correspondant
showErrorMessage(inputId: string, message: string): void {
  const inputElement = document.getElementById(inputId);
  const errorDiv = inputElement.nextElementSibling;
  if (errorDiv && errorDiv.classList.contains('text-danger')) {
    errorDiv.textContent = message;
  } else {
    const div = document.createElement('div');
    div.textContent = message;
    div.classList.add('text-danger');
    inputElement.insertAdjacentElement('afterend', div);
  }
}

  onSubmit() {
    this.updateDatasource();
  }

  gotoList() {
    this.router.navigate(['/getAllDatasources']);
  }

  togglePasswordVisibility(): void {
    // Toggle password field type between 'password' and 'text'
    this.passwordFieldType = (this.passwordFieldType === 'password') ? 'text' : 'password';
  }
  reloadData2() {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser.id) {
      this.updator_id = currentUser.id;
      this.userService.retrieveUser(currentUser.id)
        .subscribe(
          data => {
            console.log(data);
            this.user = data;
            this.updator_id = this.user.username; // Update creator_id with the retrieved username
          },
          error => console.log(error)
        );
    }
  }

  cancelUpdate() {
    this.gotoList(); // Naviguer vers la liste des tableaux de bord
  }
}
