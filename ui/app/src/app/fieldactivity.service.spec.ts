import { TestBed } from '@angular/core/testing';

import { FieldactivityService } from './fieldactivity.service';

describe('FieldactivityService', () => {
  let service: FieldactivityService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FieldactivityService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
