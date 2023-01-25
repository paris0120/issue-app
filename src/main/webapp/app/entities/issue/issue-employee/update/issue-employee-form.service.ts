import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IIssueEmployee, NewIssueEmployee } from '../issue-employee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIssueEmployee for edit and NewIssueEmployeeFormGroupInput for create.
 */
type IssueEmployeeFormGroupInput = IIssueEmployee | PartialWithRequiredKeyOf<NewIssueEmployee>;

type IssueEmployeeFormDefaults = Pick<NewIssueEmployee, 'id'>;

type IssueEmployeeFormGroupContent = {
  id: FormControl<IIssueEmployee['id'] | NewIssueEmployee['id']>;
  username: FormControl<IIssueEmployee['username']>;
  issueAssignmentTitle: FormControl<IIssueEmployee['issueAssignmentTitle']>;
};

export type IssueEmployeeFormGroup = FormGroup<IssueEmployeeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IssueEmployeeFormService {
  createIssueEmployeeFormGroup(issueEmployee: IssueEmployeeFormGroupInput = { id: null }): IssueEmployeeFormGroup {
    const issueEmployeeRawValue = {
      ...this.getFormDefaults(),
      ...issueEmployee,
    };
    return new FormGroup<IssueEmployeeFormGroupContent>({
      id: new FormControl(
        { value: issueEmployeeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      username: new FormControl(issueEmployeeRawValue.username, {
        validators: [Validators.required],
      }),
      issueAssignmentTitle: new FormControl(issueEmployeeRawValue.issueAssignmentTitle, {
        validators: [Validators.required],
      }),
    });
  }

  getIssueEmployee(form: IssueEmployeeFormGroup): IIssueEmployee | NewIssueEmployee {
    return form.getRawValue() as IIssueEmployee | NewIssueEmployee;
  }

  resetForm(form: IssueEmployeeFormGroup, issueEmployee: IssueEmployeeFormGroupInput): void {
    const issueEmployeeRawValue = { ...this.getFormDefaults(), ...issueEmployee };
    form.reset(
      {
        ...issueEmployeeRawValue,
        id: { value: issueEmployeeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): IssueEmployeeFormDefaults {
    return {
      id: null,
    };
  }
}
