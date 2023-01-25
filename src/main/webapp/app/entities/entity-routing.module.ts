import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'issue',
        data: { pageTitle: 'issueApp.issueIssue.home.title' },
        loadChildren: () => import('./issue/issue/issue.module').then(m => m.IssueIssueModule),
      },
      {
        path: 'issue-employee',
        data: { pageTitle: 'issueApp.issueIssueEmployee.home.title' },
        loadChildren: () => import('./issue/issue-employee/issue-employee.module').then(m => m.IssueIssueEmployeeModule),
      },
      {
        path: 'issue-employee-assignment',
        data: { pageTitle: 'issueApp.issueIssueEmployeeAssignment.home.title' },
        loadChildren: () =>
          import('./issue/issue-employee-assignment/issue-employee-assignment.module').then(m => m.IssueIssueEmployeeAssignmentModule),
      },
      {
        path: 'issue-tag',
        data: { pageTitle: 'issueApp.issueIssueTag.home.title' },
        loadChildren: () => import('./issue/issue-tag/issue-tag.module').then(m => m.IssueIssueTagModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
