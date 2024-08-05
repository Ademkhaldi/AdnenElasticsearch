import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPortletComponent } from './add-portlet.component';

describe('AddPortletComponent', () => {
  let component: AddPortletComponent;
  let fixture: ComponentFixture<AddPortletComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddPortletComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddPortletComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
