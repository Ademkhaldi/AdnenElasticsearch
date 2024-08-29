import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PortletDashboardAssignmentComponent } from './portlet-dashboard-assignment.component';

describe('PortletAssignmentComponent', () => {
  let component: PortletDashboardAssignmentComponent;
  let fixture: ComponentFixture<PortletDashboardAssignmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PortletDashboardAssignmentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PortletDashboardAssignmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
