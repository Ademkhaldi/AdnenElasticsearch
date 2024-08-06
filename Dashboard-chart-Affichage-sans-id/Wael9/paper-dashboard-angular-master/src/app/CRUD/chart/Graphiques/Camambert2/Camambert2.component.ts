import { Component, Input, OnInit } from '@angular/core';
import { Chart, ChartConfiguration, registerables } from 'chart.js';
import { DatasourceService } from '../../../datasource/service/datasource.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-Camambert2',
  templateUrl: './Camambert2.component.html',
  styleUrls: ['./Camambert2.component.scss']
})
export class Camambert2Component implements OnInit {

  public chart: Chart<'pie', number[], string> | undefined;
  public camabertData: any = [];
//  private chartId: string = ''; // Store the chart ID
  @Input() chartId: string = '';

  constructor(
    private datasourceService: DatasourceService,
    private route: ActivatedRoute
  ) { 
    Chart.register(...registerables); // Register Chart.js components
  }

  
  ngOnInit(): void {
    if(!this.chartId){
      this.route.paramMap.subscribe(params => {
        this.chartId = params.get('id') || '';
      });
    }
    this.fetchData(this.chartId);
  }


  fetchData(chartId: string) {
    this.datasourceService.getCamambertData(chartId)
      .subscribe(data => {
        console.log('Fetched Data:', data); // Log the data
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
      return; // Do not create chart if no data is available
    }

    console.log('Creating Chart...');
    const labels = this.camabertData.map((stat: any) => stat.label);
    const values = this.camabertData.map((stat: any) => stat.value);

    const backgroundColors = labels.map(() => this.getRandomColor());

    const chartConfig: ChartConfiguration<'pie', number[], string> = {
        type: 'pie',
        data: {
            labels,
            datasets: [{
                label: 'Data',
                backgroundColor: backgroundColors,
                borderWidth: 0,
                data: values as number[] // Ensure values are typed as number[]
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
