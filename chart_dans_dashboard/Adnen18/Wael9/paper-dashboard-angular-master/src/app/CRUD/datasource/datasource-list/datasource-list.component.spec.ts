import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DatasourceListComponent } from './datasource-list.component';

describe('DatasourceListComponent', () => {
  let component: DatasourceListComponent;
  let fixture: ComponentFixture<DatasourceListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DatasourceListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DatasourceListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
