import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIssue, NewIssue } from '../issue.model';

export type PartialUpdateIssue = Partial<IIssue> & Pick<IIssue, 'id'>;

type RestOf<T extends IIssue | NewIssue> = Omit<T, 'created' | 'modified' | 'deleted'> & {
  created?: string | null;
  modified?: string | null;
  deleted?: string | null;
};

export type RestIssue = RestOf<IIssue>;

export type NewRestIssue = RestOf<NewIssue>;

export type PartialUpdateRestIssue = RestOf<PartialUpdateIssue>;

export type EntityResponseType = HttpResponse<IIssue>;
export type EntityArrayResponseType = HttpResponse<IIssue[]>;

@Injectable({ providedIn: 'root' })
export class IssueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/issues', 'issue');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(issue: NewIssue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(issue);
    return this.http.post<RestIssue>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(issue: IIssue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(issue);
    return this.http
      .put<RestIssue>(`${this.resourceUrl}/${this.getIssueIdentifier(issue)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(issue: PartialUpdateIssue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(issue);
    return this.http
      .patch<RestIssue>(`${this.resourceUrl}/${this.getIssueIdentifier(issue)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestIssue>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestIssue[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIssueIdentifier(issue: Pick<IIssue, 'id'>): number {
    return issue.id;
  }

  compareIssue(o1: Pick<IIssue, 'id'> | null, o2: Pick<IIssue, 'id'> | null): boolean {
    return o1 && o2 ? this.getIssueIdentifier(o1) === this.getIssueIdentifier(o2) : o1 === o2;
  }

  addIssueToCollectionIfMissing<Type extends Pick<IIssue, 'id'>>(
    issueCollection: Type[],
    ...issuesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const issues: Type[] = issuesToCheck.filter(isPresent);
    if (issues.length > 0) {
      const issueCollectionIdentifiers = issueCollection.map(issueItem => this.getIssueIdentifier(issueItem)!);
      const issuesToAdd = issues.filter(issueItem => {
        const issueIdentifier = this.getIssueIdentifier(issueItem);
        if (issueCollectionIdentifiers.includes(issueIdentifier)) {
          return false;
        }
        issueCollectionIdentifiers.push(issueIdentifier);
        return true;
      });
      return [...issuesToAdd, ...issueCollection];
    }
    return issueCollection;
  }

  protected convertDateFromClient<T extends IIssue | NewIssue | PartialUpdateIssue>(issue: T): RestOf<T> {
    return {
      ...issue,
      created: issue.created?.toJSON() ?? null,
      modified: issue.modified?.toJSON() ?? null,
      deleted: issue.deleted?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restIssue: RestIssue): IIssue {
    return {
      ...restIssue,
      created: restIssue.created ? dayjs(restIssue.created) : undefined,
      modified: restIssue.modified ? dayjs(restIssue.modified) : undefined,
      deleted: restIssue.deleted ? dayjs(restIssue.deleted) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestIssue>): HttpResponse<IIssue> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestIssue[]>): HttpResponse<IIssue[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
