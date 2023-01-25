import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIssueEmployee, NewIssueEmployee } from '../issue-employee.model';

export type PartialUpdateIssueEmployee = Partial<IIssueEmployee> & Pick<IIssueEmployee, 'id'>;

export type EntityResponseType = HttpResponse<IIssueEmployee>;
export type EntityArrayResponseType = HttpResponse<IIssueEmployee[]>;

@Injectable({ providedIn: 'root' })
export class IssueEmployeeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/issue-employees', 'issue');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(issueEmployee: NewIssueEmployee): Observable<EntityResponseType> {
    return this.http.post<IIssueEmployee>(this.resourceUrl, issueEmployee, { observe: 'response' });
  }

  update(issueEmployee: IIssueEmployee): Observable<EntityResponseType> {
    return this.http.put<IIssueEmployee>(`${this.resourceUrl}/${this.getIssueEmployeeIdentifier(issueEmployee)}`, issueEmployee, {
      observe: 'response',
    });
  }

  partialUpdate(issueEmployee: PartialUpdateIssueEmployee): Observable<EntityResponseType> {
    return this.http.patch<IIssueEmployee>(`${this.resourceUrl}/${this.getIssueEmployeeIdentifier(issueEmployee)}`, issueEmployee, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIssueEmployee>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIssueEmployee[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIssueEmployeeIdentifier(issueEmployee: Pick<IIssueEmployee, 'id'>): number {
    return issueEmployee.id;
  }

  compareIssueEmployee(o1: Pick<IIssueEmployee, 'id'> | null, o2: Pick<IIssueEmployee, 'id'> | null): boolean {
    return o1 && o2 ? this.getIssueEmployeeIdentifier(o1) === this.getIssueEmployeeIdentifier(o2) : o1 === o2;
  }

  addIssueEmployeeToCollectionIfMissing<Type extends Pick<IIssueEmployee, 'id'>>(
    issueEmployeeCollection: Type[],
    ...issueEmployeesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const issueEmployees: Type[] = issueEmployeesToCheck.filter(isPresent);
    if (issueEmployees.length > 0) {
      const issueEmployeeCollectionIdentifiers = issueEmployeeCollection.map(
        issueEmployeeItem => this.getIssueEmployeeIdentifier(issueEmployeeItem)!
      );
      const issueEmployeesToAdd = issueEmployees.filter(issueEmployeeItem => {
        const issueEmployeeIdentifier = this.getIssueEmployeeIdentifier(issueEmployeeItem);
        if (issueEmployeeCollectionIdentifiers.includes(issueEmployeeIdentifier)) {
          return false;
        }
        issueEmployeeCollectionIdentifiers.push(issueEmployeeIdentifier);
        return true;
      });
      return [...issueEmployeesToAdd, ...issueEmployeeCollection];
    }
    return issueEmployeeCollection;
  }
}
