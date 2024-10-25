import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../solde.test-samples';

import { SoldeFormService } from './solde-form.service';

describe('Solde Form Service', () => {
  let service: SoldeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SoldeFormService);
  });

  describe('Service methods', () => {
    describe('createSoldeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSoldeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            clientRef: expect.any(Object),
            clientName: expect.any(Object),
            clientFirstname: expect.any(Object),
            amount: expect.any(Object),
            updatingDate: expect.any(Object),
          }),
        );
      });

      it('passing ISolde should create a new form with FormGroup', () => {
        const formGroup = service.createSoldeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            clientRef: expect.any(Object),
            clientName: expect.any(Object),
            clientFirstname: expect.any(Object),
            amount: expect.any(Object),
            updatingDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getSolde', () => {
      it('should return NewSolde for default Solde initial value', () => {
        const formGroup = service.createSoldeFormGroup(sampleWithNewData);

        const solde = service.getSolde(formGroup) as any;

        expect(solde).toMatchObject(sampleWithNewData);
      });

      it('should return NewSolde for empty Solde initial value', () => {
        const formGroup = service.createSoldeFormGroup();

        const solde = service.getSolde(formGroup) as any;

        expect(solde).toMatchObject({});
      });

      it('should return ISolde', () => {
        const formGroup = service.createSoldeFormGroup(sampleWithRequiredData);

        const solde = service.getSolde(formGroup) as any;

        expect(solde).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISolde should not enable id FormControl', () => {
        const formGroup = service.createSoldeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSolde should disable id FormControl', () => {
        const formGroup = service.createSoldeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
