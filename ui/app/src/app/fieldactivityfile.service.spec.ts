import { TestBed } from '@angular/core/testing';

import { FieldactivityfileService } from './fieldactivityfile.service';

describe('FieldactivityfileService', () => {
  let service: FieldactivityfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FieldactivityfileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
