import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IssueEmployeeComponent } from '../list/issue-employee.component';
import { IssueEmployeeDetailComponent } from '../detail/issue-employee-detail.component';
import { IssueEmployeeUpdateComponent } from '../update/issue-employee-update.component';
import { IssueEmployeeRoutingResolveService } from './issue-employee-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const issueEmployeeRoute: Routes = [
  {
    path: '',
    component: IssueEmployeeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IssueEmployeeDetailComponent,
    resolve: {
      issueEmployee: IssueEmployeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IssueEmployeeUpdateComponent,
    resolve: {
      issueEmployee: IssueEmployeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IssueEmployeeUpdateComponent,
    resolve: {
      issueEmployee: IssueEmployeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(issueEmployeeRoute)],
  exports: [RouterModule],
})
export class IssueEmployeeRoutingModule {}
