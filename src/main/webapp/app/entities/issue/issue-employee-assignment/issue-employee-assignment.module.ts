import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IssueEmployeeAssignmentComponent } from './list/issue-employee-assignment.component';
import { IssueEmployeeAssignmentDetailComponent } from './detail/issue-employee-assignment-detail.component';
import { IssueEmployeeAssignmentUpdateComponent } from './update/issue-employee-assignment-update.component';
import { IssueEmployeeAssignmentDeleteDialogComponent } from './delete/issue-employee-assignment-delete-dialog.component';
import { IssueEmployeeAssignmentRoutingModule } from './route/issue-employee-assignment-routing.module';

@NgModule({
  imports: [SharedModule, IssueEmployeeAssignmentRoutingModule],
  declarations: [
    IssueEmployeeAssignmentComponent,
    IssueEmployeeAssignmentDetailComponent,
    IssueEmployeeAssignmentUpdateComponent,
    IssueEmployeeAssignmentDeleteDialogComponent,
  ],
})
export class IssueIssueEmployeeAssignmentModule {}
