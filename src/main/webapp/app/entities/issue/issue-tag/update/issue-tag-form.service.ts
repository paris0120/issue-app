import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IIssueTag, NewIssueTag } from '../issue-tag.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIssueTag for edit and NewIssueTagFormGroupInput for create.
 */
type IssueTagFormGroupInput = IIssueTag | PartialWithRequiredKeyOf<NewIssueTag>;

type IssueTagFormDefaults = Pick<NewIssueTag, 'id'>;

type IssueTagFormGroupContent = {
  id: FormControl<IIssueTag['id'] | NewIssueTag['id']>;
  issueId: FormControl<IIssueTag['issueId']>;
  issueUuid: FormControl<IIssueTag['issueUuid']>;
  tag: FormControl<IIssueTag['tag']>;
};

export type IssueTagFormGroup = FormGroup<IssueTagFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IssueTagFormService {
  createIssueTagFormGroup(issueTag: IssueTagFormGroupInput = { id: null }): IssueTagFormGroup {
    const issueTagRawValue = {
      ...this.getFormDefaults(),
      ...issueTag,
    };
    return new FormGroup<IssueTagFormGroupContent>({
      id: new FormControl(
        { value: issueTagRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      issueId: new FormControl(issueTagRawValue.issueId, {
        validators: [Validators.required],
      }),
      issueUuid: new FormControl(issueTagRawValue.issueUuid, {
        validators: [Validators.required],
      }),
      tag: new FormControl(issueTagRawValue.tag, {
        validators: [Validators.required],
      }),
    });
  }

  getIssueTag(form: IssueTagFormGroup): IIssueTag | NewIssueTag {
    return form.getRawValue() as IIssueTag | NewIssueTag;
  }

  resetForm(form: IssueTagFormGroup, issueTag: IssueTagFormGroupInput): void {
    const issueTagRawValue = { ...this.getFormDefaults(), ...issueTag };
    form.reset(
      {
        ...issueTagRawValue,
        id: { value: issueTagRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): IssueTagFormDefaults {
    return {
      id: null,
    };
  }
}
