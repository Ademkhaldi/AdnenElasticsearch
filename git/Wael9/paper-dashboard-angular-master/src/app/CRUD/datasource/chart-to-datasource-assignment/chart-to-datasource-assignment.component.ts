import { Component, OnInit } from '@angular/core';
import { Datasource } from '../datasource.model';
import { Chart } from 'app/CRUD/chart/chart.model';
import { ChartService } from 'app/CRUD/chart/service/chart.service';
import { DatasourceService } from '../service/datasource.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-chart-to-datasource-assignment',
  templateUrl: './chart-to-datasource-assignment.component.html',
  styleUrls: ['./chart-to-datasource-assignment.component.scss']
})
export class ChartToDatasourceAssignmentComponent implements OnInit {

  datasources: Datasource[] = [];
  chart: Chart[] = [];
  selectedDatasourceId: string | null = null;
  selectedChartId: string | null = null;


  constructor(
    private datasourceService: DatasourceService,
    private chartService: ChartService,
    private router:Router
  ) {}

  ngOnInit(): void {
    this.loaddDtasources();
    this.loadCharts();
  }

  loaddDtasources() {
    this.datasourceService.getAllDatasources().subscribe(data => {
      this.datasources = data;
    });
  }

  loadCharts() {
    this.chartService.getAllCharts().subscribe(data => {
      this.chart = data;
    });
  }

  affecterChartADatasource() {
    if (this.selectedDatasourceId !== null && this.selectedChartId !== null) {
      this.datasourceService.affecterChartADatasource(this.selectedDatasourceId, this.selectedChartId)
        .subscribe(() => {
          // Actualizez la liste des portlet et des Dashboard après l'affectation réussie
          this.loaddDtasources();
          this.loadCharts();
          // Réinitialisez les sélections après l'affectation réussie
          this.selectedDatasourceId = null;
          this.selectedChartId = null;
          this.gotoList();
          // Chargez les Dashboard associés à la portlet
        //  this.loadPortletDashboards(this.selectedPortletId);
        },
        error => console.log(error)
      );
    }
  }
  gotoList() {
    this.router.navigate(['/getAllDatasources']); // Make sure the URL is correct for the list of datasources
  }


}
