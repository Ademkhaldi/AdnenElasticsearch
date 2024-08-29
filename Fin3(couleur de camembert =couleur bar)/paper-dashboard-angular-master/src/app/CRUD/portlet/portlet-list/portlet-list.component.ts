import { Component, OnInit } from '@angular/core';
import { Portlet } from '../portlet.model';
import { PortletService } from '../service/portlet.service';
import { Router } from '@angular/router';
import { DashboardService } from 'app/CRUD/dashboard/service/dashboard.service';
import { Dashboard } from 'app/CRUD/dashboard/dashboard.model';
import { ChartService } from 'app/CRUD/chart/service/chart.service';
import { Chart } from 'app/CRUD/chart/chart.model';

@Component({
  selector: 'app-portlet-list',
  templateUrl: './portlet-list.component.html',
  styleUrls: ['./portlet-list.component.scss']
})
export class PortletListComponent implements OnInit {
  public portlets: Portlet[] = [];
  public dashboard: Dashboard | undefined;
  public chart: Chart | undefined;


  public componentsMapping: { [key: string]: string } = {
    'position n1': 'app-table2',
    'position n2': 'app-Camambert2',
    'position n3': 'app-histogramme2'
  };

  public layout: string[] = ['position n1', 'position n2', 'position n3'];


  constructor(
    private portletService: PortletService,
    private router: Router,
    private dashboardService: DashboardService,
    private chartService: ChartService
  ) {}

  ngOnInit(): void {
    this.reloadData();
    this.loadDashboard();
    this.loadChart();
    this.shuffleLayout();
  }

  shuffleLayout(): void {
    // Shuffle the layout positions
    this.layout = this.layout.sort(() => Math.random() - 0.5);
  }

  reloadData() {
    this.portletService.getAllPortlets().subscribe(data => {
      this.portlets = data.map(Portlet => ({ ...Portlet }));
      console.log(data);
    });
  }

  loadDashboard() {
    this.dashboardService.getAllDashboards().subscribe(data => {
      this.dashboard = data[0]; // Assuming you want to use the first dashboard
    });
  }

  loadChart() {
    this.chartService.getAllCharts().subscribe(data => {
      this.chart = data[0]; // Assuming you want to use the first chart
    });
  }

  // Rest of your component code...

  affecterDashboardAPortlet(idPortlet: any,idDashboard: any) {
    this.router.navigate(['portlet/affecterDashboardAPortlet',idPortlet,idDashboard]);
  }

  affecterChartAPortlet(idPortlet: any,idChart: any) {
    this.router.navigate(['portlet/affecterChartAPortlet',idPortlet,idChart]);
  }

  


  createPortlet() {
    this.router.navigate(['AddPortlet'], { state: { title: 'Ajout Portlet' } });
  }
  updatePortlet(id: string) {
    this.router.navigate(['UpdatePortlet', id]); // Ajustez la route de mise à jour
  }
  deletePortlet(id: string) {
    this.portletService.deletePortlet(id).subscribe(
      data => {
        console.log(data);
        this.reloadData();
      },
      error => console.log(error)
    );
  }
  deleteAllPortlets(): void {
    this.portletService.deleteAllPortlets()
      .subscribe({
        next: (res) => {
          console.log(res);
          this.reloadData();
        },
        error: (e) => console.error(e)
      });
  }
  portletDetails(id: string){
    this.router.navigate(['portlet/details', id]); // Ajustez la route de détails

  }

  


  navigateToDashboardForm() {
    this.router.navigate(['getAllPortlets']); // Ajustez la route de navigation vers le formulaire
  }




}
