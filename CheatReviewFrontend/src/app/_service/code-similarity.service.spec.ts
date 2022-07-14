import { TestBed } from '@angular/core/testing';

import { CodeSimilarityService } from './code-similarity.service';

describe('CodeSimilarityService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CodeSimilarityService = TestBed.get(CodeSimilarityService);
    expect(service).toBeTruthy();
  });
});
