import { TestBed } from '@angular/core/testing';

import { FieldactivityfiletypeService } from './fieldactivityfiletype.service';

describe('FieldactivityfiletypeService', () => {
  let service: FieldactivityfiletypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FieldactivityfiletypeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
