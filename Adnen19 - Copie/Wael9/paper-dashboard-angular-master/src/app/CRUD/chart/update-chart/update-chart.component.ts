import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Chart } from '../chart.model';
import { charttypeLabelMapping, charttype } from '../charttype.model';
import { ChartService } from '../service/chart.service';
import { AuthService } from 'app/USERALLL/USERALL/_services/auth.service';
import { UserService } from 'app/USERALLL/USERALL/_services/user.service';
import { User } from 'app/USERALLL/USERALL/user/user.model';
import { Datasource } from 'app/CRUD/datasource/datasource.model';
import { DatasourceService } from 'app/CRUD/datasource/service/datasource.service';

@Component({
  selector: 'app-update-chart',
  templateUrl: './update-chart.component.html',
  styleUrls: ['./update-chart.component.scss']
})
export class UpdateChartComponent implements OnInit {
  public charttypeLabelMapping = charttypeLabelMapping;
  public Charttypes = Object.values(charttype);

  id: string = '';
  Datasourceid: string = '';
  chart: Chart = new Chart();

  datasource: Datasource = new Chart();
  public users: User[] = [];
  user: User = new User();
  currentUser: User | null = null;
  updator_id: string;
  public datasources: Datasource[] = [];
  public indices: String[] = [];
  public attributes: String[] = [];

  constructor(
    private route: ActivatedRoute, private router: Router,
    private authService: AuthService, private userService: UserService, 
    private chartService: ChartService, private datasourceService: DatasourceService
  ) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    this.Datasourceid = this.route.snapshot.params['Datasourceid'];
    this.reloadData2();
    this.datasourceData();
    this.loadIndices(); // Charger les indices immédiatement
  
    this.chartService.retrieveChart(this.id).subscribe(
      data => {
        console.log("Fetched Chart Data:", data);
        this.chart = data;
        this.datasourceService.retrieveDatasource(this.Datasourceid).subscribe(
          data => {
            console.log("Fetched Datasource Data:", data);
            this.datasource = data;
            this.checkIndex();

            this.checkAggregation();
          },
          error => console.log(error)
        );
      },
      error => console.log(error)
    );
  }
  
  checkIndex() {
    // Si chart.aggreg n'est pas dans les attributs disponibles, le réinitialiser à une chaîne vide
    if (this.indices.indexOf(this.chart.index) === -1) {
      this.chart.index = '';
    }
  }

  onTypeChange(newType: string) {
    // Assurez-vous que newType est une valeur valide pour charttype
    if (Object.values(charttype).includes(newType as charttype)) {
      this.chart.type = newType as charttype;
    } else {
      console.error('Invalid chart type');
    }
    
    this.resetAggregationField();
    // Vous pouvez également ajouter d'autres actions en fonction du changement de type
  }  

  resetAggregationField() {
    // Si le type est 'Pie' ou 'Table', réinitialiser 'aggreg'
    if (this.chart.type === charttype.Pie || this.chart.type === charttype.Table) {
      this.chart.aggreg = '';
    }
  }
  

  checkAggregation() {
    // Si chart.aggreg n'est pas dans les attributs disponibles ou si le type est Table ou Pie, réinitialiser à une chaîne vide
    if (this.attributes.indexOf(this.chart.aggreg) === -1 || this.chart.type === 'Table' || this.chart.type === 'Pie') {
      this.chart.aggreg = '';
    }
  }

  

  updateChart() {
    if (!this.validateChart()) return;

    const updateData = {
      ...this.chart,
      updator_id: this.updator_id
    };

    this.chartService.updateChart(this.id, this.chart.datasource.type, updateData).subscribe(
      data => {
        console.log("Update Response:", data);
        this.gotoList();
      },
      error => {
        console.log(error);
        this.gotoList();
      }
    );
  }

  validateChart(): boolean {
    if (!this.chart.title) {
      this.showErrorMessage('title', "title ne peut pas être vide");
      return false;
    }
    if (!this.chart.x_axis) {
      this.showErrorMessage('x_axis', "x_axis ne peut pas être vide");
      return false;
    }
    if (!this.chart.y_axis) {
      this.showErrorMessage('y_axis', "y_axis ne peut pas être vide");
      return false;
    }
    if (!this.chart.index) {
      this.showErrorMessage('index', "index ne peut pas être vide");
      return false;
    }
    return true;
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

  onSubmit() {
    this.updateChart();
  }

  gotoList() {
    this.router.navigate(['/getAllCharts']);
  }

  reloadData2() {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser.id) {
      this.updator_id = currentUser.id;
      this.userService.retrieveUser(currentUser.id).subscribe(
        data => {
          this.user = data;
          this.updator_id = this.user.username;
        },
        error => console.log(error)
      );
    }
  }
  onDatasourceChange(datasourceId: string) {
    this.Datasourceid = datasourceId;
    this.loadIndices();
  }

  datasourceData() {
    this.datasourceService.getAllDatasources().subscribe(data => {
      this.datasources = data.map(datasource => ({ ...datasource }));
    });
  }

  loadIndices() {
    if (this.Datasourceid) {
      this.datasourceService.getAllIndexByDatasourceId(this.Datasourceid).subscribe(
        data => {
          this.indices = data;
          this.chart.index = ''; // Réinitialiser chart.index lorsque les indices sont chargés
          this.attributes = []; // Réinitialisez les attributs lorsqu'aucun index n'est sélectionné
        },
        error => console.log(error)
      );
    }
  }

  onIndexChange(index: string) {
    this.datasourceService.getAttributesByIndex(this.Datasourceid, index).subscribe(
      (data: string[]) => {
        this.attributes = data;
      },
      error => console.log(error)
    );
  }
  cancelUpdateChart() {
    this.gotoList();
  }
}
