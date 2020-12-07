import { TestBed } from '@angular/core/testing';

import { FielduserService } from './fielduser.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('FielduserService', () => {
  let service: FielduserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(FielduserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
