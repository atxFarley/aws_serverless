import { TestBed } from '@angular/core/testing';

import { FieldactivitytypeService } from './fieldactivitytype.service';

describe('FieldactivitytypeService', () => {
  let service: FieldactivitytypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FieldactivitytypeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
