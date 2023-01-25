import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IssueTagComponent } from '../list/issue-tag.component';
import { IssueTagDetailComponent } from '../detail/issue-tag-detail.component';
import { IssueTagUpdateComponent } from '../update/issue-tag-update.component';
import { IssueTagRoutingResolveService } from './issue-tag-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const issueTagRoute: Routes = [
  {
    path: '',
    component: IssueTagComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IssueTagDetailComponent,
    resolve: {
      issueTag: IssueTagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IssueTagUpdateComponent,
    resolve: {
      issueTag: IssueTagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IssueTagUpdateComponent,
    resolve: {
      issueTag: IssueTagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(issueTagRoute)],
  exports: [RouterModule],
})
export class IssueTagRoutingModule {}
