import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManualPartComponent } from './manual-part.component';

describe('ManualPartComponent', () => {
  let component: ManualPartComponent;
  let fixture: ComponentFixture<ManualPartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManualPartComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManualPartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
