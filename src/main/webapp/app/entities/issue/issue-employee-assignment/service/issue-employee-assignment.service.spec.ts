import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIssueEmployeeAssignment } from '../issue-employee-assignment.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../issue-employee-assignment.test-samples';

import { IssueEmployeeAssignmentService } from './issue-employee-assignment.service';

const requireRestSample: IIssueEmployeeAssignment = {
  ...sampleWithRequiredData,
};

describe('IssueEmployeeAssignment Service', () => {
  let service: IssueEmployeeAssignmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IIssueEmployeeAssignment | IIssueEmployeeAssignment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IssueEmployeeAssignmentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a IssueEmployeeAssignment', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const issueEmployeeAssignment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(issueEmployeeAssignment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IssueEmployeeAssignment', () => {
      const issueEmployeeAssignment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(issueEmployeeAssignment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IssueEmployeeAssignment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IssueEmployeeAssignment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IssueEmployeeAssignment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIssueEmployeeAssignmentToCollectionIfMissing', () => {
      it('should add a IssueEmployeeAssignment to an empty array', () => {
        const issueEmployeeAssignment: IIssueEmployeeAssignment = sampleWithRequiredData;
        expectedResult = service.addIssueEmployeeAssignmentToCollectionIfMissing([], issueEmployeeAssignment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(issueEmployeeAssignment);
      });

      it('should not add a IssueEmployeeAssignment to an array that contains it', () => {
        const issueEmployeeAssignment: IIssueEmployeeAssignment = sampleWithRequiredData;
        const issueEmployeeAssignmentCollection: IIssueEmployeeAssignment[] = [
          {
            ...issueEmployeeAssignment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIssueEmployeeAssignmentToCollectionIfMissing(
          issueEmployeeAssignmentCollection,
          issueEmployeeAssignment
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IssueEmployeeAssignment to an array that doesn't contain it", () => {
        const issueEmployeeAssignment: IIssueEmployeeAssignment = sampleWithRequiredData;
        const issueEmployeeAssignmentCollection: IIssueEmployeeAssignment[] = [sampleWithPartialData];
        expectedResult = service.addIssueEmployeeAssignmentToCollectionIfMissing(
          issueEmployeeAssignmentCollection,
          issueEmployeeAssignment
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(issueEmployeeAssignment);
      });

      it('should add only unique IssueEmployeeAssignment to an array', () => {
        const issueEmployeeAssignmentArray: IIssueEmployeeAssignment[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const issueEmployeeAssignmentCollection: IIssueEmployeeAssignment[] = [sampleWithRequiredData];
        expectedResult = service.addIssueEmployeeAssignmentToCollectionIfMissing(
          issueEmployeeAssignmentCollection,
          ...issueEmployeeAssignmentArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const issueEmployeeAssignment: IIssueEmployeeAssignment = sampleWithRequiredData;
        const issueEmployeeAssignment2: IIssueEmployeeAssignment = sampleWithPartialData;
        expectedResult = service.addIssueEmployeeAssignmentToCollectionIfMissing([], issueEmployeeAssignment, issueEmployeeAssignment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(issueEmployeeAssignment);
        expect(expectedResult).toContain(issueEmployeeAssignment2);
      });

      it('should accept null and undefined values', () => {
        const issueEmployeeAssignment: IIssueEmployeeAssignment = sampleWithRequiredData;
        expectedResult = service.addIssueEmployeeAssignmentToCollectionIfMissing([], null, issueEmployeeAssignment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(issueEmployeeAssignment);
      });

      it('should return initial array if no IssueEmployeeAssignment is added', () => {
        const issueEmployeeAssignmentCollection: IIssueEmployeeAssignment[] = [sampleWithRequiredData];
        expectedResult = service.addIssueEmployeeAssignmentToCollectionIfMissing(issueEmployeeAssignmentCollection, undefined, null);
        expect(expectedResult).toEqual(issueEmployeeAssignmentCollection);
      });
    });

    describe('compareIssueEmployeeAssignment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIssueEmployeeAssignment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIssueEmployeeAssignment(entity1, entity2);
        const compareResult2 = service.compareIssueEmployeeAssignment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIssueEmployeeAssignment(entity1, entity2);
        const compareResult2 = service.compareIssueEmployeeAssignment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIssueEmployeeAssignment(entity1, entity2);
        const compareResult2 = service.compareIssueEmployeeAssignment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
