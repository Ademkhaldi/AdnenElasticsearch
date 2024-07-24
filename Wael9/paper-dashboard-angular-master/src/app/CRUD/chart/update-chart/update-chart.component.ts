import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Chart } from '../chart.model';
import { charttypeLabelMapping, charttype } from '../charttype.model';
import { ChartService } from '../service/chart.service';
import { AuthService } from 'app/USERALLL/USERALL/_services/auth.service';
import { UserService } from 'app/USERALLL/USERALL/_services/user.service';
import { User } from 'app/USERALLL/USERALL/user/user.model';

@Component({
  selector: 'app-update-chart',
  templateUrl: './update-chart.component.html',
  styleUrls: ['./update-chart.component.scss']
})
export class UpdateChartComponent implements OnInit {
  public charttypeLabelMapping = charttypeLabelMapping;
  public Charttypes = Object.values(charttype);
  
  id: string = '';
  chart: Chart = new Chart();
  public users: User[] = [];
  user: User = new User();
  currentUser: User | null = null; // Déclarez la variable currentUser de type User ou null
  updator_id: string ; // Nouveau champ creator_id
  constructor(private route: ActivatedRoute, private router: Router,
    private authService: AuthService,private userService: UserService,private chartService: ChartService) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];

    this.chartService.retrieveChart(this.id)
      .subscribe(data => {
        console.log(data);
        this.chart = data;
      }, error => console.log(error));
            this.reloadData2(); 

  } 

  updateChart() {

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


    const updateData = {
      ...this.chart, // Copier toutes les autres propriétés du tableau de bord
      updator_id: this.updator_id // Ajouter l'updator_id
    };
  
    this.chartService.updateChart(this.id, updateData).subscribe(
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
      this.userService.retrieveUser(currentUser.id)
        .subscribe(
          data => {
            console.log(data);
            this.user = data;
            this.updator_id = this.user.username; // Update creator_id with the retrieved username
          },
          error => console.log(error)
        );
    }
  }
  cancelUpdateChart() {
    this.gotoList(); // Naviguer vers la liste des tableaux de bord
  }
}


