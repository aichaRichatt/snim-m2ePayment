import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISolde } from '../solde.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../solde.test-samples';

import { SoldeService, RestSolde } from './solde.service';

const requireRestSample: RestSolde = {
  ...sampleWithRequiredData,
  updatingDate: sampleWithRequiredData.updatingDate?.format(DATE_FORMAT),
};

describe('Solde Service', () => {
  let service: SoldeService;
  let httpMock: HttpTestingController;
  let expectedResult: ISolde | ISolde[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SoldeService);
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

    it('should create a Solde', () => {
      const solde = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(solde).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Solde', () => {
      const solde = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(solde).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Solde', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Solde', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Solde', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSoldeToCollectionIfMissing', () => {
      it('should add a Solde to an empty array', () => {
        const solde: ISolde = sampleWithRequiredData;
        expectedResult = service.addSoldeToCollectionIfMissing([], solde);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(solde);
      });

      it('should not add a Solde to an array that contains it', () => {
        const solde: ISolde = sampleWithRequiredData;
        const soldeCollection: ISolde[] = [
          {
            ...solde,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSoldeToCollectionIfMissing(soldeCollection, solde);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Solde to an array that doesn't contain it", () => {
        const solde: ISolde = sampleWithRequiredData;
        const soldeCollection: ISolde[] = [sampleWithPartialData];
        expectedResult = service.addSoldeToCollectionIfMissing(soldeCollection, solde);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(solde);
      });

      it('should add only unique Solde to an array', () => {
        const soldeArray: ISolde[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const soldeCollection: ISolde[] = [sampleWithRequiredData];
        expectedResult = service.addSoldeToCollectionIfMissing(soldeCollection, ...soldeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const solde: ISolde = sampleWithRequiredData;
        const solde2: ISolde = sampleWithPartialData;
        expectedResult = service.addSoldeToCollectionIfMissing([], solde, solde2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(solde);
        expect(expectedResult).toContain(solde2);
      });

      it('should accept null and undefined values', () => {
        const solde: ISolde = sampleWithRequiredData;
        expectedResult = service.addSoldeToCollectionIfMissing([], null, solde, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(solde);
      });

      it('should return initial array if no Solde is added', () => {
        const soldeCollection: ISolde[] = [sampleWithRequiredData];
        expectedResult = service.addSoldeToCollectionIfMissing(soldeCollection, undefined, null);
        expect(expectedResult).toEqual(soldeCollection);
      });
    });

    describe('compareSolde', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSolde(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSolde(entity1, entity2);
        const compareResult2 = service.compareSolde(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSolde(entity1, entity2);
        const compareResult2 = service.compareSolde(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSolde(entity1, entity2);
        const compareResult2 = service.compareSolde(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
