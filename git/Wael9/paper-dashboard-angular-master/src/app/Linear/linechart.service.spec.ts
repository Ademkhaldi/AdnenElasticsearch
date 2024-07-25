import { TestBed } from '@angular/core/testing';

import { LineChartService } from './linechart.service';


describe('LinechartService', () => {
  let service: LineChartService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LineChartService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
