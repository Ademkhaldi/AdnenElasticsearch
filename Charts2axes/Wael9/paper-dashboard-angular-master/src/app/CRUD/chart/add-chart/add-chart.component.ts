import { Component, OnInit } from '@angular/core';
import { ChartService } from '../service/chart.service';
import { Router } from '@angular/router';
import { charttype, charttypeLabelMapping } from '../charttype.model';
import { Chart } from '../chart.model';
import { User } from 'app/USERALLL/USERALL/user/user.model';
import { AuthService } from 'app/USERALLL/USERALL/_services/auth.service';
import { UserService } from 'app/USERALLL/USERALL/_services/user.service';
import { DatasourceService } from 'app/CRUD/datasource/service/datasource.service';
import { Datasource } from 'app/CRUD/datasource/datasource.model';

@Component({
  selector: 'app-add-chart',
  templateUrl: './add-chart.component.html',
  styleUrls: ['./add-chart.component.scss']
})
export class AddChartComponent implements OnInit {
  identifier: string = '';
  idDatasource: string = '';
  public charttypeLabelMapping = charttypeLabelMapping;
  public Charttypes = Object.values(charttype);
  public datasources: Datasource[] = [];
  public indices: String[] = [];
  public attributes: String[] = [];
  chart: Chart = {
    title: '',
    type: charttype.Line,
    x_axis: '',
    y_axis: '',
    index: '',
  };
  submitted = false;
  public users: User[] = [];
  updator_id: string;
  user: User = new User();
  currentUser: User | null = null;
  creator_id: string;
  navbarTitle: string = 'List';

  constructor(
    private chartService: ChartService,
    private router: Router,
    private authService: AuthService,
    private userService: UserService,
    private datasourceService: DatasourceService
  ) { }

  ngOnInit(): void {
    this.reloadData2();
    this.datasourceData();
  }

  IndexData(idDataSource: String) {
    this.datasourceService.getAllIndexByDatasourceId(idDataSource).subscribe(
      (data: string[]) => {
        this.indices = data;
        console.log(this.indices);
        // Reset attributes when index changes
        this.attributes = [];
      },
      (error) => console.log(error)
    );
  }

  onIndexChange(index: string) {
    console.log(`idDataSource: ${this.idDatasource}, index: ${index}`);  // Debug log

    this.datasourceService.getAttributesByIndex(this.idDatasource, index).subscribe(
      (data: string[]) => {
        console.log('Attributes received:', data);  // Check if attributes are received

        if (data && data.length > 0) {
          this.attributes = data;
          console.log('Updated attributes:', this.attributes);  // Check if attributes are updated
        } else {
          console.log('No attributes found for the given index.');
        }
      },
      (error) => {
        console.log('Error fetching attributes:', error);  // Log any errors
      }
    );
  }

  datasourceData() {
    this.datasourceService.getAllDatasources().subscribe(data => {
      this.datasources = data.map(Datasource => ({
        ...Datasource,
      }));
      console.log(data);
    });
  }

  reloadData2() {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser.id) {
      this.creator_id = currentUser.id;
      this.userService.retrieveUser(currentUser.id)
        .subscribe(
          data => {
            console.log(data);
            this.user = data;
            this.creator_id = this.user.username;
          },
          error => console.log(error)
        );
    }
  }

  saveChart(): void {
    if (this.chart.title.length === 0 ) {
      this.showErrorMessage('title', "title ne peut pas être vide");
      return;
    }

    if (this.chart.x_axis.length === 0 ) {
      this.showErrorMessage('x_axis', "x_axis ne peut pas être vide");
      return;
    }

    if (this.chart.y_axis.length === 0 ) {
      this.showErrorMessage('y_axis', "y_axis ne peut pas être vide");
      return;
    }
    if (this.chart.index.length === 0 ) {
      this.showErrorMessage('index', "index ne peut pas être vide");
      return;
    }

    const data = {
      title: this.chart.title,
      type: this.chart.type,
      x_axis: this.chart.x_axis,
      y_axis: this.chart.y_axis,
      index: this.chart.index,
      creator_id: this.creator_id,
      updator_id: this.creator_id
    };

    this.chartService.createChart(this.chart, this.idDatasource)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.submitted = true;
        },
        error: (e) => {
          console.error(e);
        }
      });
  }

  showErrorMessage(inputId: string, message: string): void {
    const inputElement = document.getElementById(inputId);
    const errorDiv = inputElement.nextElementSibling;
    if (errorDiv && errorDiv.classList.contains('text-danger')) {
      errorDiv.textContent = message;
    } else {
      const div = document.createElement('div');
      div.textContent = message;
      div.classList.add('text-danger');
      inputElement.insertAdjacentElement('afterend', div);
    }
  }  

  newChart(): void {
    this.submitted = false;
    this.chart = {
      title: '',
      type: charttype.Line,
      x_axis: '',
      y_axis: '',
      index: '',
    };
  }

  gotoList() {
    this.router.navigate(['/getAllCharts']);
  }
}
