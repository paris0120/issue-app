import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../issue-employee-assignment.test-samples';

import { IssueEmployeeAssignmentFormService } from './issue-employee-assignment-form.service';

describe('IssueEmployeeAssignment Form Service', () => {
  let service: IssueEmployeeAssignmentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IssueEmployeeAssignmentFormService);
  });

  describe('Service methods', () => {
    describe('createIssueEmployeeAssignmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIssueEmployeeAssignmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            issueId: expect.any(Object),
            issueUuid: expect.any(Object),
            username: expect.any(Object),
            issueAssignmentTitle: expect.any(Object),
          })
        );
      });

      it('passing IIssueEmployeeAssignment should create a new form with FormGroup', () => {
        const formGroup = service.createIssueEmployeeAssignmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            issueId: expect.any(Object),
            issueUuid: expect.any(Object),
            username: expect.any(Object),
            issueAssignmentTitle: expect.any(Object),
          })
        );
      });
    });

    describe('getIssueEmployeeAssignment', () => {
      it('should return NewIssueEmployeeAssignment for default IssueEmployeeAssignment initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createIssueEmployeeAssignmentFormGroup(sampleWithNewData);

        const issueEmployeeAssignment = service.getIssueEmployeeAssignment(formGroup) as any;

        expect(issueEmployeeAssignment).toMatchObject(sampleWithNewData);
      });

      it('should return NewIssueEmployeeAssignment for empty IssueEmployeeAssignment initial value', () => {
        const formGroup = service.createIssueEmployeeAssignmentFormGroup();

        const issueEmployeeAssignment = service.getIssueEmployeeAssignment(formGroup) as any;

        expect(issueEmployeeAssignment).toMatchObject({});
      });

      it('should return IIssueEmployeeAssignment', () => {
        const formGroup = service.createIssueEmployeeAssignmentFormGroup(sampleWithRequiredData);

        const issueEmployeeAssignment = service.getIssueEmployeeAssignment(formGroup) as any;

        expect(issueEmployeeAssignment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIssueEmployeeAssignment should not enable id FormControl', () => {
        const formGroup = service.createIssueEmployeeAssignmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIssueEmployeeAssignment should disable id FormControl', () => {
        const formGroup = service.createIssueEmployeeAssignmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
