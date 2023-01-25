import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IssueEmployeeAssignmentDetailComponent } from './issue-employee-assignment-detail.component';

describe('IssueEmployeeAssignment Management Detail Component', () => {
  let comp: IssueEmployeeAssignmentDetailComponent;
  let fixture: ComponentFixture<IssueEmployeeAssignmentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IssueEmployeeAssignmentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ issueEmployeeAssignment: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IssueEmployeeAssignmentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IssueEmployeeAssignmentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load issueEmployeeAssignment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.issueEmployeeAssignment).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
