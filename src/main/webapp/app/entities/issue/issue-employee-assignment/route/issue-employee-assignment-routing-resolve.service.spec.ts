import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IIssueEmployeeAssignment } from '../issue-employee-assignment.model';
import { IssueEmployeeAssignmentService } from '../service/issue-employee-assignment.service';

import { IssueEmployeeAssignmentRoutingResolveService } from './issue-employee-assignment-routing-resolve.service';

describe('IssueEmployeeAssignment routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: IssueEmployeeAssignmentRoutingResolveService;
  let service: IssueEmployeeAssignmentService;
  let resultIssueEmployeeAssignment: IIssueEmployeeAssignment | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(IssueEmployeeAssignmentRoutingResolveService);
    service = TestBed.inject(IssueEmployeeAssignmentService);
    resultIssueEmployeeAssignment = undefined;
  });

  describe('resolve', () => {
    it('should return IIssueEmployeeAssignment returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIssueEmployeeAssignment = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultIssueEmployeeAssignment).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIssueEmployeeAssignment = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultIssueEmployeeAssignment).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IIssueEmployeeAssignment>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIssueEmployeeAssignment = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultIssueEmployeeAssignment).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
