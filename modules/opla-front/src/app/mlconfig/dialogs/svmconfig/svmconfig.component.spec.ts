import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SvmconfigComponent } from './svmconfig.component';

describe('SvmconfigComponent', () => {
  let component: SvmconfigComponent;
  let fixture: ComponentFixture<SvmconfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SvmconfigComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SvmconfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
