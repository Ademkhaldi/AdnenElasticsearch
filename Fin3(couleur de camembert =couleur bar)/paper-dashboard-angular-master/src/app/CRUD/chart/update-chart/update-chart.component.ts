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
  
  datasource: Datasource = new Datasource();
  public users: User[] = [];
  user: User = new User();
  currentUser: User | null = null;
  updator_id: string = '';
  public datasources: Datasource[] = [];
  public indices: string[] = [];
  public x_axisOptions: string[] = [];
  public y_axisOptions: string[] = [];
  public aggregationOptions: string[] = [];
    
  // Utilisation de string au lieu de String pour les propriétés simples
  public x_axis: string = '';
  public y_axis: string = '';
  public aggreg: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private userService: UserService, 
    private chartService: ChartService, 
    private datasourceService: DatasourceService
  ) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    this.Datasourceid = this.route.snapshot.params['Datasourceid'];
  
    this.chartService.retrieveChart(this.id).subscribe(data => {
      console.log(data);
      this.chart = data;
      this.x_axis = this.chart.x_axis || '';
      this.y_axis = this.chart.y_axis || '';
      this.aggreg = this.chart.aggreg || '';
      // Assigner le datasource à partir des données du chart
    }, error => console.log(error));
  
    this.datasourceService.retrieveDatasource(this.Datasourceid).subscribe(data => {
      console.log(data);
      this.datasource = data;
      // Assigner le datasource à partir des données du chart
      this.loadIndices(); // Charger les indices après avoir récupéré le datasource
    }, error => console.log(error));
  
    this.reloadData2();
  
    this.datasourceService.getAllDatasources().subscribe(
      data => {
        this.datasources = data;
      },
      error => console.log(error)
    );
  }
  
  loadIndices() {
    if (this.Datasourceid) {
      this.datasourceService.getAllIndexByDatasourceId(this.Datasourceid).subscribe(
        data => {
          this.indices = data;
          this.chart.index = ''; // Réinitialiser chart.index lorsque les indices sont chargés
        },
        error => console.log(error)
      );
    }
  }
    
  loadAttributes() {
    if (this.Datasourceid && this.chart.index) {
      this.datasourceService.getAttributesByIndex(this.Datasourceid, this.chart.index).subscribe(
        (attributes: string[]) => {
          console.log('Received attributes:', attributes); // Vérifiez ce que vous recevez ici
          this.x_axisOptions = attributes;
          this.y_axisOptions = attributes;
          this.aggregationOptions = attributes;
          console.log(this.x_axisOptions, this.y_axisOptions, this.aggregationOptions);
        },
        error => console.log('Error fetching attributes:', error)
      );
    }
  }
        
  updateChart() {
    const errorMessages = [];
    if (this.chart.title.length === 0) {
        errorMessages.push({ inputId: 'title', message: "title ne peut pas être vide" });
      }  if (this.chart.x_axis.length === 0) {
        errorMessages.push({ inputId: 'x_axis', message: "x_axis ne peut pas être vide" });
      }
      if (this.chart.y_axis.length === 0) {
        errorMessages.push({ inputId: 'y_axis', message: "y_axis ne peut pas être vide" });
      }
      if (this.chart.index.length === 0) {
        errorMessages.push({ inputId: 'index', message: "index ne peut pas être vide" });
      }
    
    
    
    
    
    // Si des erreurs sont présentes, les afficher toutes
    if (errorMessages.length > 0) {
      errorMessages.forEach(error => {
        this.showErrorMessage(error.inputId, error.message);
      });
      return; // Arrêtez le processus de sauvegarde si des erreurs existent
    }
    const updateData = {
      ...this.chart,
      updator_id: this.updator_id
    };

    this.chartService.updateChart(this.id, this.chart.datasource?.type, updateData).subscribe(
      (data) => {
        console.log(data);
        this.gotoList();
      },
      (error) => {
        console.log(error);
        this.gotoList();
      }
    );
  }

  showErrorMessage(inputId: string, message: string): void {
    const inputElement = document.getElementById(inputId);
    const errorDiv = inputElement?.nextElementSibling;
    if (errorDiv && errorDiv.classList.contains('text-danger')) {
      errorDiv.textContent = message;
    } else {
      const div = document.createElement('div');
      div.textContent = message;
      div.classList.add('text-danger');
      inputElement?.insertAdjacentElement('afterend', div);
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
      this.userService.retrieveUser(currentUser.id)
        .subscribe(
          data => {
            this.user = data;
            this.updator_id = this.user.username || ''; 
          },
          error => console.log(error)
        );
    }
  }

  cancelUpdateChart() {
    this.gotoList(); 
  }

  onDatasourceChange(event: Event) {
    const selectElement = event.target as HTMLSelectElement;
    const newDatasourceId = selectElement.value;
    this.Datasourceid = newDatasourceId;
    this.loadIndices(); // Recharger les indices pour le nouveau datasource
  }
    
  onIndexChange() {
    this.loadAttributes(); // Recharger les attributs lorsque l'index change
  }
}
