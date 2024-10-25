import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPayment, NewPayment } from '../payment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPayment for edit and NewPaymentFormGroupInput for create.
 */
type PaymentFormGroupInput = IPayment | PartialWithRequiredKeyOf<NewPayment>;

type PaymentFormDefaults = Pick<NewPayment, 'id'>;

type PaymentFormGroupContent = {
  id: FormControl<IPayment['id'] | NewPayment['id']>;
  transactionId: FormControl<IPayment['transactionId']>;
  paidAmount: FormControl<IPayment['paidAmount']>;
  phoneNumber: FormControl<IPayment['phoneNumber']>;
  payDate: FormControl<IPayment['payDate']>;
  clientRef: FormControl<IPayment['clientRef']>;
  walletMessage: FormControl<IPayment['walletMessage']>;
  sapMessage: FormControl<IPayment['sapMessage']>;
  payWallet: FormControl<IPayment['payWallet']>;
  payWalletStatus: FormControl<IPayment['payWalletStatus']>;
  paySapStatus: FormControl<IPayment['paySapStatus']>;
};

export type PaymentFormGroup = FormGroup<PaymentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaymentFormService {
  createPaymentFormGroup(payment: PaymentFormGroupInput = { id: null }): PaymentFormGroup {
    const paymentRawValue = {
      ...this.getFormDefaults(),
      ...payment,
    };
    return new FormGroup<PaymentFormGroupContent>({
      id: new FormControl(
        { value: paymentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      transactionId: new FormControl(paymentRawValue.transactionId, {
        validators: [Validators.required, Validators.maxLength(30)],
      }),
      paidAmount: new FormControl(paymentRawValue.paidAmount),
      phoneNumber: new FormControl(paymentRawValue.phoneNumber),
      payDate: new FormControl(paymentRawValue.payDate),
      clientRef: new FormControl(paymentRawValue.clientRef, {
        validators: [Validators.maxLength(20)],
      }),
      walletMessage: new FormControl(paymentRawValue.walletMessage, {
        validators: [Validators.maxLength(255)],
      }),
      sapMessage: new FormControl(paymentRawValue.sapMessage, {
        validators: [Validators.maxLength(255)],
      }),
      payWallet: new FormControl(paymentRawValue.payWallet, {
        validators: [Validators.maxLength(64)],
      }),
      payWalletStatus: new FormControl(paymentRawValue.payWalletStatus, {
        validators: [Validators.maxLength(64)],
      }),
      paySapStatus: new FormControl(paymentRawValue.paySapStatus, {
        validators: [Validators.maxLength(64)],
      }),
    });
  }

  getPayment(form: PaymentFormGroup): IPayment | NewPayment {
    return form.getRawValue() as IPayment | NewPayment;
  }

  resetForm(form: PaymentFormGroup, payment: PaymentFormGroupInput): void {
    const paymentRawValue = { ...this.getFormDefaults(), ...payment };
    form.reset(
      {
        ...paymentRawValue,
        id: { value: paymentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PaymentFormDefaults {
    return {
      id: null,
    };
  }
}
