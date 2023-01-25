import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IssueFormService, IssueFormGroup } from './issue-form.service';
import { IIssue } from '../issue.model';
import { IssueService } from '../service/issue.service';
import { IssueType } from 'app/entities/enumerations/issue-type.model';
import { IssueStatus } from 'app/entities/enumerations/issue-status.model';
import { IssuePriority } from 'app/entities/enumerations/issue-priority.model';

@Component({
  selector: 'microapp-issue-update',
  templateUrl: './issue-update.component.html',
})
export class IssueUpdateComponent implements OnInit {
  isSaving = false;
  issue: IIssue | null = null;
  issueTypeValues = Object.keys(IssueType);
  issueStatusValues = Object.keys(IssueStatus);
  issuePriorityValues = Object.keys(IssuePriority);

  editForm: IssueFormGroup = this.issueFormService.createIssueFormGroup();

  constructor(
    protected issueService: IssueService,
    protected issueFormService: IssueFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ issue }) => {
      this.issue = issue;
      if (issue) {
        this.updateForm(issue);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const issue = this.issueFormService.getIssue(this.editForm);
    if (issue.id !== null) {
      this.subscribeToSaveResponse(this.issueService.update(issue));
    } else {
      this.subscribeToSaveResponse(this.issueService.create(issue));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIssue>>): void {
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

  protected updateForm(issue: IIssue): void {
    this.issue = issue;
    this.issueFormService.resetForm(this.editForm, issue);
  }
}
