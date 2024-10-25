import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 16133,
  login: '8@99tE\\s9\\Fyy\\9kK0a\\K4TjzK\\}X',
};

export const sampleWithPartialData: IUser = {
  id: 651,
  login: 'V',
};

export const sampleWithFullData: IUser = {
  id: 15920,
  login: 'REB68@QcYUM\\.WSwY\\,-H',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
