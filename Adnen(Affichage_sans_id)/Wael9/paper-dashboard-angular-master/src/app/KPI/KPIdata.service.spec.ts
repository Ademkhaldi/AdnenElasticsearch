import { TestBed } from '@angular/core/testing';

import { KPIDataService } from './KPIdata.service';

describe('KPIDataService', () => {
  let service: KPIDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(KPIDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
