import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../issue-employee.test-samples';

import { IssueEmployeeFormService } from './issue-employee-form.service';

describe('IssueEmployee Form Service', () => {
  let service: IssueEmployeeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IssueEmployeeFormService);
  });

  describe('Service methods', () => {
    describe('createIssueEmployeeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIssueEmployeeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            username: expect.any(Object),
            issueAssignmentTitle: expect.any(Object),
          })
        );
      });

      it('passing IIssueEmployee should create a new form with FormGroup', () => {
        const formGroup = service.createIssueEmployeeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            username: expect.any(Object),
            issueAssignmentTitle: expect.any(Object),
          })
        );
      });
    });

    describe('getIssueEmployee', () => {
      it('should return NewIssueEmployee for default IssueEmployee initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createIssueEmployeeFormGroup(sampleWithNewData);

        const issueEmployee = service.getIssueEmployee(formGroup) as any;

        expect(issueEmployee).toMatchObject(sampleWithNewData);
      });

      it('should return NewIssueEmployee for empty IssueEmployee initial value', () => {
        const formGroup = service.createIssueEmployeeFormGroup();

        const issueEmployee = service.getIssueEmployee(formGroup) as any;

        expect(issueEmployee).toMatchObject({});
      });

      it('should return IIssueEmployee', () => {
        const formGroup = service.createIssueEmployeeFormGroup(sampleWithRequiredData);

        const issueEmployee = service.getIssueEmployee(formGroup) as any;

        expect(issueEmployee).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIssueEmployee should not enable id FormControl', () => {
        const formGroup = service.createIssueEmployeeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIssueEmployee should disable id FormControl', () => {
        const formGroup = service.createIssueEmployeeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
