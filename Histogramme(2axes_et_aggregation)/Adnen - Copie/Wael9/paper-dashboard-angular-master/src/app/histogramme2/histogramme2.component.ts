import { AfterViewInit, Component, OnInit } from '@angular/core';
import { Chart, ChartConfiguration } from 'chart.js';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { DatasourceService } from 'app/CRUD/datasource/service/datasource.service';

@Component({
  selector: 'app-histogramme2',
  templateUrl: './histogramme2.component.html',
  styleUrls: ['./histogramme2.component.scss']
})
export class Histogramme2Component implements OnInit, AfterViewInit {

  public chart: Chart<'bar', number[], string> | undefined;
  public histogrammeData: any = {};
  public searchForm: FormGroup;

  constructor(
    private datasourceService: DatasourceService,
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) { 
    this.searchForm = this.fb.group({
      chartId: ['']
    });
  }

  ngAfterViewInit(): void {
    if (Object.keys(this.histogrammeData).length > 0) {
      this.createChart();
    }    
  }

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      const chartId = params.get('chartId') || '';
      this.searchForm.setValue({ chartId });

      if (chartId) {
        this.fetchData(chartId);
      }
    });
  }

  fetchData(chartId: string) {
    this.datasourceService.getHistogramme2(chartId)
      .subscribe(data => {
        console.log('Fetched Data:', data);
        this.histogrammeData = data;
        this.createChart();
      }, error => {
        console.error('Error fetching histogram data:', error);
      });
  }

  onSubmit() {
    const chartId = this.searchForm.get('chartId')?.value;
    if (chartId) {
      this.fetchData(chartId);
    }
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

    if (!this.histogrammeData || Object.keys(this.histogrammeData).length === 0) {
      return;
    }

    console.log('Creating Chart...');
    const dates = [];
    const values = [];
    const backgroundColors = [];

    for (const assetType in this.histogrammeData) {
      if (this.histogrammeData.hasOwnProperty(assetType)) {
        this.histogrammeData[assetType].forEach((stat: any) => {
          dates.push(stat.Date);
          values.push(stat.Value);
          backgroundColors.push(this.getRandomColor());
        });
      }
    }

    const chartConfig: ChartConfiguration<'bar', number[], string> = {
      type: 'bar',
      data: {
        labels: dates,
        datasets: [{
          label: 'Values',
          backgroundColor: backgroundColors,
          borderWidth: 1,
          data: values
        }]
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

  getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

  hasData(): boolean {
    return Object.keys(this.histogrammeData).length > 0;
  }
}
