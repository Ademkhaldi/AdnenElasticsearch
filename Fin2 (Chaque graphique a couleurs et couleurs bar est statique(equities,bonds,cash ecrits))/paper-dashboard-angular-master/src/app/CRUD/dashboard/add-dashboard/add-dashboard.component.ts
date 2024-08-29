import { Component, OnInit } from "@angular/core";
import { Dashboard } from "../dashboard.model";
import { DashboardService } from "../service/dashboard.service";
import { Router } from "@angular/router";
import { AuthService } from "app/USERALLL/USERALL/_services/auth.service";
import { User } from "app/USERALLL/USERALL/user/user.model";
import { UserService } from "app/USERALLL/USERALL/_services/user.service";

@Component({
  selector: 'app-add-dashboard',
  templateUrl: './add-dashboard.component.html',
  styleUrls: ['./add-dashboard.component.scss']
})
export class AddDashboardComponent implements OnInit {

  dashboard: Dashboard = {
    title: '',
  };
  submitted = false;
  updator_id: string ;
  public users: User[] = [];
  user: User = new User();
  currentUser: User | null = null; // Déclarez la variable currentUser de type User ou null
  creator_id: string ; // Nouveau champ creator_id
  constructor(private dashboardService: DashboardService,private router:Router,private authService: AuthService,private userService: UserService) { }
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

  saveDashboard(): void {
    
     // Vérification de Title
     if (this.dashboard.title.length === 0 ) {
      this.showErrorMessage('title', "title ne peut pas être vide");
      return;
    }
    
    
    
    const data = {
      title: this.dashboard.title,
      creator_id: this.creator_id, // Ajoutez le creator_id lors de la sauvegarde du tableau de bord
      updator_id:this.creator_id
    };

    this.dashboardService.createDashboard(data)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.submitted = true;
        },
        error: (e) => console.error(e)
      });
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

  newDashboard(): void {
    this.submitted = false;
    this.dashboard = {
      title: '',
      
    };
  }

  
  gotoList() {
    this.router.navigate(['/getAllDashboards']); // Assurez-vous que l'URL est correcte pour la liste des markets
  }


}