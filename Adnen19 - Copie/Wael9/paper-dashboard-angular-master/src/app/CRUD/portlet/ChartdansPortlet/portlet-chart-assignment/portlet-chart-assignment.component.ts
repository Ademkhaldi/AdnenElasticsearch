import { Component, OnInit } from '@angular/core';
import { Portlet } from '../../portlet.model';
import { Chart } from 'app/CRUD/chart/chart.model';
import { PortletService } from '../../service/portlet.service';
import { ChartService } from 'app/CRUD/chart/service/chart.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-portlet-chart-assignment',
  templateUrl: './portlet-chart-assignment.component.html',
  styleUrls: ['./portlet-chart-assignment.component.scss']
})
export class PortletChartAssignmentComponent implements OnInit {

  portlets: Portlet[] = [];
  chart: Chart[] = [];
  selectedPortletId: string | null = null;
  selectedChartId: string | null = null;


  constructor(
    private portletService: PortletService,
    private chartService: ChartService,
    private router: Router
  
  ) {}

  ngOnInit(): void {
    this.loadPortlets();
    this.loadCharts();
  }

  loadPortlets() {
    this.portletService.getAllPortlets().subscribe(data => {
      this.portlets = data;
    });
  }

  loadCharts() {
    this.chartService.getAllCharts().subscribe(data => {
      this.chart = data;
    });
  }

  affecterChartAPortlet() {
    if (this.selectedPortletId !== null && this.selectedChartId !== null) {
      this.portletService.affecterChartAPortlet(this.selectedPortletId, this.selectedChartId)
        .subscribe(() => {
          // Actualizez la liste des portlet et des Dashboard après l'affectation réussie
          this.loadPortlets();
          this.loadCharts();
          // Réinitialisez les sélections après l'affectation réussie
          this.selectedPortletId = null;
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
    this.router.navigate(['/getAllPortlets']); // Make sure the URL is correct for the list of datasources
  }

}
