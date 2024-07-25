import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdatePortletComponent } from './update-portlet.component';

describe('UpdatePortletComponent', () => {
  let component: UpdatePortletComponent;
  let fixture: ComponentFixture<UpdatePortletComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdatePortletComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdatePortletComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
