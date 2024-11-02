import { TestBed } from '@angular/core/testing';

import { Statistics1Service } from './statistics1.service';

describe('Statistics1Service', () => {
  let service: Statistics1Service;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Statistics1Service);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
