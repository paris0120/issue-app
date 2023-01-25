import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIssueTag, NewIssueTag } from '../issue-tag.model';

export type PartialUpdateIssueTag = Partial<IIssueTag> & Pick<IIssueTag, 'id'>;

export type EntityResponseType = HttpResponse<IIssueTag>;
export type EntityArrayResponseType = HttpResponse<IIssueTag[]>;

@Injectable({ providedIn: 'root' })
export class IssueTagService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/issue-tags', 'issue');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(issueTag: NewIssueTag): Observable<EntityResponseType> {
    return this.http.post<IIssueTag>(this.resourceUrl, issueTag, { observe: 'response' });
  }

  update(issueTag: IIssueTag): Observable<EntityResponseType> {
    return this.http.put<IIssueTag>(`${this.resourceUrl}/${this.getIssueTagIdentifier(issueTag)}`, issueTag, { observe: 'response' });
  }

  partialUpdate(issueTag: PartialUpdateIssueTag): Observable<EntityResponseType> {
    return this.http.patch<IIssueTag>(`${this.resourceUrl}/${this.getIssueTagIdentifier(issueTag)}`, issueTag, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIssueTag>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIssueTag[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIssueTagIdentifier(issueTag: Pick<IIssueTag, 'id'>): number {
    return issueTag.id;
  }

  compareIssueTag(o1: Pick<IIssueTag, 'id'> | null, o2: Pick<IIssueTag, 'id'> | null): boolean {
    return o1 && o2 ? this.getIssueTagIdentifier(o1) === this.getIssueTagIdentifier(o2) : o1 === o2;
  }

  addIssueTagToCollectionIfMissing<Type extends Pick<IIssueTag, 'id'>>(
    issueTagCollection: Type[],
    ...issueTagsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const issueTags: Type[] = issueTagsToCheck.filter(isPresent);
    if (issueTags.length > 0) {
      const issueTagCollectionIdentifiers = issueTagCollection.map(issueTagItem => this.getIssueTagIdentifier(issueTagItem)!);
      const issueTagsToAdd = issueTags.filter(issueTagItem => {
        const issueTagIdentifier = this.getIssueTagIdentifier(issueTagItem);
        if (issueTagCollectionIdentifiers.includes(issueTagIdentifier)) {
          return false;
        }
        issueTagCollectionIdentifiers.push(issueTagIdentifier);
        return true;
      });
      return [...issueTagsToAdd, ...issueTagCollection];
    }
    return issueTagCollection;
  }
}
