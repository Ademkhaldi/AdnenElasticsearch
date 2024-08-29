import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Dashboard } from 'app/CRUD/dashboard/dashboard.model';
import { DashboardService } from 'app/CRUD/dashboard/service/dashboard.service';
import { PortletService } from 'app/CRUD/portlet/service/portlet.service';
import { Portlet } from 'app/CRUD/portlet/portlet.model';
import { ChartService } from 'app/CRUD/chart/service/chart.service';
import { Chart } from 'app/CRUD/chart/chart.model';
import { charttype } from 'app/CRUD/chart/charttype.model';
import { ERole } from 'app/USERALLL/USERALL/user/role.model';
import { UserService } from 'app/USERALLL/USERALL/_services/user.service';
import { AuthService } from 'app/USERALLL/USERALL/_services/auth.service';
import { User } from 'app/USERALLL/USERALL/user/user.model';

@Component({
  selector: 'app-dashboard-list',
  templateUrl: './dashboard-list.component.html',
  styleUrls: ['./dashboard-list.component.scss']
})
export class DashboardListComponent implements OnInit {
  public dashboards: Dashboard[] = [];
  public charttype = charttype;
  public portlets: Portlet[] = [];
  @Input() chart: Chart | undefined;
  public idchart: string ='';
  public iddashboard: string ='';
  dashboardId: string;
  dashboardTitle: string; // Property to store the dashboard title
  @Input() chartId: string = '';


  currentUser: User | null = null; // Déclarez la variable currentUser de type User ou null
  public isAdmin: boolean = false;
  public isUser: boolean = false;




  constructor(
    private dashboardService: DashboardService,
    private route: ActivatedRoute,
    private authService: AuthService, // Use AuthService to get the current user
    private router: Router,
    private chartService: ChartService,
    private portletService: PortletService
  ) { }

  ngOnInit(): void {
    this.reloadData();
    this.loadChart();
    this.loadPortlet();
    this.dashboardId = this.route.snapshot.paramMap.get('dashboardId');
    this.loadUserRole();  // Charger le rôle de l'utilisateur

}



loadUserRole() {
  this.currentUser = this.authService.getCurrentUser();
  // Vérifiez si l'utilisateur a le rôle admin
  this.isAdmin = this.authService.hasRole('ROLE_ADMIN');
  // Vérifiez si l'utilisateur a le rôle user
  this.isUser = this.authService.hasRole('ROLE_USER');
}

  loadPortlet() {
    this.portletService.getAllPortlets().subscribe(data => {
      this.portlets = data;
    }, error => console.error('Error fetching portlets:', error));
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
    this.router.navigate(['dashboard/assignerListePortletsADashboard', idDashboard]);
  }

  createDashboard() {
    this.router.navigate(['/AddDashboard']);
  }

  updateDashboard(id: string) {
    this.router.navigate(['/UpdateDashboard', id]);
  }

  deleteDashboard(id: string) {
    this.dashboardService.deleteDashboard(id).subscribe(
      () => this.reloadData(),
      error => console.error('Error deleting dashboard:', error)
    );
  }

  deleteAllDashboards(): void {
    this.dashboardService.deleteAllDashboards().subscribe(
      () => this.reloadData(),
      error => console.error('Error deleting all dashboards:', error)
    );
  }

  dashboardDetails(id: string) {
    this.router.navigate(['dashboard/details', id]);
  }

  navigateToDashboardForm() {
    this.router.navigate(['getAllDashboards']);
  }



  




  navigateToAssign(dashboardid: string) {
    this.router.navigate(['dashboard/getPortletsForDashboard',dashboardid]);
  }

  navigateToGraphiquesDashboardsComponent(dashboardid: string) {
    this.router.navigate(['dashboard/GraphiquesDashboardsComponent',dashboardid]);
  }





  loadChart() {
    this.chartService.getAllCharts().subscribe(data => {
      this.chart = data[0];
    }, error => console.error('Error fetching charts:', error));
  }




}
