import { AfterViewInit, Component, OnInit } from '@angular/core';
import { Chart, ChartConfiguration, ChartType } from 'chart.js';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { DatasourceService } from '../service/datasource.service';

@Component({
  selector: 'app-histogramme',
  templateUrl: './histogramme.component.html',
  styleUrls: ['./histogramme.component.scss']
})
export class HistogrammeComponent implements OnInit, AfterViewInit {

  public chart: Chart<'bar', number[], string> | undefined;
  public histogrammeData: any = [];
  public searchForm: FormGroup; // FormGroup to manage form inputs

  constructor(
    private histogrammeService: DatasourceService,
    private route: ActivatedRoute,
    private fb: FormBuilder // Inject FormBuilder
  ) { 
    // Initialize the form with default values
    this.searchForm = this.fb.group({
      chartId: ['']
    });
  }

  ngAfterViewInit(): void {
    if (this.histogrammeData.length > 0) {
      this.createChart(); // Initialize the chart after the view has been checked
    }    
  }

  ngOnInit(): void {
    // Retrieve dynamic values from query parameters and set them in the form
    this.route.queryParamMap.subscribe(params => {
      const chartId = params.get('chartId') || '';

      this.searchForm.setValue({
        chartId
      });

      // Fetch the data initially if parameters are present
      if (chartId) {
        this.fetchData(chartId);
      }
    });
  }

  fetchData(chartId: string) {
    this.histogrammeService.getHistogrammeData(chartId)
      .subscribe(data => {
        console.log('Fetched Data:', data); // Log the data
        this.histogrammeData = data;
        this.createChart();
      });
  }

  onSubmit() {
    const chartId = this.searchForm.get('chartId')?.value;
    if (chartId) {
      this.fetchData(chartId); // Fetch data when form is submitted
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
  
    if (!this.histogrammeData || this.histogrammeData.length === 0) {
      return; // Do not create chart if no data is available
    }
  
    console.log('Creating Chart...');
    const dates = this.histogrammeData.map((stat: any) => stat.date);
    const values = this.histogrammeData.map((stat: any) => stat.value);
  
    const backgroundColors = dates.map(() => this.getRandomColor());
  
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
      this.chart.destroy(); // Destroy existing chart if present
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
}
