import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIssueEmployee } from '../issue-employee.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../issue-employee.test-samples';

import { IssueEmployeeService } from './issue-employee.service';

const requireRestSample: IIssueEmployee = {
  ...sampleWithRequiredData,
};

describe('IssueEmployee Service', () => {
  let service: IssueEmployeeService;
  let httpMock: HttpTestingController;
  let expectedResult: IIssueEmployee | IIssueEmployee[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IssueEmployeeService);
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

    it('should create a IssueEmployee', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const issueEmployee = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(issueEmployee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IssueEmployee', () => {
      const issueEmployee = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(issueEmployee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IssueEmployee', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IssueEmployee', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IssueEmployee', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIssueEmployeeToCollectionIfMissing', () => {
      it('should add a IssueEmployee to an empty array', () => {
        const issueEmployee: IIssueEmployee = sampleWithRequiredData;
        expectedResult = service.addIssueEmployeeToCollectionIfMissing([], issueEmployee);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(issueEmployee);
      });

      it('should not add a IssueEmployee to an array that contains it', () => {
        const issueEmployee: IIssueEmployee = sampleWithRequiredData;
        const issueEmployeeCollection: IIssueEmployee[] = [
          {
            ...issueEmployee,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIssueEmployeeToCollectionIfMissing(issueEmployeeCollection, issueEmployee);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IssueEmployee to an array that doesn't contain it", () => {
        const issueEmployee: IIssueEmployee = sampleWithRequiredData;
        const issueEmployeeCollection: IIssueEmployee[] = [sampleWithPartialData];
        expectedResult = service.addIssueEmployeeToCollectionIfMissing(issueEmployeeCollection, issueEmployee);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(issueEmployee);
      });

      it('should add only unique IssueEmployee to an array', () => {
        const issueEmployeeArray: IIssueEmployee[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const issueEmployeeCollection: IIssueEmployee[] = [sampleWithRequiredData];
        expectedResult = service.addIssueEmployeeToCollectionIfMissing(issueEmployeeCollection, ...issueEmployeeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const issueEmployee: IIssueEmployee = sampleWithRequiredData;
        const issueEmployee2: IIssueEmployee = sampleWithPartialData;
        expectedResult = service.addIssueEmployeeToCollectionIfMissing([], issueEmployee, issueEmployee2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(issueEmployee);
        expect(expectedResult).toContain(issueEmployee2);
      });

      it('should accept null and undefined values', () => {
        const issueEmployee: IIssueEmployee = sampleWithRequiredData;
        expectedResult = service.addIssueEmployeeToCollectionIfMissing([], null, issueEmployee, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(issueEmployee);
      });

      it('should return initial array if no IssueEmployee is added', () => {
        const issueEmployeeCollection: IIssueEmployee[] = [sampleWithRequiredData];
        expectedResult = service.addIssueEmployeeToCollectionIfMissing(issueEmployeeCollection, undefined, null);
        expect(expectedResult).toEqual(issueEmployeeCollection);
      });
    });

    describe('compareIssueEmployee', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIssueEmployee(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIssueEmployee(entity1, entity2);
        const compareResult2 = service.compareIssueEmployee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIssueEmployee(entity1, entity2);
        const compareResult2 = service.compareIssueEmployee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIssueEmployee(entity1, entity2);
        const compareResult2 = service.compareIssueEmployee(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
