import { TestBed } from '@angular/core/testing';

import { PortletService } from './portlet.service';

describe('PortletService', () => {
  let service: PortletService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PortletService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
