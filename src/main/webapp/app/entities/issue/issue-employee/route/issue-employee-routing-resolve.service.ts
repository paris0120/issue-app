import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIssueEmployee } from '../issue-employee.model';
import { IssueEmployeeService } from '../service/issue-employee.service';

@Injectable({ providedIn: 'root' })
export class IssueEmployeeRoutingResolveService implements Resolve<IIssueEmployee | null> {
  constructor(protected service: IssueEmployeeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIssueEmployee | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((issueEmployee: HttpResponse<IIssueEmployee>) => {
          if (issueEmployee.body) {
            return of(issueEmployee.body);
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
