import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IIssue, NewIssue } from '../issue.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIssue for edit and NewIssueFormGroupInput for create.
 */
type IssueFormGroupInput = IIssue | PartialWithRequiredKeyOf<NewIssue>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IIssue | NewIssue> = Omit<T, 'created' | 'modified' | 'deleted'> & {
  created?: string | null;
  modified?: string | null;
  deleted?: string | null;
};

type IssueFormRawValue = FormValueOf<IIssue>;

type NewIssueFormRawValue = FormValueOf<NewIssue>;

type IssueFormDefaults = Pick<NewIssue, 'id' | 'created' | 'modified' | 'deleted'>;

type IssueFormGroupContent = {
  id: FormControl<IssueFormRawValue['id'] | NewIssue['id']>;
  username: FormControl<IssueFormRawValue['username']>;
  issueTitle: FormControl<IssueFormRawValue['issueTitle']>;
  issueContent: FormControl<IssueFormRawValue['issueContent']>;
  issueType: FormControl<IssueFormRawValue['issueType']>;
  issueStatus: FormControl<IssueFormRawValue['issueStatus']>;
  issuePriority: FormControl<IssueFormRawValue['issuePriority']>;
  uuid: FormControl<IssueFormRawValue['uuid']>;
  created: FormControl<IssueFormRawValue['created']>;
  modified: FormControl<IssueFormRawValue['modified']>;
  deleted: FormControl<IssueFormRawValue['deleted']>;
};

export type IssueFormGroup = FormGroup<IssueFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IssueFormService {
  createIssueFormGroup(issue: IssueFormGroupInput = { id: null }): IssueFormGroup {
    const issueRawValue = this.convertIssueToIssueRawValue({
      ...this.getFormDefaults(),
      ...issue,
    });
    return new FormGroup<IssueFormGroupContent>({
      id: new FormControl(
        { value: issueRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      username: new FormControl(issueRawValue.username),
      issueTitle: new FormControl(issueRawValue.issueTitle, {
        validators: [Validators.required, Validators.minLength(2)],
      }),
      issueContent: new FormControl(issueRawValue.issueContent, {
        validators: [Validators.required, Validators.minLength(10)],
      }),
      issueType: new FormControl(issueRawValue.issueType, {
        validators: [Validators.required],
      }),
      issueStatus: new FormControl(issueRawValue.issueStatus, {
        validators: [Validators.required],
      }),
      issuePriority: new FormControl(issueRawValue.issuePriority, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(issueRawValue.uuid, {
        validators: [Validators.required],
      }),
      created: new FormControl(issueRawValue.created, {
        validators: [Validators.required],
      }),
      modified: new FormControl(issueRawValue.modified, {
        validators: [Validators.required],
      }),
      deleted: new FormControl(issueRawValue.deleted),
    });
  }

  getIssue(form: IssueFormGroup): IIssue | NewIssue {
    return this.convertIssueRawValueToIssue(form.getRawValue() as IssueFormRawValue | NewIssueFormRawValue);
  }

  resetForm(form: IssueFormGroup, issue: IssueFormGroupInput): void {
    const issueRawValue = this.convertIssueToIssueRawValue({ ...this.getFormDefaults(), ...issue });
    form.reset(
      {
        ...issueRawValue,
        id: { value: issueRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): IssueFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      created: currentTime,
      modified: currentTime,
      deleted: currentTime,
    };
  }

  private convertIssueRawValueToIssue(rawIssue: IssueFormRawValue | NewIssueFormRawValue): IIssue | NewIssue {
    return {
      ...rawIssue,
      created: dayjs(rawIssue.created, DATE_TIME_FORMAT),
      modified: dayjs(rawIssue.modified, DATE_TIME_FORMAT),
      deleted: dayjs(rawIssue.deleted, DATE_TIME_FORMAT),
    };
  }

  private convertIssueToIssueRawValue(
    issue: IIssue | (Partial<NewIssue> & IssueFormDefaults)
  ): IssueFormRawValue | PartialWithRequiredKeyOf<NewIssueFormRawValue> {
    return {
      ...issue,
      created: issue.created ? issue.created.format(DATE_TIME_FORMAT) : undefined,
      modified: issue.modified ? issue.modified.format(DATE_TIME_FORMAT) : undefined,
      deleted: issue.deleted ? issue.deleted.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
