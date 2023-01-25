import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IssueEmployeeDetailComponent } from './issue-employee-detail.component';

describe('IssueEmployee Management Detail Component', () => {
  let comp: IssueEmployeeDetailComponent;
  let fixture: ComponentFixture<IssueEmployeeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IssueEmployeeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ issueEmployee: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IssueEmployeeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IssueEmployeeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load issueEmployee on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.issueEmployee).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
