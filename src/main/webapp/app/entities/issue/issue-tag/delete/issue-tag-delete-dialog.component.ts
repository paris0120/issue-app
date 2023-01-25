import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIssueTag } from '../issue-tag.model';
import { IssueTagService } from '../service/issue-tag.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './issue-tag-delete-dialog.component.html',
})
export class IssueTagDeleteDialogComponent {
  issueTag?: IIssueTag;

  constructor(protected issueTagService: IssueTagService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.issueTagService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
