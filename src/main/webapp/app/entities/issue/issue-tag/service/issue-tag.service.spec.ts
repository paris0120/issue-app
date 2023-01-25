import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIssueTag } from '../issue-tag.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../issue-tag.test-samples';

import { IssueTagService } from './issue-tag.service';

const requireRestSample: IIssueTag = {
  ...sampleWithRequiredData,
};

describe('IssueTag Service', () => {
  let service: IssueTagService;
  let httpMock: HttpTestingController;
  let expectedResult: IIssueTag | IIssueTag[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IssueTagService);
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

    it('should create a IssueTag', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const issueTag = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(issueTag).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IssueTag', () => {
      const issueTag = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(issueTag).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IssueTag', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IssueTag', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IssueTag', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIssueTagToCollectionIfMissing', () => {
      it('should add a IssueTag to an empty array', () => {
        const issueTag: IIssueTag = sampleWithRequiredData;
        expectedResult = service.addIssueTagToCollectionIfMissing([], issueTag);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(issueTag);
      });

      it('should not add a IssueTag to an array that contains it', () => {
        const issueTag: IIssueTag = sampleWithRequiredData;
        const issueTagCollection: IIssueTag[] = [
          {
            ...issueTag,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIssueTagToCollectionIfMissing(issueTagCollection, issueTag);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IssueTag to an array that doesn't contain it", () => {
        const issueTag: IIssueTag = sampleWithRequiredData;
        const issueTagCollection: IIssueTag[] = [sampleWithPartialData];
        expectedResult = service.addIssueTagToCollectionIfMissing(issueTagCollection, issueTag);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(issueTag);
      });

      it('should add only unique IssueTag to an array', () => {
        const issueTagArray: IIssueTag[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const issueTagCollection: IIssueTag[] = [sampleWithRequiredData];
        expectedResult = service.addIssueTagToCollectionIfMissing(issueTagCollection, ...issueTagArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const issueTag: IIssueTag = sampleWithRequiredData;
        const issueTag2: IIssueTag = sampleWithPartialData;
        expectedResult = service.addIssueTagToCollectionIfMissing([], issueTag, issueTag2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(issueTag);
        expect(expectedResult).toContain(issueTag2);
      });

      it('should accept null and undefined values', () => {
        const issueTag: IIssueTag = sampleWithRequiredData;
        expectedResult = service.addIssueTagToCollectionIfMissing([], null, issueTag, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(issueTag);
      });

      it('should return initial array if no IssueTag is added', () => {
        const issueTagCollection: IIssueTag[] = [sampleWithRequiredData];
        expectedResult = service.addIssueTagToCollectionIfMissing(issueTagCollection, undefined, null);
        expect(expectedResult).toEqual(issueTagCollection);
      });
    });

    describe('compareIssueTag', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIssueTag(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIssueTag(entity1, entity2);
        const compareResult2 = service.compareIssueTag(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIssueTag(entity1, entity2);
        const compareResult2 = service.compareIssueTag(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIssueTag(entity1, entity2);
        const compareResult2 = service.compareIssueTag(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
