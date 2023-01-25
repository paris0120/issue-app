import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIssueEmployeeAssignment } from '../issue-employee-assignment.model';
import { IssueEmployeeAssignmentService } from '../service/issue-employee-assignment.service';

@Injectable({ providedIn: 'root' })
export class IssueEmployeeAssignmentRoutingResolveService implements Resolve<IIssueEmployeeAssignment | null> {
  constructor(protected service: IssueEmployeeAssignmentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIssueEmployeeAssignment | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((issueEmployeeAssignment: HttpResponse<IIssueEmployeeAssignment>) => {
          if (issueEmployeeAssignment.body) {
            return of(issueEmployeeAssignment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
