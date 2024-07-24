import { AfterViewInit, Component, OnInit } from '@angular/core';
import { Chart, ChartConfiguration } from 'chart.js';
import { DatasourceService } from '../service/datasource.service';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-chart-data',
  templateUrl: './chart-data.component.html',
  styleUrls: ['./chart-data.component.scss']
})
export class ChartDataComponent implements OnInit, AfterViewInit {

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
      datasourceId: [''],
      index: [''],
      xAxisField: [''],
      yAxisField: ['']
    });
  }
  ngAfterViewInit(): void {
    this.createChart(); // Initialize the chart after the view is initialized
  }
  
  ngOnInit(): void {
    // Retrieve dynamic values from route parameters and set them in the form
    this.route.paramMap.subscribe(params => {
      const datasourceId = params.get('datasourceId') || '';
      const index = params.get('index') || '';
      const xAxisField = params.get('xAxisField') || '';
      const yAxisField = params.get('yAxisField') || '';
  
      this.searchForm.setValue({
        datasourceId,
        index,
        xAxisField,
        yAxisField
      });
      
      // Fetch the data initially if parameters are present
      this.fetchData();
    });
  }
  

fetchData() {
  const { datasourceId, index, xAxisField, yAxisField } = this.searchForm.value;

  if (datasourceId && index && xAxisField && yAxisField) {
    this.datasourceService.getCamambertData(datasourceId, index, xAxisField, yAxisField)
      .subscribe(data => {
        console.log('Fetched Data:', data); // Log the data
        this.camabertData = data;
        this.createChart();
      });
  }
}

  

  onSubmit() {
    this.fetchData(); // Fetch data when form is submitted
  }

  createChart() {
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
      this.chart.destroy(); // Destroy existing chart if present
    }
  
    this.chart = new Chart('chartEmail', chartConfig);
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
