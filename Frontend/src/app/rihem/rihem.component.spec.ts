import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RihemComponent } from './rihem.component';

describe('RihemComponent', () => {
  let component: RihemComponent;
  let fixture: ComponentFixture<RihemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RihemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RihemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
