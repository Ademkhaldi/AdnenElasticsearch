import { Component, OnInit } from '@angular/core';
import { Chart} from '../chart.model';
import { ChartService } from '../service/chart.service';
import { Router } from '@angular/router';
import { Datasource } from 'app/CRUD/datasource/datasource.model';
import { DatasourceService } from 'app/CRUD/datasource/service/datasource.service';
import { PortletService } from 'app/CRUD/portlet/service/portlet.service';
import { Portlet } from 'app/CRUD/portlet/portlet.model';
import { charttype } from '../charttype.model';

@Component({
  selector: 'app-chart-list',
  templateUrl: './chart-list.component.html',
  styleUrls: ['./chart-list.component.scss']
})
export class ChartListComponent implements OnInit {

  public charts: Chart[] = []; // Initialisez avec un tableau vide
  public datasource: Datasource | undefined;
  public portlet: Portlet | undefined;
  Datasourceid: string | null = null;
  public charttype = charttype; // Ajouter cette ligne

  constructor(private chartService: ChartService, private router: Router, private datasourceService: DatasourceService, private portletService: PortletService) { }

  ngOnInit(): void {
    this.reloadData();
    this.loadDatasource();
    this.loadPortlet();
  }

  reloadData() {
    this.chartService.getAllCharts().subscribe(data => {
      this.charts = data.map(chart => ({
        ...chart,
      }));
      console.log(data);
    });
  }

  loadDatasource() {
    this.datasourceService.getAllDatasources().subscribe(data => {
      this.datasource = data[0]; // Assuming you want to use the first datasource
    });
  }

  loadPortlet() {
    this.portletService.getAllPortlets().subscribe(data => {
      this.portlet = data[0]; // Assuming you want to use the first portlet
    });
  }

  affecterDatasourceAChart(idChart: any, idDatasource: any) {
    this.router.navigate(['chart/affecterDatasourceAChart', idChart, idDatasource]);
  }

  affecterPortletAChart(idChart: any, idPortlet: any) {
    this.router.navigate(['chart/affecterPortletAChart', idChart, idPortlet]);
  }

  createChart() {
    this.router.navigate(['AddChart'], { state: { title: 'Ajout Chart' } });
  }

  updateChart(id: string) {
    this.router.navigate(['UpdateChart', id]); // Ajustez la route de mise à jour
  }

  deleteChart(id: string) {
    this.chartService.deleteChart(id).subscribe(
      data => {
        console.log(data);
        this.reloadData();
      },
      error => console.log(error)
    );
  }

  deleteAllCharts(): void {
    this.chartService.deleteAllCharts().subscribe({
      next: (res) => {
        console.log(res);
        this.reloadData();
      },
      error: (e) => console.error(e)
    });
  }

  chartDetails(id: string) {
    this.router.navigate(['chart/details', id]); // Ajustez la route de détails
  }

  chartDetailsTitle(title: string) {
    this.router.navigate(['chart/details/title', title]); // Ajustez la route de détails
  }

  navigateToChartForm() {
    this.router.navigate(['getAllCharts']); // Ajustez la route de navigation vers le formulaire
  }

  goToCamambert2Component(id: string) {
    this.router.navigate(['Camambert2Component', id]);
  }

  goToTable2Component(id: string) {
    this.router.navigate(['Table2Component', id]);
  }

  goToHistogramm2(id: string) {
    this.router.navigate(['HistogrammeComponent2', id]);
  }


}
