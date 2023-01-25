import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IssueEmployeeAssignmentComponent } from '../list/issue-employee-assignment.component';
import { IssueEmployeeAssignmentDetailComponent } from '../detail/issue-employee-assignment-detail.component';
import { IssueEmployeeAssignmentUpdateComponent } from '../update/issue-employee-assignment-update.component';
import { IssueEmployeeAssignmentRoutingResolveService } from './issue-employee-assignment-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const issueEmployeeAssignmentRoute: Routes = [
  {
    path: '',
    component: IssueEmployeeAssignmentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IssueEmployeeAssignmentDetailComponent,
    resolve: {
      issueEmployeeAssignment: IssueEmployeeAssignmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IssueEmployeeAssignmentUpdateComponent,
    resolve: {
      issueEmployeeAssignment: IssueEmployeeAssignmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IssueEmployeeAssignmentUpdateComponent,
    resolve: {
      issueEmployeeAssignment: IssueEmployeeAssignmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(issueEmployeeAssignmentRoute)],
  exports: [RouterModule],
})
export class IssueEmployeeAssignmentRoutingModule {}
