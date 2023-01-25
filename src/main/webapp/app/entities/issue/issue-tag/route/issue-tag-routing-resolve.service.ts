import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIssueTag } from '../issue-tag.model';
import { IssueTagService } from '../service/issue-tag.service';

@Injectable({ providedIn: 'root' })
export class IssueTagRoutingResolveService implements Resolve<IIssueTag | null> {
  constructor(protected service: IssueTagService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIssueTag | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((issueTag: HttpResponse<IIssueTag>) => {
          if (issueTag.body) {
            return of(issueTag.body);
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
