import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIssueEmployeeAssignment } from '../issue-employee-assignment.model';

@Component({
  selector: 'microapp-issue-employee-assignment-detail',
  templateUrl: './issue-employee-assignment-detail.component.html',
})
export class IssueEmployeeAssignmentDetailComponent implements OnInit {
  issueEmployeeAssignment: IIssueEmployeeAssignment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ issueEmployeeAssignment }) => {
      this.issueEmployeeAssignment = issueEmployeeAssignment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
