import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailsTitleComponent } from './details-title.component';

describe('DetailsTitleComponent', () => {
  let component: DetailsTitleComponent;
  let fixture: ComponentFixture<DetailsTitleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailsTitleComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailsTitleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
