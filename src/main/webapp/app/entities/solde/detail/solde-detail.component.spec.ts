import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SoldeDetailComponent } from './solde-detail.component';

describe('Solde Management Detail Component', () => {
  let comp: SoldeDetailComponent;
  let fixture: ComponentFixture<SoldeDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SoldeDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SoldeDetailComponent,
              resolve: { solde: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SoldeDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SoldeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load solde on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SoldeDetailComponent);

      // THEN
      expect(instance.solde()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
