import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IssueTagFormService, IssueTagFormGroup } from './issue-tag-form.service';
import { IIssueTag } from '../issue-tag.model';
import { IssueTagService } from '../service/issue-tag.service';

@Component({
  selector: 'microapp-issue-tag-update',
  templateUrl: './issue-tag-update.component.html',
})
export class IssueTagUpdateComponent implements OnInit {
  isSaving = false;
  issueTag: IIssueTag | null = null;

  editForm: IssueTagFormGroup = this.issueTagFormService.createIssueTagFormGroup();

  constructor(
    protected issueTagService: IssueTagService,
    protected issueTagFormService: IssueTagFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ issueTag }) => {
      this.issueTag = issueTag;
      if (issueTag) {
        this.updateForm(issueTag);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const issueTag = this.issueTagFormService.getIssueTag(this.editForm);
    if (issueTag.id !== null) {
      this.subscribeToSaveResponse(this.issueTagService.update(issueTag));
    } else {
      this.subscribeToSaveResponse(this.issueTagService.create(issueTag));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIssueTag>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(issueTag: IIssueTag): void {
    this.issueTag = issueTag;
    this.issueTagFormService.resetForm(this.editForm, issueTag);
  }
}
