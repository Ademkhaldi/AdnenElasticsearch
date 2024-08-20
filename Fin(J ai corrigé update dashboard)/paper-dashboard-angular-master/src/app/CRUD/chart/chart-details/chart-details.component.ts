import { Component, Input, OnInit } from '@angular/core';
import { Chart } from '../chart.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ChartService } from '../service/chart.service';

@Component({
  selector: 'app-chart-details',
  templateUrl: './chart-details.component.html',
  styleUrls: ['./chart-details.component.scss']
})
export class ChartDetailsComponent implements OnInit {

  identifier: string = '';

  @Input() chart: Chart | undefined;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private chartService: ChartService, // Utilisez correctement le service MarketService
  ) {}

  ngOnInit(): void {
    if (this.chart == null) {
      this.chart = new Chart();
    }
    this.identifier = this.route.snapshot.params['id'];

    this.chartService.retrieveChart(this.identifier).subscribe(
      (data: Chart) => {
        this.chart = data;
      },
      (error) => console.log(error)
    );
  }

  list() {
    this.router.navigate(['/getAllCharts']); // Ajustez la route de navigation
  }
}