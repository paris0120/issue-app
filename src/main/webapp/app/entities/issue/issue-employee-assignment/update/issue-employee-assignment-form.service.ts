import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IIssueEmployeeAssignment, NewIssueEmployeeAssignment } from '../issue-employee-assignment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIssueEmployeeAssignment for edit and NewIssueEmployeeAssignmentFormGroupInput for create.
 */
type IssueEmployeeAssignmentFormGroupInput = IIssueEmployeeAssignment | PartialWithRequiredKeyOf<NewIssueEmployeeAssignment>;

type IssueEmployeeAssignmentFormDefaults = Pick<NewIssueEmployeeAssignment, 'id'>;

type IssueEmployeeAssignmentFormGroupContent = {
  id: FormControl<IIssueEmployeeAssignment['id'] | NewIssueEmployeeAssignment['id']>;
  issueId: FormControl<IIssueEmployeeAssignment['issueId']>;
  issueUuid: FormControl<IIssueEmployeeAssignment['issueUuid']>;
  username: FormControl<IIssueEmployeeAssignment['username']>;
  issueAssignmentTitle: FormControl<IIssueEmployeeAssignment['issueAssignmentTitle']>;
};

export type IssueEmployeeAssignmentFormGroup = FormGroup<IssueEmployeeAssignmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IssueEmployeeAssignmentFormService {
  createIssueEmployeeAssignmentFormGroup(
    issueEmployeeAssignment: IssueEmployeeAssignmentFormGroupInput = { id: null }
  ): IssueEmployeeAssignmentFormGroup {
    const issueEmployeeAssignmentRawValue = {
      ...this.getFormDefaults(),
      ...issueEmployeeAssignment,
    };
    return new FormGroup<IssueEmployeeAssignmentFormGroupContent>({
      id: new FormControl(
        { value: issueEmployeeAssignmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      issueId: new FormControl(issueEmployeeAssignmentRawValue.issueId, {
        validators: [Validators.required],
      }),
      issueUuid: new FormControl(issueEmployeeAssignmentRawValue.issueUuid, {
        validators: [Validators.required],
      }),
      username: new FormControl(issueEmployeeAssignmentRawValue.username, {
        validators: [Validators.required],
      }),
      issueAssignmentTitle: new FormControl(issueEmployeeAssignmentRawValue.issueAssignmentTitle, {
        validators: [Validators.required],
      }),
    });
  }

  getIssueEmployeeAssignment(form: IssueEmployeeAssignmentFormGroup): IIssueEmployeeAssignment | NewIssueEmployeeAssignment {
    return form.getRawValue() as IIssueEmployeeAssignment | NewIssueEmployeeAssignment;
  }

  resetForm(form: IssueEmployeeAssignmentFormGroup, issueEmployeeAssignment: IssueEmployeeAssignmentFormGroupInput): void {
    const issueEmployeeAssignmentRawValue = { ...this.getFormDefaults(), ...issueEmployeeAssignment };
    form.reset(
      {
        ...issueEmployeeAssignmentRawValue,
        id: { value: issueEmployeeAssignmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): IssueEmployeeAssignmentFormDefaults {
    return {
      id: null,
    };
  }
}
