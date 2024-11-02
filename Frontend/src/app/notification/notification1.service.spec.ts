import { TestBed } from '@angular/core/testing';

import { Notification1Service } from './notification1.service';

describe('Notification1Service', () => {
  let service: Notification1Service;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Notification1Service);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
