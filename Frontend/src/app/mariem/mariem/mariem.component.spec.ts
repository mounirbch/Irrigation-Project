import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MariemComponent } from './mariem.component';

describe('MariemComponent', () => {
  let component: MariemComponent;
  let fixture: ComponentFixture<MariemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MariemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MariemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
