import { IChild } from 'app/shared/model/child.model';
import { IParents } from 'app/shared/model/parents.model';

export interface ITask {
  id?: number;
  name?: string | null;
  description?: string | null;
  pointValue?: number | null;
  completed?: boolean | null;
  children?: IChild[] | null;
  parents?: IParents[] | null;
}

export const defaultValue: Readonly<ITask> = {
  completed: false,
};
