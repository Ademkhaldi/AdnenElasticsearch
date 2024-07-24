import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChartDatasourceAssignmentComponent } from './chart-datasource-assignment.component';

describe('ChartDatasourceAssignmentComponent', () => {
  let component: ChartDatasourceAssignmentComponent;
  let fixture: ComponentFixture<ChartDatasourceAssignmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChartDatasourceAssignmentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChartDatasourceAssignmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
