import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateDatasourceComponent } from './update-datasource.component';

describe('UpdateDatasourceComponent', () => {
  let component: UpdateDatasourceComponent;
  let fixture: ComponentFixture<UpdateDatasourceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateDatasourceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateDatasourceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
