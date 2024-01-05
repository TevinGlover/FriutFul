import { ICreditScore } from 'app/shared/model/credit-score.model';
import { IPoints } from 'app/shared/model/points.model';
import { IBalance } from 'app/shared/model/balance.model';
import { ITask } from 'app/shared/model/task.model';
import { IChild } from 'app/shared/model/child.model';
import { IAchivement } from 'app/shared/model/achivement.model';

export interface IParents {
  id?: number;
  parentsFristName?: string | null;
  parentsLastName?: string | null;
  creditScore?: ICreditScore | null;
  points?: IPoints | null;
  balances?: IBalance[] | null;
  task?: ITask | null;
  children?: IChild[] | null;
  achivements?: IAchivement[] | null;
}

export const defaultValue: Readonly<IParents> = {};
