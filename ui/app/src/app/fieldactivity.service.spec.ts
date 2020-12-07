import { TestBed } from '@angular/core/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';

import { FieldactivityService } from './fieldactivity.service';

describe('FieldactivityService', () => {
  let service: FieldactivityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(FieldactivityService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
