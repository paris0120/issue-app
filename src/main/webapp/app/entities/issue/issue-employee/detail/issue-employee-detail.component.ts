import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIssueEmployee } from '../issue-employee.model';

@Component({
  selector: 'microapp-issue-employee-detail',
  templateUrl: './issue-employee-detail.component.html',
})
export class IssueEmployeeDetailComponent implements OnInit {
  issueEmployee: IIssueEmployee | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ issueEmployee }) => {
      this.issueEmployee = issueEmployee;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
