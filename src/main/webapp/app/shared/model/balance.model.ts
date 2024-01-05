import dayjs from 'dayjs';
import { ITransactions } from 'app/shared/model/transactions.model';
import { IParents } from 'app/shared/model/parents.model';
import { IChild } from 'app/shared/model/child.model';

export interface IBalance {
  id?: number;
  creditCardType?: string | null;
  creditcardNum?: number | null;
  vaildThru?: dayjs.Dayjs | null;
  cvs?: number | null;
  maxLimit?: number | null;
  thrityPrecentLimit?: number | null;
  transactions?: ITransactions[] | null;
  parents?: IParents | null;
  child?: IChild | null;
}

export const defaultValue: Readonly<IBalance> = {};
