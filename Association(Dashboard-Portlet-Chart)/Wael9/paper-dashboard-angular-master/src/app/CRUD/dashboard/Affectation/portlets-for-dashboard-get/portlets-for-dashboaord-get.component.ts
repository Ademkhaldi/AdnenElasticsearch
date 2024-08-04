import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DashboardService } from 'app/CRUD/dashboard/service/dashboard.service';
import { Portlet } from 'app/CRUD/portlet/portlet.model';
import { Dashboard } from 'app/CRUD/dashboard/dashboard.model'; // Import the Dashboard model

@Component({
  selector: 'app-portlets-for-dashboard-get',
  templateUrl: './portlets-for-dashboaord-get.component.html', // Ensure correct filename
  styleUrls: ['./portlets-for-dashboaord-get.component.scss'] // Ensure correct filename
})
export class PortletsForDashboardGetComponent implements OnInit {
  portlets: Portlet[] = [];
  dashboardId: string;
  dashboardTitle: string; // Property to store the dashboard title

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private dashboardService: DashboardService
  ) {}

  ngOnInit(): void {
    this.dashboardId = this.route.snapshot.paramMap.get('dashboardId');
    this.getPortletsForDashboard();
    this.getDashboardTitle(); // Fetch the dashboard title
  }

  getPortletsForDashboard(): void {
    this.dashboardService.getPortletsForDashboard(this.dashboardId).subscribe(
      (data: Portlet[]) => {
        this.portlets = data;
      },
      (error) => {
        console.error('Error fetching portlets:', error);
      }
    );
  }

  getDashboardTitle(): void {
    this.dashboardService.retrieveDashboard(this.dashboardId).subscribe(
      (data: Dashboard) => {
        this.dashboardTitle = data.title; // Set the dashboard title
      },
      (error) => {
        console.error('Error fetching dashboard title:', error);
      }
    );
  }

  list(): void {
    this.router.navigate(['/getAllDashboards']);
  }

  navigateToAssignPortlets(idDashboard: any): void {
    this.router.navigate(['dashboard/assignerListePortletsADashboard', idDashboard]);
  }
}
