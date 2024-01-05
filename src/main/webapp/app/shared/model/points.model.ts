import { IParents } from 'app/shared/model/parents.model';
import { IChild } from 'app/shared/model/child.model';

export interface IPoints {
  id?: number;
  amount?: number | null;
  used?: number | null;
  parents?: IParents | null;
  child?: IChild | null;
}

export const defaultValue: Readonly<IPoints> = {};
