import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISolde } from '../solde.model';
import { SoldeService } from '../service/solde.service';
import { SoldeFormService, SoldeFormGroup } from './solde-form.service';

@Component({
  standalone: true,
  selector: 'jhi-solde-update',
  templateUrl: './solde-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SoldeUpdateComponent implements OnInit {
  isSaving = false;
  solde: ISolde | null = null;

  protected soldeService = inject(SoldeService);
  protected soldeFormService = inject(SoldeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SoldeFormGroup = this.soldeFormService.createSoldeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ solde }) => {
      this.solde = solde;
      if (solde) {
        this.updateForm(solde);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const solde = this.soldeFormService.getSolde(this.editForm);
    if (solde.id !== null) {
      this.subscribeToSaveResponse(this.soldeService.update(solde));
    } else {
      this.subscribeToSaveResponse(this.soldeService.create(solde));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISolde>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(solde: ISolde): void {
    this.solde = solde;
    this.soldeFormService.resetForm(this.editForm, solde);
  }
}
