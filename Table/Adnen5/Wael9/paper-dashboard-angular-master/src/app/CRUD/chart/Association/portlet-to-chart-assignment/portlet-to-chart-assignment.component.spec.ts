import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PortletToChartAssignmentComponent } from './portlet-to-chart-assignment.component';

describe('PortletToChartAssignmentComponent', () => {
  let component: PortletToChartAssignmentComponent;
  let fixture: ComponentFixture<PortletToChartAssignmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PortletToChartAssignmentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PortletToChartAssignmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
