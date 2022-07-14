import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReReviewComponent } from './re-review.component';

describe('ReReviewComponent', () => {
  let component: ReReviewComponent;
  let fixture: ComponentFixture<ReReviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReReviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
