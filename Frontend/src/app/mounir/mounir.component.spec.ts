import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MounirComponent } from './mounir.component';

describe('MounirComponent', () => {
  let component: MounirComponent;
  let fixture: ComponentFixture<MounirComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MounirComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MounirComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
