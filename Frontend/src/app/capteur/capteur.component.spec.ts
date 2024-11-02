import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CapteurComponent } from './capteur.component';

describe('CapteurComponent', () => {
  let component: CapteurComponent;
  let fixture: ComponentFixture<CapteurComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CapteurComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CapteurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
