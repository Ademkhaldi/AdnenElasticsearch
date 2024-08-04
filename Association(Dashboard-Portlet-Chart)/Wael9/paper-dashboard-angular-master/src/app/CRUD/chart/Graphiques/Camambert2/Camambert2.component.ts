import { AfterViewInit, Component, OnInit } from '@angular/core';
import { Chart, ChartConfiguration } from 'chart.js';
import { DatasourceService } from '../../../datasource/service/datasource.service';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-Camambert2',
  templateUrl: './Camambert2.component.html',
  styleUrls: ['./Camambert2.component.scss']
})
export class Camambert2Component implements OnInit, AfterViewInit {

  public chart: Chart<'pie', string, string> | undefined;
  public camabertData: any = [];
  public searchForm: FormGroup; // FormGroup to manage form inputs

  constructor(
    private datasourceService: DatasourceService,
    private route: ActivatedRoute,
    private fb: FormBuilder // Inject FormBuilder
  ) { 
    // Initialize the form with default values
    this.searchForm = this.fb.group({
      chartId: ['']
    });
  }

  ngAfterViewInit(): void {
    if (this.camabertData.length > 0) {
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
        this.fetchData3(chartId);
      }
    });
  }

  fetchData3(chartId: string) {
    this.datasourceService.getCamambertData(chartId)
      .subscribe(data => {
        console.log('Fetched Data:', data); // Log the data
        this.camabertData = data;
        this.createChart();
      });
  }

 

  onSubmit3() {
    const chartId = this.searchForm.get('chartId')?.value;
    if (chartId) {
      this.fetchData3(chartId); // Fetch data when form is submitted

    }
  }
  


  createChart() {
    const canvas = document.getElementById('camambertChartContainer') as HTMLCanvasElement;
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
  
    const backgroundColors = labels.map(() => this.getRandomColor());
  
    const chartConfig: ChartConfiguration<'pie', string, string> = {
      type: 'pie',
      data: {
        labels,
        datasets: [{
          label: 'Data',
          backgroundColor: backgroundColors,
          borderWidth: 0,
          data: values
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
}