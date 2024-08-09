import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DashboardService } from 'app/CRUD/dashboard/service/dashboard.service';
import { Portlet } from 'app/CRUD/portlet/portlet.model';
import { Dashboard } from 'app/CRUD/dashboard/dashboard.model';
import { Chart } from 'app/CRUD/chart/chart.model';
import { charttype } from 'app/CRUD/chart/charttype.model';

@Component({
  selector: 'app-graphiques-dashboards',
  templateUrl: './graphiques-dashboards.component.html',
  styleUrls: ['./graphiques-dashboards.component.scss']
})
export class GraphiquesDashboardsComponent implements OnInit {
  portlets: Portlet[] = [];
  shuffledPortlets: Portlet[] = [];
  dashboardId: string;
  dashboardTitle: string;

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
    this.getDashboardTitle(); // Fetch the dashboard title
    this.getPortletsForDashboard();
  }

  getPortletsForDashboard(): void {
    this.dashboardService.getPortletsForDashboard(this.dashboardId).subscribe(
      (data: Portlet[]) => {
        this.portlets = this.sortPortlets(data);
      },
      (error) => {
        console.error('Error fetching portlets:', error);
      }
    );
  }
  
  sortPortlets(portlets: Portlet[]): Portlet[] {
    return portlets.sort((a, b) => {
      // Assurez-vous que vous avez un critère de tri approprié basé sur row et column
      const rowOrder = ['haut', 'bas'];
      const columnOrder = ['gauche', 'droite', 'full'];
  
      // Comparez les rows
      const rowDiff = rowOrder.indexOf(a.row || '') - rowOrder.indexOf(b.row || '');
      if (rowDiff !== 0) return rowDiff;
  
      // Comparez les columns
      return columnOrder.indexOf(a.column || '') - columnOrder.indexOf(b.column || '');
    });
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

  shufflePortlets(array: Portlet[]): Portlet[] {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
  }

  list(): void {
    this.router.navigate(['/getAllDashboards']);
  }

  navigateToAssignPortlets(idDashboard: any): void {
    this.router.navigate(['dashboard/assignerListePortletsADashboard', idDashboard]);
  }

  getPortletPositionClass(portlet: Portlet): string {
    const row = portlet.row?.toLowerCase();
    const column = portlet.column?.toLowerCase();
  
    if (row === 'haut' && column === 'gauche') {
      return 'gauche-haut';
    } else if (row === 'haut' && column === 'droite') {
      return 'droite-haut';
    }else if (row === 'haut'&& column === 'full') {
      return 'haut'; // Nouvelle classe pour la position haut
    }
    else if (row === 'bas' && column === 'gauche') {
      return 'gauche-bas';
    } else if (row === 'bas' && column === 'droite') {
      return 'droite-bas';
    } else if (row === 'bas' && column === 'full') {
      return 'bas'; // Occupe full bottom row
    } else {
      return '';
    }
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
