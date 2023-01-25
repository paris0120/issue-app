import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../issue-tag.test-samples';

import { IssueTagFormService } from './issue-tag-form.service';

describe('IssueTag Form Service', () => {
  let service: IssueTagFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IssueTagFormService);
  });

  describe('Service methods', () => {
    describe('createIssueTagFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIssueTagFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            issueId: expect.any(Object),
            issueUuid: expect.any(Object),
            tag: expect.any(Object),
          })
        );
      });

      it('passing IIssueTag should create a new form with FormGroup', () => {
        const formGroup = service.createIssueTagFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            issueId: expect.any(Object),
            issueUuid: expect.any(Object),
            tag: expect.any(Object),
          })
        );
      });
    });

    describe('getIssueTag', () => {
      it('should return NewIssueTag for default IssueTag initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createIssueTagFormGroup(sampleWithNewData);

        const issueTag = service.getIssueTag(formGroup) as any;

        expect(issueTag).toMatchObject(sampleWithNewData);
      });

      it('should return NewIssueTag for empty IssueTag initial value', () => {
        const formGroup = service.createIssueTagFormGroup();

        const issueTag = service.getIssueTag(formGroup) as any;

        expect(issueTag).toMatchObject({});
      });

      it('should return IIssueTag', () => {
        const formGroup = service.createIssueTagFormGroup(sampleWithRequiredData);

        const issueTag = service.getIssueTag(formGroup) as any;

        expect(issueTag).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIssueTag should not enable id FormControl', () => {
        const formGroup = service.createIssueTagFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIssueTag should disable id FormControl', () => {
        const formGroup = service.createIssueTagFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
