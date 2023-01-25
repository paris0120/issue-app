import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IssueEmployeeComponent } from './list/issue-employee.component';
import { IssueEmployeeDetailComponent } from './detail/issue-employee-detail.component';
import { IssueEmployeeUpdateComponent } from './update/issue-employee-update.component';
import { IssueEmployeeDeleteDialogComponent } from './delete/issue-employee-delete-dialog.component';
import { IssueEmployeeRoutingModule } from './route/issue-employee-routing.module';

@NgModule({
  imports: [SharedModule, IssueEmployeeRoutingModule],
  declarations: [IssueEmployeeComponent, IssueEmployeeDetailComponent, IssueEmployeeUpdateComponent, IssueEmployeeDeleteDialogComponent],
})
export class IssueIssueEmployeeModule {}
