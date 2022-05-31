import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MlpconfigComponent } from './mlpconfig.component';

describe('MlpconfigComponent', () => {
  let component: MlpconfigComponent;
  let fixture: ComponentFixture<MlpconfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MlpconfigComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MlpconfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
