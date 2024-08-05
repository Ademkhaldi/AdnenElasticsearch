import { Component, OnInit } from "@angular/core";
import { DatasourceService } from "../service/datasource.service";

@Component({
  selector: 'app-elastic',
  templateUrl: './elastic.component.html',
  styleUrls: ['./elastic.component.scss']
})
export class ElasticComponent implements OnInit {
  idDatasource: string = '';
  elasticsearchUrl: string = '';
  errorMessage: string = '';
  
  constructor(private datasourceService: DatasourceService) { }

  ngOnInit(): void {}

  getElasticsearchUrl(): void {
    this.errorMessage = ''; // Clear error message before making the request
    this.elasticsearchUrl = ''; // Clear URL before making the request

    if (this.idDatasource.trim() === '') {
      this.errorMessage = 'Datasource ID cannot be empty';
      return;
    }

    console.log('Requesting Elasticsearch URL for Datasource ID:', this.idDatasource);

    this.datasourceService.getElasticsearchUrl(this.idDatasource).subscribe(
      (response: string) => {
        console.log('Received response:', response);
        this.elasticsearchUrl = response;
      },
      (error) => {
        console.error('Error occurred:', error);
        console.log('Error details:', error);
        if (error.status === 400) {
          this.errorMessage = 'Bad Request. Please check the Datasource ID.';
        } else if (error.status === 404) {
          this.errorMessage = 'Datasource or Chart not found.';
        } else {
          this.errorMessage = 'An unexpected error occurred.';
        }
      }
    );
  }
}
