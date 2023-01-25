import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IssueEmployeeAssignmentFormService } from './issue-employee-assignment-form.service';
import { IssueEmployeeAssignmentService } from '../service/issue-employee-assignment.service';
import { IIssueEmployeeAssignment } from '../issue-employee-assignment.model';

import { IssueEmployeeAssignmentUpdateComponent } from './issue-employee-assignment-update.component';

describe('IssueEmployeeAssignment Management Update Component', () => {
  let comp: IssueEmployeeAssignmentUpdateComponent;
  let fixture: ComponentFixture<IssueEmployeeAssignmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let issueEmployeeAssignmentFormService: IssueEmployeeAssignmentFormService;
  let issueEmployeeAssignmentService: IssueEmployeeAssignmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IssueEmployeeAssignmentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(IssueEmployeeAssignmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IssueEmployeeAssignmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    issueEmployeeAssignmentFormService = TestBed.inject(IssueEmployeeAssignmentFormService);
    issueEmployeeAssignmentService = TestBed.inject(IssueEmployeeAssignmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const issueEmployeeAssignment: IIssueEmployeeAssignment = { id: 456 };

      activatedRoute.data = of({ issueEmployeeAssignment });
      comp.ngOnInit();

      expect(comp.issueEmployeeAssignment).toEqual(issueEmployeeAssignment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIssueEmployeeAssignment>>();
      const issueEmployeeAssignment = { id: 123 };
      jest.spyOn(issueEmployeeAssignmentFormService, 'getIssueEmployeeAssignment').mockReturnValue(issueEmployeeAssignment);
      jest.spyOn(issueEmployeeAssignmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ issueEmployeeAssignment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: issueEmployeeAssignment }));
      saveSubject.complete();

      // THEN
      expect(issueEmployeeAssignmentFormService.getIssueEmployeeAssignment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(issueEmployeeAssignmentService.update).toHaveBeenCalledWith(expect.objectContaining(issueEmployeeAssignment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIssueEmployeeAssignment>>();
      const issueEmployeeAssignment = { id: 123 };
      jest.spyOn(issueEmployeeAssignmentFormService, 'getIssueEmployeeAssignment').mockReturnValue({ id: null });
      jest.spyOn(issueEmployeeAssignmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ issueEmployeeAssignment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: issueEmployeeAssignment }));
      saveSubject.complete();

      // THEN
      expect(issueEmployeeAssignmentFormService.getIssueEmployeeAssignment).toHaveBeenCalled();
      expect(issueEmployeeAssignmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIssueEmployeeAssignment>>();
      const issueEmployeeAssignment = { id: 123 };
      jest.spyOn(issueEmployeeAssignmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ issueEmployeeAssignment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(issueEmployeeAssignmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
