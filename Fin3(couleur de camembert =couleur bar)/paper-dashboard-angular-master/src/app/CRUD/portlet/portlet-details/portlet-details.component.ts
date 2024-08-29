import { Component, Input, OnInit } from '@angular/core';
import { Portlet } from '../portlet.model';
import { ActivatedRoute, Router } from '@angular/router';
import { PortletService } from '../service/portlet.service';

@Component({
  selector: 'app-portlet-details',
  templateUrl: './portlet-details.component.html',
  styleUrls: ['./portlet-details.component.scss']
})
export class PortletDetailsComponent implements OnInit {

  identifier: string = '';

  @Input() portlet: Portlet | undefined;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private portletService: PortletService, // Utilisez correctement le service MarketService
  ) {}

  ngOnInit(): void {
    if (this.portlet == null) {
      this.portlet = new Portlet();
    }
    this.identifier = this.route.snapshot.params['id'];

    this.portletService.retrievePortlet (this.identifier).subscribe(
      (data: Portlet) => {
        this.portlet = data;
      },
      (error) => console.log(error)
    );
  }

  list() {
    this.router.navigate(['/getAllPortlets']); // Ajustez la route de navigation
  }
}