import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Dashboard } from 'app/CRUD/dashboard/dashboard.model';
import { DashboardService } from 'app/CRUD/dashboard/service/dashboard.service';
import { AuthService } from 'app/USERALLL/USERALL/_services/auth.service';
import { UserService } from 'app/USERALLL/USERALL/_services/user.service';
import { User } from 'app/USERALLL/USERALL/user/user.model';

@Component({
  selector: 'app-update-dashboard',
  templateUrl: './update-dashboard.component.html',
  styleUrls: ['./update-dashboard.component.scss']
})
export class UpdateDashboardComponent implements OnInit {

  public users: User[] = [];
  user: User = new User();
  currentUser: User | null = null; // Déclarez la variable currentUser de type User ou null
  updator_id: string ; // Nouveau champ creator_id
  id: string = '';
  dashboard: Dashboard = new Dashboard();
  constructor(private route: ActivatedRoute, private router: Router,
    private authService: AuthService,private userService: UserService,private dashboardService: DashboardService) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];

    this.dashboardService.retrieveDashboard(this.id)
      .subscribe(data => {
        console.log(data);
        this.dashboard = data;
      }, error => console.log(error));
  this.reloadData2();
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

  updateDashboard() {
         // Vérification de Title
         if (this.dashboard.title.length === 0 ) {
          this.showErrorMessage('title', "title ne peut pas être vide");
          return;
        }
    
    const updateData = {
      ...this.dashboard, // Copier toutes les autres propriétés du tableau de bord
      updator_id: this.updator_id // Ajouter l'updator_id
    };
  
    this.dashboardService.updateDashboard(this.id, updateData).subscribe(
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
    this.updateDashboard();
  }

  gotoList() {
    this.router.navigate(['/getAllDashboards']);
  }
  cancelUpdate() {
    this.gotoList(); // Naviguer vers la liste des tableaux de bord
  }
}
