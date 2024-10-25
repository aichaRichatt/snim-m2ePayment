import dayjs from 'dayjs/esm';

import { ISolde, NewSolde } from './solde.model';

export const sampleWithRequiredData: ISolde = {
  id: 7801,
  clientRef: 'après que placer',
};

export const sampleWithPartialData: ISolde = {
  id: 17015,
  clientRef: 'encourager si bien q',
  updatingDate: dayjs('2024-10-25'),
};

export const sampleWithFullData: ISolde = {
  id: 19194,
  clientRef: 'sitôt que sympathiqu',
  clientName: 'toc auprès de',
  clientFirstname: 'main-d’œuvre vraimen',
  amount: 13993.75,
  updatingDate: dayjs('2024-10-25'),
};

export const sampleWithNewData: NewSolde = {
  clientRef: 'groin groin oh adept',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
