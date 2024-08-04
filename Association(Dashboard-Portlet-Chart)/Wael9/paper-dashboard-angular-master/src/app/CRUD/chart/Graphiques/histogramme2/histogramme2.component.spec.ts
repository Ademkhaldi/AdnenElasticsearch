import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Histogramme2Component } from './histogramme2.component';

describe('Histogramme2Component', () => {
  let component: Histogramme2Component;
  let fixture: ComponentFixture<Histogramme2Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Histogramme2Component ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Histogramme2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
