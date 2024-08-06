import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatasourceService } from '../service/datasource.service';
import { Datasource } from '../datasource.model';

@Component({
  selector: 'app-datasource-details',
  templateUrl: './datasource-details.component.html',
  styleUrls: ['./datasource-details.component.scss']
})
export class DatasourceDetailsComponent implements OnInit {

  identifier: string = '';

  @Input() datasource: Datasource | undefined;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private datasourceService: DatasourceService, // Utilisez correctement le service MarketService
  ) {}

  ngOnInit(): void {
    if (this.datasource == null) {
      this.datasource = new Datasource();
    }
    this.identifier = this.route.snapshot.params['id'];

    this.datasourceService.retrieveDatasource(this.identifier).subscribe(
      (data: Datasource) => {
        this.datasource = data;
      },
      (error) => console.log(error)
    );
  }
  hidePassword(password: string): string {
    if (!password) {
      return ''; // Or some placeholder if the password is undefined
    }
    return '*'.repeat(password.length); // Replace each character with an asterisk
  }
    list() {
    this.router.navigate(['/getAllDatasources']); // Ajustez la route de navigation
  }
}