import { TestBed } from '@angular/core/testing';

import { FielduserService } from './fielduser.service';

describe('FielduserService', () => {
  let service: FielduserService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FielduserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
