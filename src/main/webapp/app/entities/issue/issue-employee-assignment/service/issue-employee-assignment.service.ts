import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIssueEmployeeAssignment, NewIssueEmployeeAssignment } from '../issue-employee-assignment.model';

export type PartialUpdateIssueEmployeeAssignment = Partial<IIssueEmployeeAssignment> & Pick<IIssueEmployeeAssignment, 'id'>;

export type EntityResponseType = HttpResponse<IIssueEmployeeAssignment>;
export type EntityArrayResponseType = HttpResponse<IIssueEmployeeAssignment[]>;

@Injectable({ providedIn: 'root' })
export class IssueEmployeeAssignmentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/issue-employee-assignments', 'issue');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(issueEmployeeAssignment: NewIssueEmployeeAssignment): Observable<EntityResponseType> {
    return this.http.post<IIssueEmployeeAssignment>(this.resourceUrl, issueEmployeeAssignment, { observe: 'response' });
  }

  update(issueEmployeeAssignment: IIssueEmployeeAssignment): Observable<EntityResponseType> {
    return this.http.put<IIssueEmployeeAssignment>(
      `${this.resourceUrl}/${this.getIssueEmployeeAssignmentIdentifier(issueEmployeeAssignment)}`,
      issueEmployeeAssignment,
      { observe: 'response' }
    );
  }

  partialUpdate(issueEmployeeAssignment: PartialUpdateIssueEmployeeAssignment): Observable<EntityResponseType> {
    return this.http.patch<IIssueEmployeeAssignment>(
      `${this.resourceUrl}/${this.getIssueEmployeeAssignmentIdentifier(issueEmployeeAssignment)}`,
      issueEmployeeAssignment,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIssueEmployeeAssignment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIssueEmployeeAssignment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIssueEmployeeAssignmentIdentifier(issueEmployeeAssignment: Pick<IIssueEmployeeAssignment, 'id'>): number {
    return issueEmployeeAssignment.id;
  }

  compareIssueEmployeeAssignment(
    o1: Pick<IIssueEmployeeAssignment, 'id'> | null,
    o2: Pick<IIssueEmployeeAssignment, 'id'> | null
  ): boolean {
    return o1 && o2 ? this.getIssueEmployeeAssignmentIdentifier(o1) === this.getIssueEmployeeAssignmentIdentifier(o2) : o1 === o2;
  }

  addIssueEmployeeAssignmentToCollectionIfMissing<Type extends Pick<IIssueEmployeeAssignment, 'id'>>(
    issueEmployeeAssignmentCollection: Type[],
    ...issueEmployeeAssignmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const issueEmployeeAssignments: Type[] = issueEmployeeAssignmentsToCheck.filter(isPresent);
    if (issueEmployeeAssignments.length > 0) {
      const issueEmployeeAssignmentCollectionIdentifiers = issueEmployeeAssignmentCollection.map(
        issueEmployeeAssignmentItem => this.getIssueEmployeeAssignmentIdentifier(issueEmployeeAssignmentItem)!
      );
      const issueEmployeeAssignmentsToAdd = issueEmployeeAssignments.filter(issueEmployeeAssignmentItem => {
        const issueEmployeeAssignmentIdentifier = this.getIssueEmployeeAssignmentIdentifier(issueEmployeeAssignmentItem);
        if (issueEmployeeAssignmentCollectionIdentifiers.includes(issueEmployeeAssignmentIdentifier)) {
          return false;
        }
        issueEmployeeAssignmentCollectionIdentifiers.push(issueEmployeeAssignmentIdentifier);
        return true;
      });
      return [...issueEmployeeAssignmentsToAdd, ...issueEmployeeAssignmentCollection];
    }
    return issueEmployeeAssignmentCollection;
  }
}
