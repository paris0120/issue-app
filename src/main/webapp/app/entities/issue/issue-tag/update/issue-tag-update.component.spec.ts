import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IssueTagFormService } from './issue-tag-form.service';
import { IssueTagService } from '../service/issue-tag.service';
import { IIssueTag } from '../issue-tag.model';

import { IssueTagUpdateComponent } from './issue-tag-update.component';

describe('IssueTag Management Update Component', () => {
  let comp: IssueTagUpdateComponent;
  let fixture: ComponentFixture<IssueTagUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let issueTagFormService: IssueTagFormService;
  let issueTagService: IssueTagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IssueTagUpdateComponent],
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
      .overrideTemplate(IssueTagUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IssueTagUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    issueTagFormService = TestBed.inject(IssueTagFormService);
    issueTagService = TestBed.inject(IssueTagService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const issueTag: IIssueTag = { id: 456 };

      activatedRoute.data = of({ issueTag });
      comp.ngOnInit();

      expect(comp.issueTag).toEqual(issueTag);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIssueTag>>();
      const issueTag = { id: 123 };
      jest.spyOn(issueTagFormService, 'getIssueTag').mockReturnValue(issueTag);
      jest.spyOn(issueTagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ issueTag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: issueTag }));
      saveSubject.complete();

      // THEN
      expect(issueTagFormService.getIssueTag).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(issueTagService.update).toHaveBeenCalledWith(expect.objectContaining(issueTag));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIssueTag>>();
      const issueTag = { id: 123 };
      jest.spyOn(issueTagFormService, 'getIssueTag').mockReturnValue({ id: null });
      jest.spyOn(issueTagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ issueTag: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: issueTag }));
      saveSubject.complete();

      // THEN
      expect(issueTagFormService.getIssueTag).toHaveBeenCalled();
      expect(issueTagService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIssueTag>>();
      const issueTag = { id: 123 };
      jest.spyOn(issueTagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ issueTag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(issueTagService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
