import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Chart } from '../chart.model';
import { ChartService } from '../service/chart.service';

@Component({
  selector: 'app-details-title',
  templateUrl: './details-title.component.html',
  styleUrls: ['./details-title.component.scss']
})
export class DetailsTitleComponent implements OnInit {


  title: string = '';

  @Input() chartDetails: Chart | undefined;

  constructor(
    private route: ActivatedRoute,
    private chartService: ChartService,
    private router: Router,

  ) {}

  ngOnInit(): void {
    if (this.chartDetails == null) {
      this.chartDetails = new Chart();
    }
    this.title = this.route.snapshot.params['title'];

    this.chartService.getChartByTitle(this.title).subscribe(
      (data: Chart) => {
        this.chartDetails = data;
      },
      (error) => console.log(error)
    );
  }
  list() {
    this.router.navigate(['/getAllCharts']); // Ajustez la route de navigation
  }
}