import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DatasourceService } from 'app/CRUD/datasource/service/datasource.service';

@Component({
  selector: 'app-table2',
  templateUrl: './table2.component.html',
  styleUrls: ['./table2.component.scss']
})
export class Table2Component implements OnInit {

  public tableData: any = [];
  @Input() chartId: string = '';

  constructor(
    private datasourceService: DatasourceService,
    private route: ActivatedRoute
  ) { }

  


  ngOnInit(): void {
    if(!this.chartId){
      this.route.paramMap.subscribe(params => {
        this.chartId = params.get('id') || '';
      });
    }
    this.fetchData2(this.chartId);
  }


  fetchData2(chartId: string) {
    this.datasourceService.getTableData(chartId)
      .subscribe(data => {
        console.log('Fetched Data:', data);
        this.tableData = data;
      });
  }
}
