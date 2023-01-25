import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IssueEmployeeAssignmentFormService, IssueEmployeeAssignmentFormGroup } from './issue-employee-assignment-form.service';
import { IIssueEmployeeAssignment } from '../issue-employee-assignment.model';
import { IssueEmployeeAssignmentService } from '../service/issue-employee-assignment.service';

@Component({
  selector: 'microapp-issue-employee-assignment-update',
  templateUrl: './issue-employee-assignment-update.component.html',
})
export class IssueEmployeeAssignmentUpdateComponent implements OnInit {
  isSaving = false;
  issueEmployeeAssignment: IIssueEmployeeAssignment | null = null;

  editForm: IssueEmployeeAssignmentFormGroup = this.issueEmployeeAssignmentFormService.createIssueEmployeeAssignmentFormGroup();

  constructor(
    protected issueEmployeeAssignmentService: IssueEmployeeAssignmentService,
    protected issueEmployeeAssignmentFormService: IssueEmployeeAssignmentFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ issueEmployeeAssignment }) => {
      this.issueEmployeeAssignment = issueEmployeeAssignment;
      if (issueEmployeeAssignment) {
        this.updateForm(issueEmployeeAssignment);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const issueEmployeeAssignment = this.issueEmployeeAssignmentFormService.getIssueEmployeeAssignment(this.editForm);
    if (issueEmployeeAssignment.id !== null) {
      this.subscribeToSaveResponse(this.issueEmployeeAssignmentService.update(issueEmployeeAssignment));
    } else {
      this.subscribeToSaveResponse(this.issueEmployeeAssignmentService.create(issueEmployeeAssignment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIssueEmployeeAssignment>>): void {
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

  protected updateForm(issueEmployeeAssignment: IIssueEmployeeAssignment): void {
    this.issueEmployeeAssignment = issueEmployeeAssignment;
    this.issueEmployeeAssignmentFormService.resetForm(this.editForm, issueEmployeeAssignment);
  }
}
