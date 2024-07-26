import { TestBed } from '@angular/core/testing';

import { AreaUseractivityService } from './Areauseractivity.service';

describe('AreaUseractivityService', () => {
  let service: AreaUseractivityService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AreaUseractivityService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
