import { IChild } from 'app/shared/model/child.model';
import { IParents } from 'app/shared/model/parents.model';

export interface IAchivement {
  id?: number;
  name?: string | null;
  pointValue?: number | null;
  child?: IChild | null;
  parents?: IParents | null;
}

export const defaultValue: Readonly<IAchivement> = {};
