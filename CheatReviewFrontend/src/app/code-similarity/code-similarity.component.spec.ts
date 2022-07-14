import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CodeSimilarityComponent } from './code-similarity.component';

describe('CodeSimilarityComponent', () => {
  let component: CodeSimilarityComponent;
  let fixture: ComponentFixture<CodeSimilarityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CodeSimilarityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CodeSimilarityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
