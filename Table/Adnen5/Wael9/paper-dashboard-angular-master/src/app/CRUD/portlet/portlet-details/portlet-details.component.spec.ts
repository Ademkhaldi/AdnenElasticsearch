import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PortletDetailsComponent } from './portlet-details.component';

describe('PortletDetailsComponent', () => {
  let component: PortletDetailsComponent;
  let fixture: ComponentFixture<PortletDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PortletDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PortletDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
