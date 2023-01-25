import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIssueEmployeeAssignment } from '../issue-employee-assignment.model';
import { IssueEmployeeAssignmentService } from '../service/issue-employee-assignment.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './issue-employee-assignment-delete-dialog.component.html',
})
export class IssueEmployeeAssignmentDeleteDialogComponent {
  issueEmployeeAssignment?: IIssueEmployeeAssignment;

  constructor(protected issueEmployeeAssignmentService: IssueEmployeeAssignmentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.issueEmployeeAssignmentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
