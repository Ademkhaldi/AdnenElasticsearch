import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GraphiquesDashboardsComponent } from './graphiques-dashboards.component';

describe('GraphiquesDashboardsComponent', () => {
  let component: GraphiquesDashboardsComponent;
  let fixture: ComponentFixture<GraphiquesDashboardsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GraphiquesDashboardsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GraphiquesDashboardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
