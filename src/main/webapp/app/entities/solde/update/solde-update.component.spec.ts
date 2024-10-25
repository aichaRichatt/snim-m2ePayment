import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { SoldeService } from '../service/solde.service';
import { ISolde } from '../solde.model';
import { SoldeFormService } from './solde-form.service';

import { SoldeUpdateComponent } from './solde-update.component';

describe('Solde Management Update Component', () => {
  let comp: SoldeUpdateComponent;
  let fixture: ComponentFixture<SoldeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let soldeFormService: SoldeFormService;
  let soldeService: SoldeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, SoldeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SoldeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SoldeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    soldeFormService = TestBed.inject(SoldeFormService);
    soldeService = TestBed.inject(SoldeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const solde: ISolde = { id: 456 };

      activatedRoute.data = of({ solde });
      comp.ngOnInit();

      expect(comp.solde).toEqual(solde);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISolde>>();
      const solde = { id: 123 };
      jest.spyOn(soldeFormService, 'getSolde').mockReturnValue(solde);
      jest.spyOn(soldeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ solde });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: solde }));
      saveSubject.complete();

      // THEN
      expect(soldeFormService.getSolde).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(soldeService.update).toHaveBeenCalledWith(expect.objectContaining(solde));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISolde>>();
      const solde = { id: 123 };
      jest.spyOn(soldeFormService, 'getSolde').mockReturnValue({ id: null });
      jest.spyOn(soldeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ solde: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: solde }));
      saveSubject.complete();

      // THEN
      expect(soldeFormService.getSolde).toHaveBeenCalled();
      expect(soldeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISolde>>();
      const solde = { id: 123 };
      jest.spyOn(soldeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ solde });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(soldeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
