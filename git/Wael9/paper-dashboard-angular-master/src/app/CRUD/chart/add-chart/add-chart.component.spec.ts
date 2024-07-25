import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddChartComponent } from './add-chart.component';

describe('AddChartComponent', () => {
  let component: AddChartComponent;
  let fixture: ComponentFixture<AddChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddChartComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
