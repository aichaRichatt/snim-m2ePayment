import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'c3b9e232-d478-43f1-9523-1bbc23a454ba',
};

export const sampleWithPartialData: IAuthority = {
  name: '72b524f6-3b5b-4659-9601-cb7d9cf26e28',
};

export const sampleWithFullData: IAuthority = {
  name: 'fca05028-c4ea-442b-af5a-81205e9f98b6',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
