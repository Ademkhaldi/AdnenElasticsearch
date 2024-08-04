import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { DatasourceService } from 'app/CRUD/datasource/service/datasource.service';

@Component({
  selector: 'app-table2',
  templateUrl: './table2.component.html',
  styleUrls: ['./table2.component.scss']
})
export class Table2Component implements OnInit {


  public searchForm: FormGroup; // FormGroup to manage form inputs
  public tableData: any = [];

  constructor(
    private datasourceService: DatasourceService,
    private route: ActivatedRoute,
    private fb: FormBuilder // Inject FormBuilder

  ) {     // Initialize the form with default values
    this.searchForm = this.fb.group({
      chartId: ['']
    });
}

ngOnInit(): void {
  // Retrieve dynamic values from query parameters and set them in the form
  this.route.queryParamMap.subscribe(params => {
    const chartId = params.get('chartId') || '';
    
    this.searchForm.setValue({
      chartId
    });

    // Fetch the data initially if parameters are present
    if (chartId) {
      this.fetchData2(chartId);
    }
  });
}
fetchData2(chartId: string) {
  this.datasourceService.getTableData(chartId)
    .subscribe(data => {
      console.log('Fetched Data:', data);
      this.tableData = data;
    });
}


onSubmit2() {
  const chartId = this.searchForm.get('chartId')?.value;
  if (chartId) {
    this.fetchData2(chartId); // Fetch data when form is submitted
 
  }


 }
}