import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Dashboard } from 'app/CRUD/dashboard/dashboard.model';
import { DashboardService } from 'app/CRUD/dashboard/service/dashboard.service';

@Component({
  selector: 'app-dashboard-details',
  templateUrl: './dashboard-details.component.html',
  styleUrls: ['./dashboard-details.component.scss']
})
export class DashboardDetailsComponent implements OnInit {
  identifier: string = '';

  @Input() dashboard: Dashboard | undefined;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private dashboardService: DashboardService, // Utilisez correctement le service MarketService
  ) {}

  ngOnInit(): void {
    if (this.dashboard == null) {
      this.dashboard = new Dashboard();
    }
    this.identifier = this.route.snapshot.params['id'];

    this.dashboardService.retrieveDashboard(this.identifier).subscribe(
      (data: Dashboard) => {
        this.dashboard = data;
      },
      (error) => console.log(error)
    );
  }

  list() {
    this.router.navigate(['/getAllDashboards']); // Ajustez la route de navigation
  }
}