import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChartToDatasourceAssignmentComponent } from './chart-to-datasource-assignment.component';

describe('ChartToDatasourceAssignmentComponent', () => {
  let component: ChartToDatasourceAssignmentComponent;
  let fixture: ComponentFixture<ChartToDatasourceAssignmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChartToDatasourceAssignmentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChartToDatasourceAssignmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
