import { TestBed } from '@angular/core/testing';

import { FieldactivitytypeService } from './fieldactivitytype.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('FieldactivitytypeService', () => {
  let service: FieldactivitytypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(FieldactivitytypeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
