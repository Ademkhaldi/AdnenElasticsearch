import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ElasticComponent } from './elastic.component';

describe('DetailsTitleComponent', () => {
  let component: ElasticComponent;
  let fixture: ComponentFixture<ElasticComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ElasticComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ElasticComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
