import { TestBed } from '@angular/core/testing';

import { OptimizationService } from './optimization.service';

describe('AppService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: OptimizationService = TestBed.get(OptimizationService);
    expect(service).toBeTruthy();
  });
});
