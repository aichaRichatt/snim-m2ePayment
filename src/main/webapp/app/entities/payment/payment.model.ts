import dayjs from 'dayjs/esm';

export interface IPayment {
  id: number;
  transactionId?: string | null;
  paidAmount?: number | null;
  phoneNumber?: string | null;
  payDate?: dayjs.Dayjs | null;
  clientRef?: string | null;
  walletMessage?: string | null;
  sapMessage?: string | null;
  payWallet?: string | null;
  payWalletStatus?: string | null;
  paySapStatus?: string | null;
}

export type NewPayment = Omit<IPayment, 'id'> & { id: null };
