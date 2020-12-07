import { TestBed } from '@angular/core/testing';

import { FieldattributeService } from './fieldattribute.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('FieldattributeService', () => {
  let service: FieldattributeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(FieldattributeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
