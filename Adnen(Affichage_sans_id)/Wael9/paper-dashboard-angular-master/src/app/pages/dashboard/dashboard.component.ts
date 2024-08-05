import { Component, OnInit } from '@angular/core';
import { KPIDataService } from 'app/KPI/KPIdata.service';
import { EmailDataCamembertService } from 'app/Camembert/emaildatacamembert.service';
import { LineChartService } from 'app/Linear/linechart.service';
import { AreaUseractivityService } from 'app/Area/Areauseractivity.service';
import { Chart, ChartType, ChartConfiguration ,registerables, ChartTypeRegistry, ChartOptions } from 'chart.js/auto';

@Component({
  selector: 'dashboard-cmp',
  moduleId: module.id,
  templateUrl: 'dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  public chart: Chart<ChartType, any, any>;
  public emailData: any = []; // Initialize with empty array
  public lineChartData: any;

  public capacity: string;
  public revenue: string;
  public errors: string;
  public followers: string;

  constructor(
    private emailDataCamembertService: EmailDataCamembertService,
    private lineChartService: LineChartService,
    private areauserActivityService: AreaUseractivityService,
    private KPIdataService: KPIDataService
  
  ) {
    Chart.register(...registerables);

  }

  ngOnInit() {
    this.KPIdataService.getDataKPI().subscribe(data => {
      this.capacity = data.capacity;
      this.revenue = data.revenue;
      this.errors = data.errors;
      this.followers = data.followers;
    });

    this.areauserActivityService.getAreaUserActivityData().subscribe(data => {
      this.createArea(data);
    });

    this.emailDataCamembertService.getEmailDataCamabert().subscribe(data => {
      this.emailData = data;
      this.createChart();
    });

    this.lineChartService.getLineChartData().subscribe(data => {
      this.lineChartData = data;
      this.createLineChart();
    });
  }

  createChart() {
    const labels = this.emailData.map(stat => stat.label);
    const values = this.emailData.map(stat => stat.value);

    const backgroundColors = labels.map(() => this.getRandomColor());

    const chartConfig: ChartConfiguration<'pie', number[], string> = {
        type: 'pie',
        data: {
            labels,
            datasets: [{
                label: 'Emails',
                backgroundColor: backgroundColors,
                borderWidth: 0,
                data: values
            }]
        },
        options: {
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function (context) {
                            var label = context.label || '';
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
                        usePointStyle: true, // Use point style (circle)
                        boxWidth: 10 // Size of circles in legend
                    }
                }
            }
        }
    };

    this.chart = new Chart('chartEmail', chartConfig as ChartConfiguration<keyof ChartTypeRegistry, any, any>);
}



createLineChart() {
  const canvas = document.getElementById("lineChartCanvas") as HTMLCanvasElement;
  const ctx = canvas.getContext("2d");

  const lineChartOptions = {
      plugins: {
          legend: {
              display: false, // Adjust based on your needs
              position: 'top' as const // Use 'top', 'bottom', etc.
          }
      },
      // Add any additional options here
  } as const;

  const lineChart = new Chart(ctx, {
      type: 'line',
      data: {
          labels: this.lineChartData.labels,
          datasets: this.lineChartData.datasets
      },
      options: lineChartOptions as ChartOptions<'line'>
  });
}


createArea(data: any): void {
  const canvas: HTMLCanvasElement = <HTMLCanvasElement>document.getElementById("chartHours");
  const ctx = canvas.getContext("2d");

  new Chart(ctx, {
      type: 'line', // Change this to 'bar' if you're using bar-related properties
      data: data,
      options: {
          scales: {
              y: {
                  beginAtZero: false,
                  ticks: {
                      color: "#9f9f9f",
                      maxTicksLimit: 5,
                  },
                  grid: {
                      color: 'rgba(255, 255, 255, 0.05)',
                  }
              },
              x: {
                  grid: {
                      color: 'rgba(255, 255, 255, 0.1)',
                      display: false,
                  },
                  ticks: {
                      padding: 20,
                      color: "#9f9f9f"
                  }
                  // Remove barPercentage if the chart is not a bar chart
              }
          },
          elements: {
              line: {
                  fill: true
              }
          }
      }
  });
}







  getRandomColor() {
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }

    return color;
  }
}
