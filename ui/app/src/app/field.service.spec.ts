import {TestBed} from '@angular/core/testing';

import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FieldService} from './field.service';


describe('FieldService', () => {
  let service: FieldService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(FieldService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
