import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PortletsForDashboardGetComponent } from './portlets-for-dashboaord-get.component';

describe('PortletsForClientGetComponent', () => {
  let component: PortletsForDashboardGetComponent;
  let fixture: ComponentFixture<PortletsForDashboardGetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PortletsForDashboardGetComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PortletsForDashboardGetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
