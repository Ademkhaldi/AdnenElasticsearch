import { Component, OnInit } from '@angular/core';
import { Dashboard } from 'app/CRUD/dashboard/dashboard.model';
import { DashboardService } from 'app/CRUD/dashboard/service/dashboard.service';
import { Router } from '@angular/router'; // Import Router from '@angular/router'
import { PortletService } from 'app/CRUD/portlet/service/portlet.service';
import { Portlet } from 'app/CRUD/portlet/portlet.model';

@Component({
  selector: 'app-portlets-for-dashboard-get',
  templateUrl: './portlets-for-dashboaord-get.component.html',
  styleUrls: ['./portlets-for-dashboaord-get.component.scss']
})
export class PortletsForDashboardGetComponent implements OnInit {
    public dashboards: Dashboard[] = []; // Initialisez avec un tableau vide
    public portlets: Portlet[] = [] ;
  
    constructor(private dashboardService: DashboardService,private router: Router,private portletService:PortletService) { }
  
    ngOnInit(): void {
      this.reloadData();
      this.reloadPortlet();
    }
  
    reloadData() {
      this.dashboardService.getAllDashboards().subscribe(data => {
        this.dashboards = data.map(Dashboard => ({
          ...Dashboard,
        }));
        console.log(data);
      });
    }
  
    reloadPortlet() {
      this.portletService.getAllPortlets().subscribe(data => {
        this.portlets = data.map(Portlet => ({
          ...Portlet,
        }));
        console.log(data);
      });
    }
  
  
    navigateToAssignPortlets(idDashboard: any): void {
      this.router.navigate(['dashbord/assignerListePortletsADashboard', idDashboard]); // Redirection vers la page d'assignation de portlets
    }
    

    Dashboard() {
      this.router.navigate(['getAllDashboards']); // Ajustez la route de navigation vers le formulaire
    }
  
    }
  