// src/app/camambert2/camambert2.component.ts
import { Component, Input, OnInit } from '@angular/core';
import { Chart, ChartConfiguration, registerables } from 'chart.js';
import { DatasourceService } from '../../../datasource/service/datasource.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ChartService } from '../../service/chart.service';
import { ColorService } from 'app/services/color.service'; 

@Component({
  selector: 'app-Camambert2',
  templateUrl: './Camambert2.component.html',
  styleUrls: ['./Camambert2.component.scss']
})
export class Camambert2Component implements OnInit {

  public chart: Chart<'pie', number[], string> | undefined;
  public camabertData: any[] = [];
  public chartTitle: string = '';
  @Input() chartId: string = '';
  @Input() showButton: boolean = true; // Nouvelle propriété pour afficher/masquer le bouton


  constructor(
    private chartService: ChartService,
    private datasourceService: DatasourceService,
    private route: ActivatedRoute,
    private router: Router,
    private colorService: ColorService // Inject the ColorService
  ) { 
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    if (!this.chartId) {
      this.route.paramMap.subscribe(params => {
        this.chartId = params.get('id') || '';
        this.fetchChartTitle(this.chartId);
      });
    } else {
      this.fetchChartTitle(this.chartId);
    }
    this.fetchData(this.chartId);
  }

  fetchChartTitle(chartId: string) {
    this.chartService.retrieveChart(chartId)
      .subscribe(chart => {
        this.chartTitle = chart.title || 'Title Not Found';
      });
  }

  fetchData(chartId: string) {
    this.datasourceService.getCamambertData(chartId)
      .subscribe(data => {
        console.log('Fetched Data:', data);
        this.camabertData = data;
        this.createChart();
      });
  }

  createChart() {
    const canvas = document.getElementById('chartContainer') as HTMLCanvasElement;
    if (!canvas) {
      console.error('Canvas element not found!');
      return;
    }
    const ctx = canvas.getContext('2d');
    if (!ctx) {
      console.error('Failed to get 2D context from canvas!');
      return;
    }

    if (!this.camabertData || this.camabertData.length === 0) {
      return;
    }

    console.log('Creating Chart...');
    const labels = this.camabertData.map((stat: any) => stat.label);
    const values = this.camabertData.map((stat: any) => stat.value);

    // Generate colors for each label
    const backgroundColors = labels.map(label => this.colorService.getColorForLabel(label));

    const chartConfig: ChartConfiguration<'pie', number[], string> = {
      type: 'pie',
      data: {
        labels,
        datasets: [{
          label: 'Data',
          backgroundColor: backgroundColors,
          borderWidth: 0,
          data: values as number[]
        }]
      },
      options: {
        responsive: true,
        plugins: {
          tooltip: {
            callbacks: {
              label: function (context) {
                let label = context.label || '';
                if (label) {
                  label += ': ';
                }
                if (context.raw !== null) {
                  label += context.raw;
                }
                return label;
              }
            }
          },
          legend: {
            position: 'bottom',
            align: 'center',
            labels: {
              usePointStyle: true,
              boxWidth: 10
            }
          }
        }
      }
    };

    if (this.chart) {
      this.chart.destroy();
    }

    this.chart = new Chart(ctx, chartConfig);
  }
  return() {
    this.gotoList(); 
  }

  gotoList() {
    this.router.navigate(['/getAllCharts']);
  }

}
