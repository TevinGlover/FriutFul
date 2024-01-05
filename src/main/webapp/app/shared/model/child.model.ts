import { ICreditScore } from 'app/shared/model/credit-score.model';
import { IPoints } from 'app/shared/model/points.model';
import { IBalance } from 'app/shared/model/balance.model';
import { ITask } from 'app/shared/model/task.model';
import { IAchivement } from 'app/shared/model/achivement.model';
import { IParents } from 'app/shared/model/parents.model';

export interface IChild {
  id?: number;
  userFristName?: string | null;
  userLastName?: string | null;
  creditScore?: ICreditScore | null;
  points?: IPoints | null;
  account?: IBalance | null;
  task?: ITask | null;
  achivements?: IAchivement[] | null;
  parents?: IParents[] | null;
}

export const defaultValue: Readonly<IChild> = {};
