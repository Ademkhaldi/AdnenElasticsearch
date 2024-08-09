import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DashboardService } from 'app/CRUD/dashboard/service/dashboard.service';
import { Portlet } from 'app/CRUD/portlet/portlet.model';
import { Dashboard } from 'app/CRUD/dashboard/dashboard.model'; // Import the Dashboard model
import { Chart } from 'app/CRUD/chart/chart.model';
import { charttype } from 'app/CRUD/chart/charttype.model';

@Component({
  selector: 'app-portlets-for-dashboard-get',
  templateUrl: './portlets-for-dashboaord-get.component.html', // Ensure correct filename
  styleUrls: ['./portlets-for-dashboaord-get.component.scss'] // Ensure correct filename
})
export class PortletsForDashboardGetComponent implements OnInit {
  portlets: Portlet[] = [];
  dashboardId: string;
  dashboardTitle: string; // Property to store the dashboard title



  public dashboards: Dashboard[] = [];
  public charttype = charttype;
  @Input() chart: Chart | undefined;
  public idchart: string = '';
  public iddashboard: string = '';
  @Input() chartId: string = '';

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


  getChartComponent(chart: Chart): string {
    switch (chart?.type) {
      case 'Table':
        return 'app-table2';
      case 'Pie':
        return 'app-Camambert2';
      case 'Bar':
        return 'app-histogramme2';
      default:
        return '';
    }
  }




}