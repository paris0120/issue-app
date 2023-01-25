import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIssue } from '../issue.model';
import { IssueService } from '../service/issue.service';

@Injectable({ providedIn: 'root' })
export class IssueRoutingResolveService implements Resolve<IIssue | null> {
  constructor(protected service: IssueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIssue | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((issue: HttpResponse<IIssue>) => {
          if (issue.body) {
            return of(issue.body);
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
