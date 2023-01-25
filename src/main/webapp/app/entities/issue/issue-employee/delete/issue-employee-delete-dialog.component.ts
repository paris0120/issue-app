import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIssueEmployee } from '../issue-employee.model';
import { IssueEmployeeService } from '../service/issue-employee.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './issue-employee-delete-dialog.component.html',
})
export class IssueEmployeeDeleteDialogComponent {
  issueEmployee?: IIssueEmployee;

  constructor(protected issueEmployeeService: IssueEmployeeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.issueEmployeeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
