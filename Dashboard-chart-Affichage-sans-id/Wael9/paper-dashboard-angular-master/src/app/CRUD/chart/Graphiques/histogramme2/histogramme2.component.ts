import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { Chart, ChartConfiguration } from 'chart.js';
import { ActivatedRoute } from '@angular/router';
import { DatasourceService } from 'app/CRUD/datasource/service/datasource.service';

@Component({
  selector: 'app-histogramme2',
  templateUrl: './histogramme2.component.html',
  styleUrls: ['./histogramme2.component.scss']
})
export class Histogramme2Component implements OnInit {
  public chart: Chart<'bar', number[], string> | undefined;
  public histogrammeData: any = {};
  @Input() chartId: string = '';
  @Input() idChart: string = ''; // Accept chartId as input
  constructor(
    private datasourceService: DatasourceService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    if(!this.chartId){
      this.route.paramMap.subscribe(params => {
        this.chartId = params.get('id') || '';
      });
    }
    this.fetchData(this.chartId);
  }

  fetchData(chartId: string) {
    this.datasourceService.getHistogramme2(chartId)
      .subscribe(data => {
        console.log('Fetched Data:', data);
        this.histogrammeData = data;
        // Delay chart creation to ensure the view is fully initialized
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
    const dates: Set<string> = new Set();
    const datasets: { label: string, backgroundColor: string, borderWidth: number, data: number[] }[] = [];

    for (const assetType in this.histogrammeData) {
      if (this.histogrammeData.hasOwnProperty(assetType)) {
        const data: number[] = [];
        this.histogrammeData[assetType].forEach((stat: any) => {
          dates.add(stat.Date);
          data.push(stat.Value);
        });
        datasets.push({
          label: assetType,
          backgroundColor: this.getColorForAssetType(assetType),
          borderWidth: 1,
          data: data
        });
      }
    }

    const chartConfig: ChartConfiguration<'bar', number[], string> = {
      type: 'bar',
      data: {
        labels: Array.from(dates),
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
            align: 'start',
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

  getColorForAssetType(assetType: string): string {
    switch (assetType) {
      case 'equities':
        return '#FF6384'; // Rouge
      case 'bonds':
        return '#36A2EB'; // Bleu
      case 'cash':
        return '#FFCE56'; // Jaune
      default:
        return '#000000'; // Default color
    }
  }

  hasData(): boolean {
    return Object.keys(this.histogrammeData).length > 0;
  }
}
