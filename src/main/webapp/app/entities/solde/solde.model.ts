import dayjs from 'dayjs/esm';

export interface ISolde {
  id: number;
  clientRef?: string | null;
  clientName?: string | null;
  clientFirstname?: string | null;
  amount?: number | null;
  updatingDate?: dayjs.Dayjs | null;
}

export type NewSolde = Omit<ISolde, 'id'> & { id: null };
