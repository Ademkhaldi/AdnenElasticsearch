import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DatasourceService } from '../service/datasource.service';
import { Datasource } from '../datasource.model';
import { ChartService } from 'app/CRUD/chart/service/chart.service';
import { Chart } from 'app/CRUD/chart/chart.model';

@Component({
  selector: 'app-datasource-list',
  templateUrl: './datasource-list.component.html',
  styleUrls: ['./datasource-list.component.scss']
})
export class DatasourceListComponent implements OnInit {

  public datasources: Datasource[] = []; // Initialisez avec un tableau vide

  public chart: Chart | undefined;

  constructor(private datasourceService: DatasourceService,private router: Router,    private chartService: ChartService
  ) { }

  ngOnInit(): void {
    this.reloadData();
    this.loadChart();

  }

  reloadData() {
    this.datasourceService.getAllDatasources().subscribe(data => {
      this.datasources = data.map(Datasource => ({
        ...Datasource,
      }));
      console.log(data);
    });
  }
   

  loadChart() {
    this.chartService.getAllCharts().subscribe(data => {
      this.chart = data[0]; // Assuming you want to use the first chart
    });
  }
  affecterChartADatasource(idDatasource: any,idChart: any) {
    this.router.navigate(['datasource/affecterChartADatasource',idDatasource,idChart]);
  }




  createDatasource() {
    this.router.navigate(['/AddDatasource']); // Ajustez la route de création
  }
  updateDatasource(id: string) {
    this.router.navigate(['/UpdateDatasource', id]); // Ajustez la route de mise à jour
  }
  deleteDatasource(id: string) {
    this.datasourceService.deleteDatasource(id).subscribe(
      data => {
        console.log(data);
        this.reloadData();
      },
      error => console.log(error)
    );
  }
  deleteAllDatasources(): void {
    this.datasourceService.deleteAllDatasources()
      .subscribe({
        next: (res) => {
          console.log(res);
          this.reloadData();
        },
        error: (e) => console.error(e)
      });
  }
  datasourceDetails(id: string){
    this.router.navigate(['datasource/details', id]); // Ajustez la route de détails

  }

  
// Dans datasource-list.component.ts
hidePassword(password: string): string {
  return '*'.repeat(password.length); // Remplacer chaque caractère par un astérisque
}


  navigateToDashboardForm() {
    this.router.navigate(['getAllDatasources']); // Ajustez la route de navigation vers le formulaire
  }
}
