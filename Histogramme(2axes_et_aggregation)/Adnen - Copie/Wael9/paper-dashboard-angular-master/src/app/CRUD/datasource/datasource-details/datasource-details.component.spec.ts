import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DatasourceDetailsComponent } from './datasource-details.component';

describe('DatasourceDetailsComponent', () => {
  let component: DatasourceDetailsComponent;
  let fixture: ComponentFixture<DatasourceDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DatasourceDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DatasourceDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
