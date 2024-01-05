import dayjs from 'dayjs';
import { IBalance } from 'app/shared/model/balance.model';

export interface ITransactions {
  id?: number;
  amount?: number | null;
  date?: dayjs.Dayjs | null;
  account?: IBalance | null;
}

export const defaultValue: Readonly<ITransactions> = {};
