import { TestBed } from '@angular/core/testing';

import { FieldactivityfileService } from './fieldactivityfile.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('FieldactivityfileService', () => {
  let service: FieldactivityfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(FieldactivityfileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
