import { Component, OnInit } from '@angular/core';
import { ChartService } from '../../service/chart.service';
import { PortletService } from 'app/CRUD/portlet/service/portlet.service';
import { Portlet } from 'app/CRUD/portlet/portlet.model';
import { Chart } from '../../chart.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-portlet-to-chart-assignment',
  templateUrl: './portlet-to-chart-assignment.component.html',
  styleUrls: ['./portlet-to-chart-assignment.component.scss']
})
export class PortletToChartAssignmentComponent implements OnInit {

  charts: Chart[] = [];
  portlet: Portlet[] = [];
  selectedChartId: string | null = null;
  selectedPortletId: string | null = null;


  constructor(
    private chartService: ChartService,
    private portletService: PortletService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCharts();
    this.loadPortlets();
  }

  loadCharts() {
    this.chartService.getAllCharts().subscribe(data => {
      this.charts = data;
    });
  }

  loadPortlets() {
    this.portletService.getAllPortlets().subscribe(data => {
      this.portlet = data;
    });
  }

  affecterPortletAChart() {
    if (this.selectedChartId !== null && this.selectedPortletId !== null) {
      this.chartService.affecterPortletAChart(this.selectedChartId, this.selectedPortletId)
        .subscribe(() => {
          // Actualizez la liste des Chart et des datasource après l'affectation réussie
          this.loadCharts();
          this.loadPortlets();
          // Réinitialisez les sélections après l'affectation réussie
          this.selectedChartId = null;
          this.selectedPortletId = null;
          this.gotoList();
          // Chargez les datasource associés à la Chart
        //  this.loadChartdatasources(this.selectedChartId);
        },
        error => console.log(error)
      );
    }
  }

  gotoList() {
    this.router.navigate(['getAllCharts']); // Ajustez la route de navigation vers le formulaire
  }


}
