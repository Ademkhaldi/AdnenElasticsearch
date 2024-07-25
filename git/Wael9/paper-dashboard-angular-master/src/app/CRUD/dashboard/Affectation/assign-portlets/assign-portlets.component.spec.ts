import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignPortletsComponent } from './assign-portlets.component';

describe('AssignPortletsComponent', () => {
  let component: AssignPortletsComponent;
  let fixture: ComponentFixture<AssignPortletsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AssignPortletsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AssignPortletsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
