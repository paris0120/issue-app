import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIssue } from '../issue.model';

@Component({
  selector: 'microapp-issue-detail',
  templateUrl: './issue-detail.component.html',
})
export class IssueDetailComponent implements OnInit {
  issue: IIssue | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ issue }) => {
      this.issue = issue;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
