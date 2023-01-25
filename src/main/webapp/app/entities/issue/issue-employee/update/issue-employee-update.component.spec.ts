import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IssueEmployeeFormService } from './issue-employee-form.service';
import { IssueEmployeeService } from '../service/issue-employee.service';
import { IIssueEmployee } from '../issue-employee.model';

import { IssueEmployeeUpdateComponent } from './issue-employee-update.component';

describe('IssueEmployee Management Update Component', () => {
  let comp: IssueEmployeeUpdateComponent;
  let fixture: ComponentFixture<IssueEmployeeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let issueEmployeeFormService: IssueEmployeeFormService;
  let issueEmployeeService: IssueEmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IssueEmployeeUpdateComponent],
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
      .overrideTemplate(IssueEmployeeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IssueEmployeeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    issueEmployeeFormService = TestBed.inject(IssueEmployeeFormService);
    issueEmployeeService = TestBed.inject(IssueEmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const issueEmployee: IIssueEmployee = { id: 456 };

      activatedRoute.data = of({ issueEmployee });
      comp.ngOnInit();

      expect(comp.issueEmployee).toEqual(issueEmployee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIssueEmployee>>();
      const issueEmployee = { id: 123 };
      jest.spyOn(issueEmployeeFormService, 'getIssueEmployee').mockReturnValue(issueEmployee);
      jest.spyOn(issueEmployeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ issueEmployee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: issueEmployee }));
      saveSubject.complete();

      // THEN
      expect(issueEmployeeFormService.getIssueEmployee).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(issueEmployeeService.update).toHaveBeenCalledWith(expect.objectContaining(issueEmployee));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIssueEmployee>>();
      const issueEmployee = { id: 123 };
      jest.spyOn(issueEmployeeFormService, 'getIssueEmployee').mockReturnValue({ id: null });
      jest.spyOn(issueEmployeeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ issueEmployee: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: issueEmployee }));
      saveSubject.complete();

      // THEN
      expect(issueEmployeeFormService.getIssueEmployee).toHaveBeenCalled();
      expect(issueEmployeeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIssueEmployee>>();
      const issueEmployee = { id: 123 };
      jest.spyOn(issueEmployeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ issueEmployee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(issueEmployeeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
