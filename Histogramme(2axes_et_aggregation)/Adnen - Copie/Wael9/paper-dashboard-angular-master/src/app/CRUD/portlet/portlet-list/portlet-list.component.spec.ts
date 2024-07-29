import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PortletListComponent } from './portlet-list.component';

describe('PortletListComponent', () => {
  let component: PortletListComponent;
  let fixture: ComponentFixture<PortletListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PortletListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PortletListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
