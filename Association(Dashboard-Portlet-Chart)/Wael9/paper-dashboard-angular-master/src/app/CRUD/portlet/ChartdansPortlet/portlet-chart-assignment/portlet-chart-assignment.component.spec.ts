import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PortletChartAssignmentComponent } from './portlet-chart-assignment.component';

describe('PortletChartAssignmentComponent', () => {
  let component: PortletChartAssignmentComponent;
  let fixture: ComponentFixture<PortletChartAssignmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PortletChartAssignmentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PortletChartAssignmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
