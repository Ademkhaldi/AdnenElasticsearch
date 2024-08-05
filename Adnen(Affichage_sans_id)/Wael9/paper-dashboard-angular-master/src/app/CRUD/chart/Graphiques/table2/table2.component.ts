import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DatasourceService } from 'app/CRUD/datasource/service/datasource.service';

@Component({
  selector: 'app-table2',
  templateUrl: './table2.component.html',
  styleUrls: ['./table2.component.scss']
})
export class Table2Component implements OnInit {

  public tableData: any = [];

  constructor(
    private datasourceService: DatasourceService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    // Retrieve chartId from route parameters
    this.route.paramMap.subscribe(params => {
      const chartId = params.get('id') || '';
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
}
