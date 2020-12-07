import { TestBed } from '@angular/core/testing';

import { FileuploadService } from './fileupload.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('FileuploadService', () => {
  let service: FileuploadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(FileuploadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
