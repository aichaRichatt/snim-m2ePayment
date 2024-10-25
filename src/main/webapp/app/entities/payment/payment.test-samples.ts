import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: 1525,
  transactionId: 'de façon que',
};

export const sampleWithPartialData: IPayment = {
  id: 12386,
  transactionId: 'clac sitôt que',
  paidAmount: 18408.3,
  phoneNumber: 'grossir snob puisque',
  payDate: dayjs('2024-10-25'),
  clientRef: "à l'instar de tsoin-",
  walletMessage: 'soudain si bien que quelquefois',
  payWalletStatus: 'régner',
  paySapStatus: 'zzzz infime',
};

export const sampleWithFullData: IPayment = {
  id: 8102,
  transactionId: 'hé depuis',
  paidAmount: 3631.1,
  phoneNumber: 'doucement',
  payDate: dayjs('2024-10-25'),
  clientRef: 'au-dessus de',
  walletMessage: 'ah administration appartenir',
  sapMessage: 'ralentir concernant confier',
  payWallet: 'de façon que du moment que membre à vie',
  payWalletStatus: 'membre du personnel',
  paySapStatus: 'après',
};

export const sampleWithNewData: NewPayment = {
  transactionId: 'asseoir jusqu’à ce que hebdoma',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
