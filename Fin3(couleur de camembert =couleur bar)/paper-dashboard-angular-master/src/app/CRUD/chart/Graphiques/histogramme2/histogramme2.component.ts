// src/app/histogramme2/histogramme2.component.ts
import { Component, Input, OnInit } from '@angular/core';
import { Chart, ChartConfiguration } from 'chart.js';
import { ActivatedRoute, Router } from '@angular/router';
import { DatasourceService } from 'app/CRUD/datasource/service/datasource.service';
import { ChartService } from '../../service/chart.service';
import { ColorService } from 'app/services/color.service';

@Component({
  selector: 'app-histogramme2',
  templateUrl: './histogramme2.component.html',
  styleUrls: ['./histogramme2.component.scss']
})
export class Histogramme2Component implements OnInit {
  public chart: Chart<'bar', number[], string> | undefined;
  public histogrammeData: any = {};
  @Input() chartId: string = '';
  public chartTitle: string = '';
  @Input() showButton: boolean = true; // Nouvelle propriété pour afficher/masquer le bouton


  constructor(
    private chartService: ChartService,
    private datasourceService: DatasourceService,
    private route: ActivatedRoute,
    private router: Router,
    private colorService: ColorService
  ) { }

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
    this.datasourceService.getHistogramme2(chartId)
      .subscribe(data => {
        console.log('Fetched Data:', data);
        this.histogrammeData = data;
        setTimeout(() => {
          this.createChart();
        }, 0);
      }, error => {
        console.error('Error fetching histogram data:', error);
      });
  }

  createChart() {
    const canvas = document.getElementById('histogrammeChartContainer') as HTMLCanvasElement;
    if (!canvas) {
      console.error('Canvas element not found!');
      return;
    }
    const ctx = canvas.getContext('2d');
    if (!ctx) {
      console.error('Failed to get 2D context from canvas!');
      return;
    }

    if (!this.histogrammeData || Object.keys(this.histogrammeData).length === 0) {
      return;
    }

    console.log('Creating Chart...');
    const Labels: Set<string> = new Set();
    const datasets: { label: string, backgroundColor: string, borderWidth: number, data: number[] }[] = [];

    for (const aggreg in this.histogrammeData) {
      if (this.histogrammeData.hasOwnProperty(aggreg)) {
        const Values: number[] = [];
        this.histogrammeData[aggreg].forEach((stat: any) => {
          Labels.add(stat.label);
          Values.push(stat.value);
        });

        const capitalizedAggreg = this.capitalizeFirstLetter(aggreg);

        datasets.push({
          label: capitalizedAggreg,
          backgroundColor: this.colorService.getColorForLabel(capitalizedAggreg), // Assign a color based on label
          borderWidth: 1,
          data: Values
        });
      }
    }

    const chartConfig: ChartConfiguration<'bar', number[], string> = {
      type: 'bar',
      data: {
        labels: Array.from(Labels),
        datasets: datasets
      },
      options: {
        responsive: true,
        scales: {
          x: {
            beginAtZero: true
          },
          y: {
            beginAtZero: true
          }
        },
        plugins: {
          tooltip: {
            callbacks: {
              label: function (context) {
                let label = context.dataset.label || '';
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

  capitalizeFirstLetter(string: string): string {
    return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
  }

  hasData(): boolean {
    return Object.keys(this.histogrammeData).length > 0;
  }
  return() {
    this.gotoList(); 
  }

  gotoList() {
    this.router.navigate(['/getAllCharts']);
  }

}
