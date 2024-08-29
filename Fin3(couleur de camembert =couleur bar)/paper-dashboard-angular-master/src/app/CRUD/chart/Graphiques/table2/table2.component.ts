// table2.component.ts
import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatasourceService } from 'app/CRUD/datasource/service/datasource.service';
import { ChartService } from 'app/CRUD/chart/service/chart.service';  // Ajoutez ce service

@Component({
  selector: 'app-table2',
  templateUrl: './table2.component.html',
  styleUrls: ['./table2.component.scss']
})
export class Table2Component implements OnInit {

  public tableData: any = [];
  public chartTitle: string = '';  // Propriété pour le titre
  @Input() chartId: string = '';
  @Input() showButton: boolean = true; // Nouvelle propriété pour afficher/masquer le bouton

  constructor(
    private datasourceService: DatasourceService,
    private router: Router,
    private chartService: ChartService,  // Injectez le service Chart
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    if (!this.chartId) {
      this.route.paramMap.subscribe(params => {
        this.chartId = params.get('id') || '';
        this.fetchChartTitle(this.chartId);  // Récupérez le titre lors de la récupération du chartId
      });
    } else {
      this.fetchChartTitle(this.chartId);  // Récupérez le titre si chartId est déjà présent
    }
    this.fetchData2(this.chartId);
  }

  fetchData2(chartId: string) {
    this.datasourceService.getTableData(chartId)
      .subscribe(data => {
        console.log('Fetched Data:', data);
        this.tableData = data;
      });
  }

  fetchChartTitle(chartId: string) {
    this.chartService.retrieveChart(chartId)  // Appel au service pour obtenir le Chart
      .subscribe(chart => {
        this.chartTitle = chart.title || 'Title Not Found';  // Assignez le titre du Chart
      });
  }

  return() {
    this.gotoList(); 
  }

  gotoList() {
    this.router.navigate(['/getAllCharts']);
  }
}
