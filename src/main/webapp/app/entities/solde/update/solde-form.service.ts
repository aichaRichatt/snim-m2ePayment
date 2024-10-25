import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISolde, NewSolde } from '../solde.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISolde for edit and NewSoldeFormGroupInput for create.
 */
type SoldeFormGroupInput = ISolde | PartialWithRequiredKeyOf<NewSolde>;

type SoldeFormDefaults = Pick<NewSolde, 'id'>;

type SoldeFormGroupContent = {
  id: FormControl<ISolde['id'] | NewSolde['id']>;
  clientRef: FormControl<ISolde['clientRef']>;
  clientName: FormControl<ISolde['clientName']>;
  clientFirstname: FormControl<ISolde['clientFirstname']>;
  amount: FormControl<ISolde['amount']>;
  updatingDate: FormControl<ISolde['updatingDate']>;
};

export type SoldeFormGroup = FormGroup<SoldeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SoldeFormService {
  createSoldeFormGroup(solde: SoldeFormGroupInput = { id: null }): SoldeFormGroup {
    const soldeRawValue = {
      ...this.getFormDefaults(),
      ...solde,
    };
    return new FormGroup<SoldeFormGroupContent>({
      id: new FormControl(
        { value: soldeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      clientRef: new FormControl(soldeRawValue.clientRef, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      clientName: new FormControl(soldeRawValue.clientName, {
        validators: [Validators.maxLength(20)],
      }),
      clientFirstname: new FormControl(soldeRawValue.clientFirstname, {
        validators: [Validators.maxLength(20)],
      }),
      amount: new FormControl(soldeRawValue.amount),
      updatingDate: new FormControl(soldeRawValue.updatingDate),
    });
  }

  getSolde(form: SoldeFormGroup): ISolde | NewSolde {
    return form.getRawValue() as ISolde | NewSolde;
  }

  resetForm(form: SoldeFormGroup, solde: SoldeFormGroupInput): void {
    const soldeRawValue = { ...this.getFormDefaults(), ...solde };
    form.reset(
      {
        ...soldeRawValue,
        id: { value: soldeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SoldeFormDefaults {
    return {
      id: null,
    };
  }
}
