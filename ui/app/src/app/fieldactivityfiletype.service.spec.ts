import { TestBed } from '@angular/core/testing';

import { FieldactivityfiletypeService } from './fieldactivityfiletype.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('FieldactivityfiletypeService', () => {
  let service: FieldactivityfiletypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(FieldactivityfiletypeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
