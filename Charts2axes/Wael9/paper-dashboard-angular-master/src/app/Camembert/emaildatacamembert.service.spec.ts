import { TestBed } from '@angular/core/testing';

import { EmailDataCamembertService } from './emaildatacamembert.service';

describe('EmailDataCamembertService', () => {
  let service: EmailDataCamembertService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmailDataCamembertService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
