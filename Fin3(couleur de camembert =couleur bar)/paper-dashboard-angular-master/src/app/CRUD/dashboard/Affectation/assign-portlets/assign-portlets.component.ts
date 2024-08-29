import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../../service/dashboard.service';
import { Router } from '@angular/router';
import { Portlet } from 'app/CRUD/portlet/portlet.model';
import { Dashboard } from '../../dashboard.model';
import { PortletService } from 'app/CRUD/portlet/service/portlet.service';

@Component({
  selector: 'app-assign-portlets',
  templateUrl: './assign-portlets.component.html',
  styleUrls: ['./assign-portlets.component.scss']
})
export class AssignPortletsComponent implements OnInit {
  portlets: Portlet[] = [];
  dashboards: Dashboard[] = [];
  selectedDashboardId: string | null = null;
  selectedPortlet: Portlet = new Portlet(); // Portlet sélectionné
  id: string;

  constructor(private dashboardService: DashboardService, private router: Router, private portletService: PortletService) { }

  ngOnInit(): void {
    this.loadDashboards();
    this.loadPortlets();
  }

  loadDashboards() {
    this.dashboardService.getAllDashboards().subscribe(data => {
      this.dashboards = data;
    });
  }

  loadPortlets() {
    this.portletService.getAllPortlets().subscribe(data => {
      this.portlets = data;
    });
  }

  assignerListePortletsADashboard(): void {
    if (!this.selectedDashboardId) {
      console.error('No dashboard selected.');
      return;
    }

    this.dashboardService.assignerListePortletsADashboard(this.selectedDashboardId, [this.selectedPortlet]).subscribe({
      next: (dashboard) => {
        console.log('Portlets assigned successfully to dashboard:', dashboard);
        //this.errorOccurred = false; // Réinitialiser l'état d'erreur
        this.router.navigate(['dashboard/getPortletsForDashboard', this.selectedDashboardId]);
      },
    });
  }
}
