import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QContentDlgComponent } from './qcontent-dlg.component';

describe('QContentDlgComponent', () => {
  let component: QContentDlgComponent;
  let fixture: ComponentFixture<QContentDlgComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QContentDlgComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QContentDlgComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
