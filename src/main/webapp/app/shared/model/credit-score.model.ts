import dayjs from 'dayjs';
import { IParents } from 'app/shared/model/parents.model';
import { IChild } from 'app/shared/model/child.model';

export interface ICreditScore {
  id?: number;
  month?: dayjs.Dayjs | null;
  scoreNumber?: number | null;
  parents?: IParents | null;
  child?: IChild | null;
}

export const defaultValue: Readonly<ICreditScore> = {};
