import { Component, OnInit } from '@angular/core';
import { Dashboard } from 'app/CRUD/dashboard/dashboard.model';
import { DashboardService } from 'app/CRUD/dashboard/service/dashboard.service';
import { Router } from '@angular/router'; // Import Router from '@angular/router'
import { PortletService } from 'app/CRUD/portlet/service/portlet.service';
import { Portlet } from 'app/CRUD/portlet/portlet.model';

@Component({
  selector: 'app-dashboard-list',
  templateUrl: './dashboard-list.component.html',
  styleUrls: ['./dashboard-list.component.scss']
})
export class DashboardListComponent implements OnInit {
  public dashboards: Dashboard[] = []; // Initialisez avec un tableau vide
  public portlets: Portlet[] = [] ;

  constructor(private dashboardService: DashboardService,private router: Router) { }

  ngOnInit(): void {
    this.reloadData();
  }

  reloadData() {
    this.dashboardService.getAllDashboards().subscribe(data => {
      this.dashboards = data.map(Dashboard => ({
        ...Dashboard,
      }));
      console.log(data);
    });
  }
  navigateToAssignPortlets(idDashboard: any): void {
    this.router.navigate(['dashboard/assignerListePortletsADashboard', idDashboard]); // Redirection vers la page d'assignation de portlets
  }


  

  
  createDashboard() {
    this.router.navigate(['/AddDashboard']); // Ajustez la route de création
  }
  updateDashaboard(id: string) {
    this.router.navigate(['/UpdateDashboard', id]); // Ajustez la route de mise à jour
  }
  deleteDashboard(id: string) {
    this.dashboardService.deleteDashboard(id).subscribe(
      data => {
        console.log(data);
        this.reloadData();
      },
      error => console.log(error)
    );
  }
  deleteAllDashboards(): void {
    this.dashboardService.deleteAllDashboards()
      .subscribe({
        next: (res) => {
          console.log(res);
          this.reloadData();
        },
        error: (e) => console.error(e)
      });
  }
  dashboardDetails(id: string){
    this.router.navigate(['dashboard/details', id]); // Ajustez la route de détails

  }

  


  navigateToDashboardForm() {
    this.router.navigate(['getAllDashboards']); // Ajustez la route de navigation vers le formulaire
  }
}
