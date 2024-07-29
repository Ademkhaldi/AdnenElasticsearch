import { Component, OnInit } from '@angular/core';
import { DatasourceService } from '../service/datasource.service';
import { Router } from '@angular/router';
import { Datasource } from '../datasource.model';
import { UserService } from 'app/USERALLL/USERALL/_services/user.service';
import { AuthService } from 'app/USERALLL/USERALL/_services/auth.service';
import { User } from 'app/USERALLL/USERALL/user/user.model';

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
    
    // Vérification de Type
    if (this.datasource.type.length === 0 ) {
      this.showErrorMessage('type', "Type ne peut pas être vide");
      return;
    }
    

    

    // connection_port
    if (this.datasource.connection_port.toString().length > 4) {
      this.showErrorMessage('connection_port', "connection_port ne peut pas depasser 4 caractere");
      return;
    }
    if (this.datasource.connection_port === 0) {
      this.showErrorMessage('connection_port', "connection_port ne peut pas etre egal a 0");
      return;
    }
    
// Vérification de l'URL
if (!this.datasource.url.trim()) {
  this.showErrorMessage('url', "L'URL ne peut pas être vide.");
  return;
}


   

// Vérification de User
if (this.datasource.user.length === 0 ) {
  this.showErrorMessage('user', "User ne peut pas être vide");
  return;
}

    
// Vérification de Password
if (this.datasource.password.length === 0 ) {
  this.showErrorMessage('password', "password ne peut pas être vide");
  return;
}


 
   const data = {
   
      type: this.datasource.type,
      connection_port: this.datasource.connection_port,
      url: this.datasource.url,
      user: this.datasource.user,
      password: this.datasource.password,
      creator_id: this.creator_id, // Add creator_id when saving the datasource
      updator_id:this.creator_id

    
    };

    this.datasourceService.createDatasource(data)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.submitted = true;
        },
        error: (e) => {
          console.error(e);
          // Handle errors appropriately
        }
      });

      this.submitted = true;
  }
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

