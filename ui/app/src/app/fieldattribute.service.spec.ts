import { TestBed } from '@angular/core/testing';

import { FieldattributeService } from './fieldattribute.service';

describe('FieldattributeService', () => {
  let service: FieldattributeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FieldattributeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
