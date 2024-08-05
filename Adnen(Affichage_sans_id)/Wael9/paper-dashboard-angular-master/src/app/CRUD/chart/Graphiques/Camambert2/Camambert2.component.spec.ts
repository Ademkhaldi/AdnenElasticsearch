import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Camambert2Component } from './Camambert2.component';

describe('ChartDataComponent', () => {
  let component: Camambert2Component;
  let fixture: ComponentFixture<Camambert2Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Camambert2Component ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Camambert2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
