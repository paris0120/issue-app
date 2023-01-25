import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IssueEmployeeFormService, IssueEmployeeFormGroup } from './issue-employee-form.service';
import { IIssueEmployee } from '../issue-employee.model';
import { IssueEmployeeService } from '../service/issue-employee.service';

@Component({
  selector: 'microapp-issue-employee-update',
  templateUrl: './issue-employee-update.component.html',
})
export class IssueEmployeeUpdateComponent implements OnInit {
  isSaving = false;
  issueEmployee: IIssueEmployee | null = null;

  editForm: IssueEmployeeFormGroup = this.issueEmployeeFormService.createIssueEmployeeFormGroup();

  constructor(
    protected issueEmployeeService: IssueEmployeeService,
    protected issueEmployeeFormService: IssueEmployeeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ issueEmployee }) => {
      this.issueEmployee = issueEmployee;
      if (issueEmployee) {
        this.updateForm(issueEmployee);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const issueEmployee = this.issueEmployeeFormService.getIssueEmployee(this.editForm);
    if (issueEmployee.id !== null) {
      this.subscribeToSaveResponse(this.issueEmployeeService.update(issueEmployee));
    } else {
      this.subscribeToSaveResponse(this.issueEmployeeService.create(issueEmployee));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIssueEmployee>>): void {
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

  protected updateForm(issueEmployee: IIssueEmployee): void {
    this.issueEmployee = issueEmployee;
    this.issueEmployeeFormService.resetForm(this.editForm, issueEmployee);
  }
}
