import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIssueTag } from '../issue-tag.model';

@Component({
  selector: 'microapp-issue-tag-detail',
  templateUrl: './issue-tag-detail.component.html',
})
export class IssueTagDetailComponent implements OnInit {
  issueTag: IIssueTag | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ issueTag }) => {
      this.issueTag = issueTag;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
