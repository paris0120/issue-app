import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IssueTagComponent } from './list/issue-tag.component';
import { IssueTagDetailComponent } from './detail/issue-tag-detail.component';
import { IssueTagUpdateComponent } from './update/issue-tag-update.component';
import { IssueTagDeleteDialogComponent } from './delete/issue-tag-delete-dialog.component';
import { IssueTagRoutingModule } from './route/issue-tag-routing.module';

@NgModule({
  imports: [SharedModule, IssueTagRoutingModule],
  declarations: [IssueTagComponent, IssueTagDetailComponent, IssueTagUpdateComponent, IssueTagDeleteDialogComponent],
})
export class IssueIssueTagModule {}
